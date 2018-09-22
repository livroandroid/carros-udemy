package br.com.livroandroid.carros.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.livroandroid.carros.activity.CarroActivity

import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.FavoritosService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.event.FavoritoEvent
import br.com.livroandroid.carros.extensions.*
import kotlinx.android.synthetic.main.adapter_carro.*

import kotlinx.android.synthetic.main.fragment_carros.*
import kotlinx.android.synthetic.main.include_progress.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.custom.onUiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoritosFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        // Registra para receber os eventos do bus
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init Views
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()

        swipeToRefresh.setOnRefreshListener {
            taskCarros()
        }
        swipeToRefresh.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        taskCarros()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_fragment_carros, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                taskCarros()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun taskCarros() {

        if(!swipeToRefresh.isRefreshing) {
            visible(progress)
        }

        doAsync {

            val carros = FavoritosService.getCarros()

            uiThread {
                recyclerView.adapter = CarroAdapter(carros) { c, longClick ->
                    if(! longClick) {
                        activity?.startActivity<CarroActivity>("carro" to c)
                    } else {
                        toast("LongClick ${c.nome}")
                    }
                }

                invisible(progress)
                swipeToRefresh.isRefreshing = false
            }

        }
    }

    @Subscribe
    fun onBusFavoritos(event: FavoritoEvent) {
        // Recebe o evento e atualiza os favoritos
        taskCarros()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Cancela os eventos do bus
        EventBus.getDefault().unregister(this)
    }
}
