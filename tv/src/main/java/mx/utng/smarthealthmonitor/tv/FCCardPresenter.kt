package mx.utng.smarthealthmonitor.tv

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter
import mx.utng.smarthealthmonitor.data.db.LecturaFC

class FCCardPresenter : Presenter() {

    companion object {
        private const val CARD_WIDTH  = 280   // dp equivalente — TV 1080p
        private const val CARD_HEIGHT = 200
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        // Creamos la tarjeta manualmente para tener control total del diseño
        val context = parent.context
        val cardLayout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            isFocusable            = true
            isFocusableInTouchMode = true

            val px = (CARD_WIDTH * context.resources.displayMetrics.density).toInt()
            val py = (CARD_HEIGHT * context.resources.displayMetrics.density).toInt()
            layoutParams = ViewGroup.LayoutParams(px, py)

            // Borde redondeado + sombra al recibir foco
            elevation = 8f

            // fondo oscuro base que se sobreescribe en onBind
            setBackgroundColor(Color.parseColor("#1B2A3B"))
        }

        // Área superior — icono grande
        val iconView = TextView(context).apply {
            id = android.view.View.generateViewId()
            textSize = 52f
            gravity  = Gravity.CENTER
            setTextColor(Color.WHITE)
            val weight = android.widget.LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.6f)
            layoutParams = weight
        }

        // Área inferior — textos
        val infoLayout = android.widget.LinearLayout(context).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            gravity     = Gravity.CENTER
            setPadding(24, 12, 24, 12)
            val weight  = android.widget.LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.4f)
            layoutParams = weight
        }

        val bpmText = TextView(context).apply {
            id = android.view.View.generateViewId()
            textSize  = 20f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setTextColor(Color.WHITE)
            gravity   = Gravity.CENTER
        }
        val horaText = TextView(context).apply {
            id = android.view.View.generateViewId()
            textSize  = 13f
            setTextColor(Color.parseColor("#B0BEC5"))
            gravity   = Gravity.CENTER
        }

        infoLayout.addView(bpmText)
        infoLayout.addView(horaText)
        cardLayout.addView(iconView)
        cardLayout.addView(infoLayout)

        // Guardamos referencias en el tag para usarlas en onBind
        cardLayout.tag = Triple(iconView, bpmText, horaText)

        return ViewHolder(cardLayout)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val card    = viewHolder.view as android.widget.LinearLayout
        val lectura = item as LecturaFC

        val (iconView, bpmText, horaText) =
            card.tag as Triple<*, *, *>

        // Textos
        (bpmText as TextView).text = "${lectura.valorBpm} bpm"
        (horaText as TextView).text = lectura.hora

        val normal = lectura.esNormal

        // Icono emoji — ❤️ normal, ⚠️ alerta
        (iconView as TextView).text = if (normal) "❤️" else "⚠️"

        // Fondo con degradado según estado
        val topColor  = if (normal) Color.parseColor("#1B4F8A") else Color.parseColor("#8C1D17")
        val botColor  = if (normal) Color.parseColor("#0D1F3C") else Color.parseColor("#5A0D0D")

        val grad = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(topColor, botColor)
        ).apply { cornerRadius = 16f }

        card.background = grad
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        // nada que limpiar
    }
}
