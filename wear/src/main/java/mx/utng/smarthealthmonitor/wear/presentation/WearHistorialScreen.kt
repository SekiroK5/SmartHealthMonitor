package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.foundation.rotary.rotaryScrollable
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText

@Composable
fun WearHistorialScreen(
    onBack: () -> Unit,
    viewModel: WearDashboardViewModel = viewModel()
) {
    val historial      by viewModel.historial.collectAsState()
    val listState       = rememberScalingLazyListState()
    val focusRequester  = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        timeText = {
            // ← sin scrollAway para evitar el conflicto de Horologist
            TimeText()
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state    = listState,
            modifier = Modifier
                .fillMaxSize()
                .rotaryScrollable(
                    behavior       = RotaryScrollableDefaults.behavior(
                        scrollableState = listState
                    ),
                    focusRequester = focusRequester
                )
        ) {
            item {
                Text(
                    text     = "Historial (${historial.size})",
                    style    = MaterialTheme.typography.title3,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (historial.isEmpty()) {
                item {
                    Text(
                        text     = "Sin lecturas aún",
                        style    = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                items(historial, key = { it.id }) { lectura ->
                    WearFilaHistorial(lectura = lectura)
                }
            }
        }
    }
}