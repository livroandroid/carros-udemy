package br.com.livroandroid.carros.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.event.CarroEvent
import br.com.livroandroid.carros.extensions.toast
import kotlinx.android.synthetic.main.activity_carro_form.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class CarroFormActivity : AppCompatActivity() {

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_carro_form)

        btSalvar.setOnClickListener { onClickSalvar() }
    }

    private fun onClickSalvar() {

        val dialog = indeterminateProgressDialog (message = R.string.msg_aguarde, title = R.string.app_name)

        doAsync {
            val c = Carro()
            c.tipo = TipoCarro.Classicos.name.toLowerCase()
            c.nome = tNome.text.toString()
            c.desc = tDesc.text.toString()
            c.urlFoto = "http://www.livroandroid.com.br/livro/carros/classicos/Dodge_Challenger.png"

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
}
