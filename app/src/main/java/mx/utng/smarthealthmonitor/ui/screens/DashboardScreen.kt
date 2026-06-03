package mx.utng.smarthealthmonitor.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.BuildConfig
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.models.MockData
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.ui.components.FilaHistorial
import mx.utng.smarthealthmonitor.ui.components.TarjetaDato
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val fc    by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val historial by viewModel.historial.collectAsState()
    val scope = rememberCoroutineScope()

    SmartHealthMonitorTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("SmartHealth Monitor") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor    = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAlertClick,
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Enviar alerta",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    TarjetaDato(
                        valor      = "$fc",
                        unidad     = "bpm",
                        label      = "Frecuencia cardíaca",
                        colorValor = MaterialTheme.colorScheme.error
                    )
                }
                item {
                    TarjetaDato(
                        valor      = "%,d".format(pasos),
                        unidad     = "pasos",
                        label      = "Pasos del día",
                        colorValor = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            "Historial reciente",
                            style = MaterialTheme.typography.titleMedium
                        )
                        TextButton(onClick = onHistorialClick) {
                            Text("Ver todo")
                        }
                    }
                }
                items(historial, key = { it.id }) { lectura ->
                    FilaHistorial(lectura = lectura)
                }

                // Botón simulación wearable — solo en DEBUG
                item {
                    if (BuildConfig.DEBUG) {
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    SmartHealthRepository.actualizarFC((60..110).random())
                                }
                                SmartHealthRepository.actualizarPasos((3000..8000).random())
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Simular dato del wearable (DEBUG)")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Dashboard - Light",
    showSystemUi = true, device = "id:pixel_6")
@Preview(showBackground = true, name = "Dashboard - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardScreenPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen()
    }
}