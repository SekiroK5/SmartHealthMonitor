package mx.utng.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WearDashboardViewModel : ViewModel() {

    private val _fc = MutableStateFlow(72)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    fun subirFC() {
        _fc.value = (_fc.value + 5).coerceAtMost(150)
    }

    fun bajarFC() {
        _fc.value = (_fc.value - 5).coerceAtLeast(40)
    }
}