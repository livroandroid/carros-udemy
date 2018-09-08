package br.com.livroandroid.carros.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.fragments.CarrosFragment
import kotlinx.android.synthetic.main.include_toolbar.*

class CarrosActivity : BaseActivity() {

    private lateinit var tipo: TipoCarro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carros)

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        tipo = intent.getSerializableExtra("tipo") as TipoCarro
        actionBar?.title = getString(tipo.string)

        if(savedInstanceState == null) {
            val frag = CarrosFragment()
            frag.arguments = intent.extras

            val t = supportFragmentManager.beginTransaction()
            t.add(R.id.fragment, frag)
            t.commit()
        }
    }
}
