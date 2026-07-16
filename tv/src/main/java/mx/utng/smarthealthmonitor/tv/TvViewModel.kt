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
    val fcActual: Int = 0,
    val fcEstado: String = "Normal",
    val ultimaHora: String = "",
    val isLoading: Boolean = false
)

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
    
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow)

    /** Retrocompatibilidad Leanback */
    val historial: StateFlow<List<LecturaFC>> = _lecturas.asStateFlow()

    init {
        // Datos de demostración
        _fcActual.value = 78
        val demoLecturas = listOf(
            LecturaFC(id = 2,  valorBpm = 72,  hora = "08:00"),
            LecturaFC(id = 3,  valorBpm = 68,  hora = "09:30"),
            LecturaFC(id = 4,  valorBpm = 91,  hora = "11:00"),
            LecturaFC(id = 5,  valorBpm = 110, hora = "12:30"),
            LecturaFC(id = 6,  valorBpm = 75,  hora = "14:00"),
            LecturaFC(id = 7,  valorBpm = 130, hora = "16:00"),
            LecturaFC(id = 8,  valorBpm = 65,  hora = "17:30"),
            LecturaFC(id = 9,  valorBpm = 88,  hora = "19:00")
        )
        _lecturas.value = demoLecturas
        _state.value = TvUiState(lecturas = demoLecturas, fcActual = 78)

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
