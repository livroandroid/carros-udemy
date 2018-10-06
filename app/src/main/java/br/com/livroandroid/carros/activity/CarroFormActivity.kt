package br.com.livroandroid.carros.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.event.CarroEvent
import br.com.livroandroid.carros.extensions.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_carro_form.*
import kotlinx.android.synthetic.main.activity_carro_form_contents.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class CarroFormActivity : AppCompatActivity() {

    private var carro: Carro? = null

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_carro_form)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btSalvar.setOnClickListener { onClickSalvar() }

        carro = intent.getParcelableExtra("carro") as Carro?
        initViews()
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

    private fun onClickSalvar() {

        if(tNome.text.isEmpty()) {
            tNome.error = getString(R.string.msg_error_form_nome)
            return
        }

        val dialog = indeterminateProgressDialog (message = R.string.msg_aguarde, title = R.string.app_name)

        doAsync {
            val c = carro?: Carro()
            if(c.id == 0L) {
                c.urlFoto = "http://www.livroandroid.com.br/livro/carros/classicos/Dodge_Challenger.png"
            }
            c.nome = tNome.text.toString()
            c.desc = tDesc.text.toString()

            c.tipo = when (radioTipo.checkedRadioButtonId) {
                R.id.radioClassico -> TipoCarro.Classicos.name.toLowerCase()
                R.id.radioEsportivo -> TipoCarro.Esportivos.name.toLowerCase()
                else -> TipoCarro.Luxo.name.toLowerCase()
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
}
