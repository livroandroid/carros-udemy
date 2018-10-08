package br.com.livroandroid.carros.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.VideoView
import androidx.core.content.ContextCompat
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.FavoritosService
import br.com.livroandroid.carros.domain.event.CarroEvent
import br.com.livroandroid.carros.extensions.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_carro.*
import kotlinx.android.synthetic.main.activity_carro_contents.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.*

class CarroActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var carro: Carro

    private lateinit var mapFragment: SupportMapFragment
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carro)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        carro = intent.getParcelableExtra("carro") as Carro

        with(carro) {
            //tNome.text = nome
            supportActionBar?.title = nome

            tDesc.text = desc

            Picasso.get().load(urlFoto).into(img)
            Picasso.get().load(urlFoto).into(appBarImg)
        }

        fab.setOnClickListener { onClickFavoritar() }

        // Toca o Vídeo
        imgPlayVideo.setOnClickListener { onClickPlayVideo() }

        // Mapa
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map

        // Configura o tipo do mapa
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Cria o objeto lat/lng com a coordenada da fábrica
        val location = LatLng(carro.latitude.toDouble(), carro.longitude.toDouble())
        // Posiciona o mapa na coordenada da fábrica (zoom = 13)
        val update = CameraUpdateFactory.newLatLngZoom(location, 13f)
        map?.moveCamera(update)
        // Marcador no local da fábrica
        map?.addMarker(MarkerOptions()
                .title(carro.nome)
                .snippet(carro.desc)
                .position(location))
    }

    private fun onClickPlayVideo() {
        // 1) Player Nativo
        val url = carro.urlVideo
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(url), "video/*")
        startActivity(intent)

        // 2) VideoView
//        startActivity<VideoActivity>("carro" to carro)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_carro, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        } R.id.action_editar -> {
            startActivity<CarroFormActivity>("carro" to carro)
            true
        } R.id.action_share -> {
            taskShare()
            true
        } R.id.action_deletar -> {
            alert(R.string.msg_confirma_excluir_carro, R.string.app_name) {
                positiveButton(R.string.sim) {
                    // Confirmou o excluir
                    taskDeletar()
                }
                negativeButton(R.string.nao) {
                    // Não confirmou...
                }
            }.show()

            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun taskShare() {

        val dialog = indeterminateProgressDialog (message = R.string.msg_aguarde, title = R.string.app_name)

        doAsync {
            val carros = listOf(carro)

            val fotoUris = CarroService.downloadFotos(context, carros)

            uiThread {

                // Fecha o dialog
                dialog.dismiss()

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

    private fun taskDeletar() {

        val dialog = indeterminateProgressDialog (message = R.string.msg_aguarde, title = R.string.app_name)

        doAsync {

            val response = CarroService.delete(carro)

            uiThread {

                toast(response.msg)

                // Dispara o evento
                EventBus.getDefault().post(CarroEvent(carro))

                finish()

                dialog.dismiss()
            }
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
                EventBus.getDefault().post(CarroEvent(carro))
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
