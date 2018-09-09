package br.com.livroandroid.carros.fragments


import android.os.Bundle
import android.os.Handler
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
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.extensions.toast

import kotlinx.android.synthetic.main.fragment_carros.*
import kotlinx.android.synthetic.main.include_progress.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * A simple [Fragment] subclass.
 *
 */
class CarrosFragment : BaseFragment() {

    private lateinit var tipo: TipoCarro

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
            taskCarros()
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

    private fun taskCarros() {

        tError.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        if(!swipeToRefresh.isRefreshing) {
            progress.visibility = View.VISIBLE
        }

        Thread {

            try {
                val carros = CarroService.getCarros(context, tipo)

                activity?.runOnUiThread {
                    recyclerView.adapter = CarroAdapter(carros) { c ->
                        activity?.startActivity<CarroActivity>("carro" to c)
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    tError.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                    Log.e("carros","Erro ${e.message}", e)
                }
            } finally {
                activity?.runOnUiThread {
                    progress.visibility = View.INVISIBLE
                    swipeToRefresh.isRefreshing = false
                }
            }

        }.start()


    }
}
