package br.com.livroandroid.carros.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.MediaController
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : BaseActivity() {

    private val carro: Carro by lazy {
        intent.getParcelableExtra("carro") as Carro
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video)

        val url = carro.urlVideo

        Log.d("carros", "Video $url")

        videoView.setVideoURI(Uri.parse(url))

        val mc = MediaController(this)
        mc.setAnchorView(videoView)
        videoView.setMediaController(mc)

        videoView.start()
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        android.R.id.home -> {
            finish()
//            val upIntent = NavUtils.getParentActivityIntent(this)
//            upIntent?.putExtras(intent.extras)
//            NavUtils.navigateUpTo(this, upIntent!!)
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
