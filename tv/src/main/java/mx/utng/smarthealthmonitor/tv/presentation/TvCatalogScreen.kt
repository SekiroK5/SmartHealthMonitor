@file:OptIn(androidx.tv.material3.ExperimentalTvMaterial3Api::class)

package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Surface
import mx.utng.smarthealthmonitor.data.db.LecturaFC
import mx.utng.smarthealthmonitor.tv.TvViewModel
import mx.utng.smarthealthmonitor.tv.TvViewModelFactory

@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel(factory = TvViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val alertas = state.lecturas.filter { !it.esNormal }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0D1B4A), Color(0xFF0D1117))))
            .padding(48.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Header
        Text("SmartHealth TV", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Text("FC actual: ${state.fcActual} bpm", color = Color(0xFFD4860A), fontSize = 18.sp)

        Spacer(Modifier.height(8.dp))

        CatalogRow(title = "Historial FC",       items = state.lecturas, onCardClick = onCardClick)
        if (alertas.isNotEmpty()) {
            CatalogRow(title = "Alertas recientes", items = alertas,         onCardClick = onCardClick)
        }
    }
}

@Composable
private fun CatalogRow(title: String, items: List<LecturaFC>, onCardClick: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, color = Color.White.copy(alpha = 0.7f), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(items, key = { it.id }) { lectura ->
                FcCard(lectura = lectura, onClick = { onCardClick(lectura.id) })
            }
        }
    }
}

@Composable
private fun FcCard(lectura: LecturaFC, onClick: () -> Unit) {
    val bgTop = if (lectura.esNormal) Color(0xFF1B4F8A) else Color(0xFF8C1D17)
    val bgBot = if (lectura.esNormal) Color(0xFF0D1F3C) else Color(0xFF5A0D0D)
    val focusedBg = if (lectura.esNormal) Color(0xFF2979FF) else Color(0xFFD32F2F)

    Surface(
        onClick  = onClick,
        modifier = Modifier.width(200.dp).height(160.dp),
        colors   = ClickableSurfaceDefaults.colors(
            containerColor        = bgTop,
            focusedContainerColor = focusedBg
        ),
        shape    = ClickableSurfaceDefaults.shape(RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(bgTop, bgBot))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = if (lectura.esNormal) "❤️" else "⚠️", fontSize = 40.sp)
                Text(text = "${lectura.valorBpm} bpm", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = lectura.hora, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            }
        }
    }
}
