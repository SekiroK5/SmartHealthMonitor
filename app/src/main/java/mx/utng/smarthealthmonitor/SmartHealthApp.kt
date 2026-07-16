package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.smarthealthmonitor.data.SmartHealthRepository

import mx.utng.smarthealthmonitor.mqtt.MqttAppService

class SmartHealthApp : Application() {
    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)

        // Inicializar MQTT con el StateFlow del Repository
        mqttService = MqttAppService(
            context = this,
            fcFlow  = SmartHealthRepository._fcFlow // Note: The guide said fcFlow, but it must be a MutableStateFlow. Let's check repository.
        )
        mqttService.connect()
    }
}
