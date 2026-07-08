package mx.utng.smarthealthmonitor.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smarthealthmonitor.data.db.LecturaFC

/**
 * TvViewModel — conecta la UI de Android TV con los datos reactivos.
 *
 * Expone:
 *  - fc: StateFlow<Int>          → FC actual del wearable
 *  - historial: StateFlow<List<LecturaFC>> → historial de Room (mock local)
 *
 * En producción, se inyectaría SmartHealthRepository (del módulo :app)
 * a través de un SmartHealthApplication compartido.
 */
class TvViewModel : ViewModel() {

    // FC actual — arranca en 0 hasta recibir dato del wearable
    private val _fcActual = MutableStateFlow(0)
    val fc: StateFlow<Int> = _fcActual.asStateFlow()

    // Historial reactivo — se actualiza con Room en producción
    private val _historial = MutableStateFlow<List<LecturaFC>>(emptyList())
    val historial: StateFlow<List<LecturaFC>> = _historial.asStateFlow()

    init {
        // Datos de demostración mientras Room del módulo :app no está enlazado
        _fcActual.value = 78
        _historial.value = listOf(
            LecturaFC(id=2,  valorBpm=72,  hora="08:00"),
            LecturaFC(id=3,  valorBpm=68,  hora="09:30"),
            LecturaFC(id=4,  valorBpm=91,  hora="11:00"),
            LecturaFC(id=5,  valorBpm=110, hora="12:30"),
            LecturaFC(id=6,  valorBpm=75,  hora="14:00"),
            LecturaFC(id=7,  valorBpm=130, hora="16:00"),
            LecturaFC(id=8,  valorBpm=65,  hora="17:30"),
            LecturaFC(id=9,  valorBpm=88,  hora="19:00")
        )
    }
}
