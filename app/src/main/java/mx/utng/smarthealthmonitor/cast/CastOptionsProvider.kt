package mx.utng.smarthealthmonitor.cast

import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

class CastOptionsProvider : OptionsProvider {
    override fun getCastOptions(ctx: Context): CastOptions =
        CastOptions.Builder()
            .setReceiverApplicationId(
                // DEFAULT_MEDIA_RECEIVER_APPLICATION_ID:
                // Web Receiver genérico de Google, no requiere App ID propio para pruebas
                CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID
            )
            .build()

    override fun getAdditionalSessionProviders(ctx: Context): List<SessionProvider> =
        emptyList()
}
