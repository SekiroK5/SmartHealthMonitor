package mx.utng.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

data class LecturaFCWear(
    val id: Int,
    val valorBpm: Int,
    val hora: String,
    val esNormal: Boolean = valorBpm in 60..100
)

class WearDashboardViewModel : ViewModel() {

    private val _fc = MutableStateFlow(72)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    fun subirFC() { _fc.value = (_fc.value + 5).coerceAtMost(150) }
    fun bajarFC()  { _fc.value = (_fc.value - 5).coerceAtLeast(40) }

    private val _historial = MutableStateFlow(
        listOf(
            LecturaFCWear(1,  78,  "11:00"),
            LecturaFCWear(2,  82,  "10:30"),
            LecturaFCWear(3,  76,  "10:00"),
            LecturaFCWear(4,  105, "09:30"),
            LecturaFCWear(5,  71,  "09:00"),
            LecturaFCWear(6,  80,  "08:30"),
            LecturaFCWear(7,  74,  "08:00"),
            LecturaFCWear(8,  112, "07:30"),
            LecturaFCWear(9,  68,  "07:00"),
            LecturaFCWear(10, 90,  "06:30")
        )
    )
    val historial: StateFlow<List<LecturaFCWear>> = _historial.asStateFlow()
}