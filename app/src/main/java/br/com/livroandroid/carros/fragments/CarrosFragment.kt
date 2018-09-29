package br.com.livroandroid.carros.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.CarroActivity
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.event.CarroEvent
import br.com.livroandroid.carros.extensions.invisible
import br.com.livroandroid.carros.extensions.runOnUiThread
import br.com.livroandroid.carros.extensions.toast
import br.com.livroandroid.carros.extensions.visible
import kotlinx.android.synthetic.main.fragment_carros.*
import kotlinx.android.synthetic.main.include_progress.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 *
 */
class CarrosFragment : BaseFragment() {

    private lateinit var tipo: TipoCarro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Registra para receber os eventos do bus
        EventBus.getDefault().register(this)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_carros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tipo = arguments?.getSerializable("tipo") as TipoCarro

        // Init Views
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()

        swipeToRefresh.setOnRefreshListener {
            taskCarros(true)
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
                taskCarros(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun taskCarros(refresh: Boolean = false) {

//        if(! isNetworkAvailable()) {
//            handleError(null)
//            return
//        }

        invisible(tError)
        visible(recyclerView)
        if (!swipeToRefresh.isRefreshing) {
            visible(progress)
        }

        doAsync(exceptionHandler = { e -> handleError(e) }) {

            val carros = CarroService.getCarros(tipo, refresh)

            uiThread {
                recyclerView?.adapter = CarroAdapter(carros) { c, longClick ->
                    if(! longClick) {
                        activity?.startActivity<CarroActivity>("carro" to c)
                    } else {
                        toast("LongClick ${c.nome}")
                    }
                }

                // (1)
                /*recyclerView?.adapter = CarroAdapter(carros, { c ->
                    activity?.startActivity<CarroActivity>("carro" to c)
                }, { c ->
                    toast("LongClick ${c.nome}")
                    true
                } )*/

                // (2)
                /*recyclerView?.adapter = CarroAdapter(carros,
                        onClick = { c ->
                            activity?.startActivity<CarroActivity>("carro" to c)
                        },
                        onLongClick = { c ->
                            toast("LongClick ${c.nome}")
                            true
                        })*/

                invisible(progress)
                swipeToRefresh.isRefreshing = false
            }

        }
    }

    private fun handleError(e: Throwable?) {
        runOnUiThread {
            visible(tError)
            invisible(recyclerView, progress)
            swipeToRefresh.isRefreshing = false
            if (e != null) {
                Log.e("carros", "Erro ${e.message}", e)
                toast("Erro ${e.message}")
            }
        }
    }

    @Subscribe
    fun onBusAtualizarLista(event: CarroEvent) {
        // Recebe o evento e atualiza os favoritos
        taskCarros()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Cancela os eventos do bus
        EventBus.getDefault().unregister(this)
    }
}
