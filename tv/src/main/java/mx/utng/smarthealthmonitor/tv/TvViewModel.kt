package mx.utng.smarthealthmonitor.tv

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.data.db.LecturaFC

import mx.utng.smarthealthmonitor.mqtt.TvMessage
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber
import kotlinx.coroutines.launch

/** UI state expuesto a las pantallas Compose */
data class TvUiState(
    val lecturas: List<LecturaFC> = emptyList(),
    val estadisticas: List<LecturaFC> = emptyList(),
    val fcActual: Int = 0,
    val fcEstado: String = "Normal",
    val ultimaHora: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

fun mx.utng.smarthealthmonitor.tv.data.remote.LecturaFcDto.toLecturaFC(): LecturaFC {
    return LecturaFC(
        id = this.id,
        valorBpm = this.bpm,
        esNormal = this.estado == "Normal",
        dispositivo = this.dispositivo,
        hora = this.hora,
        sincronizado = true
    )
}

/**
 * TvViewModel — datos reactivos para las pantallas Compose for TV.
 *
 * Expone:
 *  - state: StateFlow<TvUiState>  → usado con collectAsStateWithLifecycle()
 *  - fc   : StateFlow<Int>        → FC actual (retrocompatibilidad con Leanback)
 */
class TvViewModel(private val context: Context) : ViewModel() {

    private val _fcActual  = MutableStateFlow(78)
    val fc: StateFlow<Int> = _fcActual.asStateFlow()

    private val _lecturas  = MutableStateFlow<List<LecturaFC>>(emptyList())

    /** State principal consumido por TvDetailScreen */
    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()
    
    private val neonRepo = mx.utng.smarthealthmonitor.tv.data.TvNeonRepository()
    
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow)

    /** Retrocompatibilidad Leanback */
    val historial: StateFlow<List<LecturaFC>> = _lecturas.asStateFlow()

    init {
        cargarDatos()

        mqttSubscriber.connect()

        viewModelScope.launch {
            mqttFlow.collect { tvMsg ->
                tvMsg ?: return@collect
                
                // Actualizamos state principal (Compose)
                _state.update { it.copy(
                    fcActual = tvMsg.bpm,
                    fcEstado = tvMsg.estado,
                    ultimaHora = tvMsg.hora,
                    isLoading = false
                )}
                
                // Actualizamos Flow Leanback
                _fcActual.value = tvMsg.bpm
            }
        }
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val lecturas = neonRepo.obtenerHistorialCompleto(50)
                val stats    = neonRepo.obtenerEstadisticas()
                val historialRoom = lecturas.map { it.toLecturaFC() }
                _lecturas.value = historialRoom
                _state.update { it.copy(
                    lecturas  = historialRoom,
                    estadisticas = stats.map { it.toLecturaFC() },
                    isLoading = false
                )}
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
    
    fun refresh() = cargarDatos()

    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }
}

/** Factory requerida por la guía para instanciar el ViewModel con Context */
class TvViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TvViewModel(context) as T
}
