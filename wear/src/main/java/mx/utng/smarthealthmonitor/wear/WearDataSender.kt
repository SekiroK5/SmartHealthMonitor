package mx.utng.smarthealthmonitor.wear

import android.content.Context
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {

    companion object {
        const val PATH_FC    = "/smarthealthmonitor/fc"
        const val PATH_PASOS = "/smarthealthmonitor/pasos"
    }

    suspend fun enviarFC(bpm: Int) {
        try {
            val nodes = Wearable.getNodeClient(context)
                .connectedNodes.await()
            nodes.forEach { node ->
                Wearable.getMessageClient(context).sendMessage(
                    node.id,
                    PATH_FC,
                    bpm.toString().toByteArray()
                ).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun enviarPasos(pasos: Int) {
        try {
            val nodes = Wearable.getNodeClient(context)
                .connectedNodes.await()
            nodes.forEach { node ->
                Wearable.getMessageClient(context).sendMessage(
                    node.id,
                    PATH_PASOS,
                    pasos.toString().toByteArray()
                ).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
