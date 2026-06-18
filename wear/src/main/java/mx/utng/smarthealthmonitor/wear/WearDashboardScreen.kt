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
    onHistorialClick: () -> Unit = {},
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
            item {
                WearFCCard(
                    fc       = fc,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CompactChip(
                        label  = { Text("▼ FC") },
                        onClick = { viewModel.bajarFC() },
                        colors  = ChipDefaults.primaryChipColors(
                            backgroundColor = MaterialTheme.colors.surface
                        )
                    )
                    CompactChip(
                        label  = { Text("▲ FC") },
                        onClick = { viewModel.subirFC() },
                        colors  = ChipDefaults.primaryChipColors(
                            backgroundColor = MaterialTheme.colors.surface
                        )
                    )
                }
            }

            item {
                Chip(
                    label    = { Text("📋 Historial") },
                    onClick   = onHistorialClick,
                    modifier  = Modifier.fillMaxWidth()
                )
            }

            item {
                Chip(
                    label    = { Text("⚠ Alerta") },
                    onClick   = onAlertClick,
                    colors    = ChipDefaults.primaryChipColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    modifier  = Modifier.fillMaxWidth()
                )
            }
        }
    }
}