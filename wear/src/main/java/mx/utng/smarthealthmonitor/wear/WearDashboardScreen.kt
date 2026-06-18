package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.google.android.horologist.compose.layout.scrollAway
import mx.utng.smarthealthmonitor.wear.presentation.components.WearFCCard

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit = {},
    viewModel: WearDashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state    = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Item 1: Card de FC
            item {
                WearFCCard(
                    fc       = fc,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Item 2: Botones subir/bajar FC
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Bajar FC
                    CompactChip(
                        label  = { Text("▼ FC") },
                        onClick = { viewModel.bajarFC() },
                        colors  = ChipDefaults.primaryChipColors(
                            backgroundColor = MaterialTheme.colors.surface
                        )
                    )
                    // Subir FC
                    CompactChip(
                        label  = { Text("▲ FC") },
                        onClick = { viewModel.subirFC() },
                        colors  = ChipDefaults.primaryChipColors(
                            backgroundColor = MaterialTheme.colors.surface
                        )
                    )
                }
            }

            // Item 3: Chip de Alerta
            item {
                Chip(
                    label   = { Text("⚠ Alerta") },
                    onClick  = onAlertClick,
                    colors   = ChipDefaults.primaryChipColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}