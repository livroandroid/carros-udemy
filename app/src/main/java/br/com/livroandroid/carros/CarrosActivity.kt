package br.com.livroandroid.carros

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.livroandroid.carros.adapter.CarroAdapter
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import kotlinx.android.synthetic.main.activity_carros.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.jetbrains.anko.toast

class CarrosActivity : AppCompatActivity() {

    private lateinit var tipo: TipoCarro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carros)

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Params
        tipo = intent.getSerializableExtra("tipo") as TipoCarro
        actionBar?.title = getString(tipo.string)

        // Init views
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun onResume() {
        super.onResume()

        val carros = CarroService.getCarros(this,tipo)

        recyclerView.adapter = CarroAdapter(carros) { carro ->
            toast("Carro ${carro.nome}")
        }
    }
}
