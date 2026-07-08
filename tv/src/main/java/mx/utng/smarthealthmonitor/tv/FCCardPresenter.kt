package mx.utng.smarthealthmonitor.tv
 
import android.graphics.Color
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import mx.utng.smarthealthmonitor.data.db.LecturaFC
 
class FCCardPresenter : Presenter() {
 
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable           = true
            isFocusableInTouchMode = true
            setMainImageDimensions(240, 180)
            // Centrar el icono para que se vea elegante en la tarjeta
            setMainImageScaleType(android.widget.ImageView.ScaleType.CENTER)
        }
        return ViewHolder(cardView)
    }
 
    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val card    = viewHolder.view as ImageCardView
        val lectura = item as LecturaFC
 
        card.titleText   = "${lectura.valorBpm} bpm"
        card.contentText = lectura.hora
 
        val bgColor = if (lectura.esNormal) {
            Color.parseColor("#1B4F8A")  // primary
        } else {
            Color.parseColor("#B3261E")  // error
        }
        
        // Asignar el color base a la tarjeta inferior (info area)
        card.setBackgroundColor(bgColor)
        
        // Asignar un color un poco más oscuro al fondo de la imagen superior
        card.mainImageView.setBackgroundColor(
            Color.parseColor(if(lectura.esNormal) "#143A66" else "#8C1D17")
        )

        // Cargar el icono vectorial usando ContextCompat
        val iconRes = if (lectura.esNormal) R.drawable.ic_heart else R.drawable.ic_alert
        card.mainImage = androidx.core.content.ContextCompat.getDrawable(card.context, iconRes)
    }
 
    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        (viewHolder.view as ImageCardView).mainImage = null
    }
}
