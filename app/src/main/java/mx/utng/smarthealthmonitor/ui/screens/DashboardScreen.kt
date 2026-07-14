package mx.utng.smarthealthmonitor.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.ui.components.FilaHistorial
import mx.utng.smarthealthmonitor.ui.components.TarjetaDato
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val fc       by viewModel.fc.collectAsState()
    val pasos    by viewModel.pasos.collectAsState()
    val historial by viewModel.historial.collectAsState()

    // ── Estado del diálogo y Snackbar ──────────────────────────
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHost  = remember { SnackbarHostState() }
    val scope         = rememberCoroutineScope()

    // ── Diálogo condicional ────────────────────────────────────
    if (mostrarAlerta) {
        AlertaScreen(
            fc          = fc,
            onDismiss   = { mostrarAlerta = false },
            onConfirmar = { nota ->         // ← recibe la nota
                mostrarAlerta = false
                scope.launch {
                    snackbarHost.showSnackbar(
                        message  = "✅ Alerta enviada a tus contactos de emergencia",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        )
    }

    SmartHealthMonitorTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHost) },
            topBar = {
                TopAppBar(
                    title = { Text("SmartHealth Monitor") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor    = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    actions = {
                        // CastButton: AndroidView que envuelve MediaRouteButton
                        // El ícono Cast es: gris (sin Chromecasts) / blanco (disponibles) / animado (conectando)
                        AndroidView(
                            factory = { context ->
                                MediaRouteButton(context).apply {
                                    CastButtonFactory.setUpMediaRouteButton(context, this)
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick        = { mostrarAlerta = true },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(
                        imageVector        = Icons.Default.Warning,
                        contentDescription = "Enviar alerta de emergencia",
                        tint               = MaterialTheme.colorScheme.onError
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier       = Modifier
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

                // Botón simulación wearable
                item {
                    OutlinedButton(
                        onClick = {
                            SmartHealthRepository.actualizarPasos((3000..8000).random())
                            scope.launch {
                                SmartHealthRepository.actualizarFC((60..110).random())
                            }
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

@Preview(
    showBackground = true, name = "Dashboard - Light",
    showSystemUi = true, device = "id:pixel_6"
)
@Preview(
    showBackground = true, name = "Dashboard - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DashboardScreenPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen()
    }
}