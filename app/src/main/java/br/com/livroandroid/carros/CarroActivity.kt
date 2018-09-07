package br.com.livroandroid.carros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import br.com.livroandroid.carros.domain.Carro
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_carro.*

class CarroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carro)

        val c = intent.getParcelableExtra("carro") as Carro

        textView.text = c.nome
        supportActionBar?.title = c.nome

        Picasso.get().load(c.urlFoto).into(img)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
