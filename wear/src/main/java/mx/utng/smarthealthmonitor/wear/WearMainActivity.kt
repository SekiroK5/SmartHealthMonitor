package mx.utng.smarthealthmonitor.wear

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.wear.presentation.SmartHealthWearNavGraph
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {

    // ← API moderna para permisos (reemplaza onRequestPermissionsResult)
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        if (permisos[Manifest.permission.BODY_SENSORS] == true) {
            registrarHealthService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BODY_SENSORS,
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
            )
        } else {
            registrarHealthService()
        }

        setContent {
            SmartHealthWearTheme {
                SmartHealthWearNavGraph()
            }
        }
    }

    private fun registrarHealthService() {
        lifecycleScope.launch {
            HealthDataService.registrar(applicationContext)
        }
    }
}