package br.com.livroandroid.carros.fragments


import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_carros.*
import org.jetbrains.anko.startActivity

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
    }

    override fun onResume() {
        super.onResume()

        val t = Thread {
            val carros = CarroService.getCarros(context, tipo)

            activity?.runOnUiThread {
                recyclerView.adapter = CarroAdapter(carros) { c ->
                    activity?.startActivity<CarroActivity>("carro" to c)
                }
            }
        }
        t.start()
    }
}
