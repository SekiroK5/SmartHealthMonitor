package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.db.LecturaFC

class MainFragment : BrowseSupportFragment() {

    private val viewModel: TvViewModel by viewModels()

    private lateinit var estadoAdapter: ArrayObjectAdapter
    private lateinit var histAdapter: ArrayObjectAdapter
    private lateinit var alertasAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del BrowseFragment
        title        = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Color de la marca en el sidebar
        brandColor = resources.getColor(R.color.sh_primary, null)

        cargarFilas()
        observarDatos()
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual ─────────────────────────
        estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem("Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC ───────────────────────
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem("Historial FC"), histAdapter))

        // ── Fila 3: Alertas recientes ─────────────────────
        alertasAdapter = ArrayObjectAdapter(FCCardPresenter())
        alertasAdapter.add(LecturaFC(id=10, valorBpm=145, hora="10:30 AM", esNormal=false))
        alertasAdapter.add(LecturaFC(id=11, valorBpm=45,  hora="06:15 AM", esNormal=false))
        alertasAdapter.add(LecturaFC(id=12, valorBpm=160, hora="Ayer",     esNormal=false))
        rowsAdapter.add(ListRow(HeaderItem("Alertas recientes"), alertasAdapter))

        this.adapter = rowsAdapter
    }

    private fun observarDatos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Observar FC actual → fila "Estado actual"
                launch {
                    viewModel.fc.collect { bpm ->
                        estadoAdapter.clear()
                        estadoAdapter.add(LecturaFC(id=0, valorBpm=bpm,  hora="FC Actual"))
                        estadoAdapter.add(LecturaFC(id=1, valorBpm=65,   hora="FC Mínima"))
                        estadoAdapter.add(LecturaFC(id=8, valorBpm=bpm+37, hora="FC Máxima"))
                    }
                }

                // Observar historial de Room → fila "Historial FC"
                launch {
                    viewModel.historial.collect { lecturas ->
                        histAdapter.clear()
                        lecturas.forEach { histAdapter.add(it) }
                    }
                }
            }
        }
    }
}
