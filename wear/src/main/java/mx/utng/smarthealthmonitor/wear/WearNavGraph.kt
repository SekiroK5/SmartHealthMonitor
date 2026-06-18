package mx.utng.smarthealthmonitor.wear.presentation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.*

object WearScreens {
    const val DASHBOARD = "wear_dashboard"
    const val ALERTA    = "wear_alerta"
    const val HISTORIAL = "wear_historial"  // ← NUEVO
}

@Composable
fun SmartHealthWearNavGraph() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController    = navController,
        startDestination = WearScreens.DASHBOARD
    ) {
        composable(WearScreens.DASHBOARD) {
            WearDashboardScreen(
                onAlertClick     = { navController.navigate(WearScreens.ALERTA) },
                onHistorialClick = { navController.navigate(WearScreens.HISTORIAL) }
            )
        }

        composable(WearScreens.ALERTA) {
            val vm: WearDashboardViewModel = viewModel()
            val fc by vm.fc.collectAsState()
            WearAlertaScreen(
                fc          = fc,
                onConfirmar = { navController.popBackStack() },
                onCancelar  = { navController.popBackStack() }
            )
        }

        composable(WearScreens.HISTORIAL) {
            WearHistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}