package br.com.livroandroid.carros.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.livroandroid.carros.activity.CarroActivity

import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.FavoritosService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.extensions.*
import kotlinx.android.synthetic.main.adapter_carro.*

import kotlinx.android.synthetic.main.fragment_carros.*
import kotlinx.android.synthetic.main.include_progress.*
import org.jetbrains.anko.custom.onUiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoritosFragment : BaseFragment() {

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
            taskCarros(true)
        }
        swipeToRefresh.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)
    }

    override fun onResume() {
        super.onResume()

        taskCarros()
    }

    private fun taskCarros(refresh: Boolean = false) {

        if(!swipeToRefresh.isRefreshing) {
            visible(progress)
        }

        doAsync {

            val carros = FavoritosService.getCarros()

            uiThread {
                recyclerView.adapter = CarroAdapter(carros) { c ->
                    activity?.startActivity<CarroActivity>("carro" to c)
                }

                invisible(progress)
                swipeToRefresh.isRefreshing = false
            }

        }
    }
}
