package br.com.livroandroid.carros.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.event.CarroEvent
import br.com.livroandroid.carros.extensions.toast
import br.com.livroandroid.carros.utils.CameraHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_carro_form.*
import kotlinx.android.synthetic.main.activity_carro_form_contents.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class CarroFormActivity : AppCompatActivity() {

    private var carro: Carro? = null
    private val camera = CameraHelper()

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_carro_form)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btSalvar.setOnClickListener { onClickSalvar() }

        carro = intent.getParcelableExtra("carro") as Carro?
        initViews()

        // Inicia a camera
        camera.init(b) { foto ->
            // Se existe arquivo da camera, mostra a foto
           appBarImg.setImageURI(Uri.fromFile(foto))
        }

        appBarImg.setOnClickListener { onClickCamera() }
    }

    private fun initViews() {
        carro?.apply {
            tNome.setText(nome.toString())
            tDesc.setText(desc.toString())
            Picasso.get().load(urlFoto).into(appBarImg)

            // Tipo do carro
            when(tipo) {
                "classicos" -> radioTipo.check(R.id.radioClassico)
                "esportivos" -> radioTipo.check(R.id.radioEsportivo)
                "luxo" -> radioTipo.check(R.id.radioLuxo)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Salva o estado do arquivo caso gire a tela
        camera.onSaveInstanceState(outState)
    }

    // Ao clicar na imagem do AppHeader abre a câmera
    private fun onClickCamera() {
        val ms = System.currentTimeMillis()
        // Nome do arquivo da foto
        val fileName = "foto_carro_$ms.jpg"
        // Abre a câmera
        val cameraIntent = camera.open(this, fileName)
        startActivityForResult(cameraIntent, 0)
    }

    private fun onClickSalvar() {

        if(tNome.text.isEmpty()) {
            tNome.error = getString(R.string.msg_error_form_nome)
            return
        }

        val dialog = indeterminateProgressDialog (message = R.string.msg_aguarde, title = R.string.app_name)

        doAsync {
            val c = carro?: Carro()
            c.nome = tNome.text.toString()
            c.desc = tDesc.text.toString()

            c.tipo = when (radioTipo.checkedRadioButtonId) {
                R.id.radioClassico -> TipoCarro.Classicos.name.toLowerCase()
                R.id.radioEsportivo -> TipoCarro.Esportivos.name.toLowerCase()
                else -> TipoCarro.Luxo.name.toLowerCase()
            }

            // Upload Foto
            val file = camera.file
            if (file != null && file.exists()) {
                val response = CarroService.postFoto(file)
                if (response.isOk()) {
                    // Atualiza a URL da foto no carro
                    c.urlFoto = response.url
                }
            }

            val response = CarroService.save(c)

            uiThread {

                if(response.isOk()) {
                    toast(response.msg)

                    // Dispara o evento
                    EventBus.getDefault().post(CarroEvent(c))

                    finish()

                    dialog.dismiss()

                } else {
                    toast(getString(R.string.msg_erro_salvar))
                }
            }
        }
    }

    // Lê a foto quando a câmera retornar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // Resize da imagem
            val bitmap = camera.getBitmap(1200, 800)
            if (bitmap != null) {
                // Mostra a foto do carro
                appBarImg.setImageBitmap(bitmap)
            }
        }
    }
}
