package br.com.livroandroid.carros.activity

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.FavoritosService
import br.com.livroandroid.carros.domain.event.FavoritoEvent
import br.com.livroandroid.carros.extensions.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_carro.*
import kotlinx.android.synthetic.main.activity_carro_contents.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CarroActivity : BaseActivity {

    private lateinit var carro: Carro

    constructor() : super()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carro)

        setSupportActionBar(toolbar)

        carro = intent.getParcelableExtra("carro") as Carro

        with(carro) {
            //tNome.text = nome
            tDesc.text = desc
            supportActionBar?.title = nome
            Picasso.get().load(urlFoto).into(img)
            Picasso.get().load(urlFoto).into(appBarImg)
        }

        fab.setOnClickListener { onClickFavoritar() }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_carro, menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        } R.id.action_favoritar -> {
            onClickFavoritar()
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            val favoritado = FavoritosService.isFavorito(carro)

            uiThread { setFavoriteColor(favoritado) }
        }
    }

    private fun onClickFavoritar() {
        doAsync {
            val favoritado = FavoritosService.favoritar(carro)

            uiThread {
                toast(if (favoritado) R.string.msg_carro_favoritado else R.string.msg_carro_desfavoritado)

                // Atualiza cor do botão FAB
                setFavoriteColor(favoritado)

                // Dispara o evento
                EventBus.getDefault().post(FavoritoEvent(carro))
            }
        }
    }

    // Desenha a cor do FAB conforme está favoritado ou não.
    private fun setFavoriteColor(favorito: Boolean) {
        // Troca a cor conforme o status do favoritos
        val fundo = ContextCompat.getColor(this, if (favorito) R.color.favorito_background_on else R.color.favorito_background_off)
        val cor = ContextCompat.getColor(this, if (favorito) R.color.favorito_tint_on else R.color.favorito_tint_off)
        fab.backgroundTintList = ColorStateList(arrayOf(intArrayOf(0)), intArrayOf(fundo))
        fab.setColorFilter(cor)
    }
}
