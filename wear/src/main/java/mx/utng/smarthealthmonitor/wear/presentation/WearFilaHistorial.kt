package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.*

@Composable
fun WearFilaHistorial(lectura: LecturaFCWear) {
    val color = if (lectura.esNormal)
        MaterialTheme.colors.primary
    else
        MaterialTheme.colors.error

    Chip(
        label = {
            Text(
                text  = "${lectura.valorBpm} bpm",
                color = color
            )
        },
        secondaryLabel = { Text(lectura.hora) },
        onClick  = { },
        colors   = ChipDefaults.secondaryChipColors(),
        modifier = Modifier.fillMaxWidth()
    )
}