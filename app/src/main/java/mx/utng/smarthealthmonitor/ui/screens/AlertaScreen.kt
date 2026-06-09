package mx.utng.smarthealthmonitor.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@Composable
fun AlertaScreen(
    fc: Int,
    onDismiss: () -> Unit,
    onConfirmar: (nota: String) -> Unit   // ← ahora recibe la nota
) {
    var enviando by remember { mutableStateOf(false) }
    var nota     by remember { mutableStateOf("") }  // ← estado de la nota

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector        = Icons.Default.Warning,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.error,
                modifier           = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text  = "Enviar alerta de emergencia",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text  = "FC actual: $fc bpm",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Se notificará a tus contactos de emergencia.\n" +
                            "Esta acción no se puede deshacer."
                )
                // ── Nota opcional ──────────────────────────
                OutlinedTextField(
                    value       = nota,
                    onValueChange = { nota = it },
                    label       = { Text("Nota opcional") },
                    placeholder = { Text("Me siento mareado...") },
                    singleLine  = false,
                    maxLines    = 3,
                    modifier    = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    enviando = true
                    onConfirmar(nota)   // ← pasa la nota al confirmar
                },
                enabled = !enviando,
                colors  = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor   = MaterialTheme.colorScheme.onError
                )
            ) {
                if (enviando) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(20.dp),
                        color       = MaterialTheme.colorScheme.onError,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "CONFIRMAR ALERTA",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true, name = "Alerta - Light")
@Preview(
    showBackground = true,
    name = "Alerta - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AlertaScreenPreview() {
    SmartHealthMonitorTheme {
        AlertaScreen(fc = 145, onDismiss = { }, onConfirmar = { _ -> })
    }
}