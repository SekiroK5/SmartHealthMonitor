package mx.utng.smarthealthmonitor.wear.watchface

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import androidx.wear.watchface.*
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

class SmartHealthRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    interactiveDrawModeUpdateDelayMillis: Long
) : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    CanvasType.HARDWARE,
    interactiveDrawModeUpdateDelayMillis,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    private val prefs = context.getSharedPreferences("smarthealthmonitor", 0)

    private val paintHora = Paint().apply {
        color       = Color.WHITE
        textSize    = 72f
        isAntiAlias = true
        typeface    = Typeface.DEFAULT_BOLD
    }

    private val paintFC = Paint().apply {
        textSize    = 30f
        isAntiAlias = true
    }

    private val paintSub = Paint().apply {
        color       = Color.GRAY
        textSize    = 22f
        isAntiAlias = true
    }

    override suspend fun createSharedAssets(): Renderer.SharedAssets =
        object : Renderer.SharedAssets { override fun onDestroy() {} }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: Renderer.SharedAssets
    ) {
        canvas.drawColor(Color.BLACK)

        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()

        // Hora digital centrada
        val hora = String.format(
            "%02d:%02d",
            zonedDateTime.hour,
            zonedDateTime.minute
        )
        val tw = paintHora.measureText(hora)
        canvas.drawText(hora, cx - tw / 2, cy - 10f, paintHora)

        // Segundos
        val seg = String.format("%02d", zonedDateTime.second)
        canvas.drawText(seg, cx - 18f, cy + 30f, paintSub)

        // FC desde SharedPreferences — se actualiza cada segundo
        val fc = prefs.getInt("fc_actual", 72)
        val esNormal = fc in 60..100
        paintFC.color = if (esNormal) Color.GREEN else Color.RED

        val fcStr = "❤ $fc bpm"
        val fcW   = paintFC.measureText(fcStr)
        canvas.drawText(fcStr, cx - fcW / 2, cy + 70f, paintFC)
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: Renderer.SharedAssets
    ) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }
}