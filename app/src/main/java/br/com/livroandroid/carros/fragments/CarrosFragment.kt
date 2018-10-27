package br.com.livroandroid.carros.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.CarroActivity
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.event.CarroEvent
import br.com.livroandroid.carros.domain.retroft.CarroServiceRetrofit
import br.com.livroandroid.carros.domain.retroft.CarrosAPI
import br.com.livroandroid.carros.extensions.invisible
import br.com.livroandroid.carros.extensions.runOnUiThread
import br.com.livroandroid.carros.extensions.toast
import br.com.livroandroid.carros.extensions.visible
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_carros.*
import kotlinx.android.synthetic.main.include_progress.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class CarrosFragment : BaseFragment() {

    private var actionMode: ActionMode? = null
    private lateinit var tipo: TipoCarro

    private lateinit var carros: MutableList<Carro>

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

    @SuppressLint("CheckResult")
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

//        Observable.fromCallable { CarroServiceRetrofit.getCarros(tipo) }

        CarroServiceRetrofit.getCarrosRx(tipo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { carros ->
                if(carros != null) {
                    invisible(progress)
                    swipeToRefresh.isRefreshing = false

                    //toast("Rx OK ${carros.size}")

                    recyclerView?.adapter = CarroAdapter(context,carros) { c, longClick ->
                        if (longClick) {
                            onLongClickCarro(c)
                        } else {
                            onClickCarro(c)
                        }
                    }
                }
            }

        /*// Retrofit
        val call = CarroServiceRetrofit.getCarrosAsync(tipo)
        call.enqueue(object:Callback<List<Carro>>{
            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {
                toast("Erro ${t.message}")
            }

            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {
                val carrosList = response.body()?.toMutableList()

                if(carrosList != null) {

                    toast("Retrofit OK ${carrosList.size}")

                    carros = carrosList

                    invisible(progress)
                    swipeToRefresh.isRefreshing = false

                    recyclerView?.adapter = CarroAdapter(context,carros) { c, longClick ->
                        if (longClick) {
                            onLongClickCarro(c)
                        } else {
                            onClickCarro(c)
                        }
                    }
                }
            }
        })*/

        /*
        // ANKO - OKHttp

        doAsync(exceptionHandler = { e -> handleError(e) }) {

            val carrosList = CarroServiceRetrofit.getCarros(tipo, refresh)

            if(carrosList != null) {
                carros = carrosList

                uiThread {
                    invisible(progress)
                    swipeToRefresh.isRefreshing = false

                    recyclerView?.adapter = CarroAdapter(context,carros) { c, longClick ->
                        if (longClick) {
                            onLongClickCarro(c)
                        } else {
                            onClickCarro(c)
                        }
                    }
                }
            }
        }*/
    }

    private fun onLongClickCarro(c: Carro) {
        if (actionMode != null) {
            return
        }
        // Liga a action bar de contexto (CAB)
        val appCompatActivity = activity as AppCompatActivity
        actionMode = appCompatActivity.startSupportActionMode(getActionModeCallback())
        // Seleciona o carro
        c.selected = true
        // Atualiza o RecyclerView
        recyclerView.adapter?.notifyDataSetChanged()
        // Atualiza o título para mostrar a quantidade de carros selecionados
        updateActionModeTitle()
    }

    private fun getActionModeCallback(): androidx.appcompat.view.ActionMode.Callback {
        return object : androidx.appcompat.view.ActionMode.Callback {
            override fun onCreateActionMode(mode: androidx.appcompat.view.ActionMode?, menu: Menu?): Boolean {
                // Infla o menu específico da CAB
                val inflater = activity?.menuInflater
                inflater?.inflate(R.menu.menu_fragment_carros_cab, menu)
                return true;
            }

            override fun onActionItemClicked(mode: androidx.appcompat.view.ActionMode?, item: MenuItem?): Boolean {
                if (item?.itemId == R.id.action_remove) {
                    taskDeletarCarros()

                } else if (item?.itemId == R.id.action_share) {
                    taskShare()
                }
                // Encerra o action mode
                mode?.finish()
                return true
            }


            override fun onPrepareActionMode(mode: androidx.appcompat.view.ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: androidx.appcompat.view.ActionMode?) {
                // Limpa o estado
                actionMode = null
                // Configura todos os carros para não selecionados
                for (c in carros) {
                    c.selected = false
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }

        }
    }

    private fun taskShare() {

        val dialog = activity?.indeterminateProgressDialog (message = R.string.msg_aguarde, title = R.string.app_name)

        doAsync {
            val carros = getSelectedCarros()

            val fotoUris = CarroService.downloadFotos(context, carros)

            uiThread {

                // Fecha o dialog
                dialog?.dismiss()

                // Cria a intent com a foto dos carros
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND_MULTIPLE
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fotoUris)
                shareIntent.type = "image/*"
                startActivity(Intent.createChooser(shareIntent, getString(R.string.compartilhar_carro)))
            }
        }
    }

    private fun onClickCarro(c: Carro) {
        // Click simples
        if (actionMode == null) {
            activity?.startActivity<CarroActivity>("carro" to c)
        } else {
            // Se a CAB está ativada
            // Seleciona o carro
            c.selected = !c.selected
            // Atualiza o título com a quantidade de carros selecionados
            updateActionModeTitle()
            // Redesenha a lista
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    // Atualiza o título da action bar (CAB)
    private fun updateActionModeTitle() {
        if (actionMode != null) {
            actionMode?.title = "Selecione os carros."
            actionMode?.subtitle = null
            val selectedCarros = getSelectedCarros()
            if (selectedCarros.size == 1) {
                actionMode?.subtitle = "1 carro selecionado"
            } else if (selectedCarros.size > 1) {
                actionMode?.subtitle = "${selectedCarros.size} carros selecionados"
            }
            //updateShareIntent(selectedCarros)
        }
    }

    // Retorna a lista de carros selecionados
    private fun getSelectedCarros(): List<Carro> {
        val list = ArrayList<Carro>()
        for (c in carros) {
            if (c.selected) {
                list.add(c)
            }
        }
        return list
    }

    private fun taskDeletarCarros() {
        val selectedCarros = getSelectedCarros()

        val dialog = activity?.indeterminateProgressDialog(message = R.string.msg_aguarde, title = R.string.app_name)

        doAsync {
            val response = CarroService.deleteCarros(selectedCarros)

            toast(response.msg)

            if (response.isOk()) {

                for (c in selectedCarros) {
                    val idx = deletefromList(carros, c)
                    if (idx > -1) {
                        uiThread {
                            recyclerView?.adapter?.notifyItemRemoved(idx)
                        }
                    }
                }
            }

            uiThread {
                dialog?.dismiss()
            }
        }
    }

    private fun deletefromList(carros: MutableList<Carro>, carro: Carro): Int {
        for ((i, c) in carros.withIndex()) {
            if (c.id == carro.id) {
                carros.removeAt(i)
                return i
            }
        }
        return -1
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
        taskCarros(true)

    }

    override fun onDestroy() {
        super.onDestroy()

        // Cancela os eventos do bus
        EventBus.getDefault().unregister(this)
    }
}
