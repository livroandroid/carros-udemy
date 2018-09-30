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

    private var carro: Carro? = null

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_carro_form)

        btSalvar.setOnClickListener { onClickSalvar() }

        carro = intent.getParcelableExtra("carro") as Carro?
        initViews()
    }

    private fun initViews() {
        if(carro != null) {
            tNome.setText(carro?.nome.toString())
            tDesc.setText(carro?.desc.toString())
        }
    }

    private fun onClickSalvar() {

        val dialog = indeterminateProgressDialog (message = R.string.msg_aguarde, title = R.string.app_name)

        doAsync {
            val c = carro?: Carro()
            if(c.id == 0L) {
                c.tipo = TipoCarro.Classicos.name.toLowerCase()
                c.urlFoto = "http://www.livroandroid.com.br/livro/carros/classicos/Dodge_Challenger.png"
            }
            c.nome = tNome.text.toString()
            c.desc = tDesc.text.toString()

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
