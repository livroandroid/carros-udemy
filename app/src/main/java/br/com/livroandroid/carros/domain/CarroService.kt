package br.com.livroandroid.carros.domain

import android.content.Context
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.extensions.fromJson
import com.google.gson.Gson
import java.net.URL

class CarroService {
    companion object {
        private const val TAG = "livro"

        private const val BASE_URL = "http://livrowebservices.com.br/rest/carros"

        // Busca os carros por tipo (clássicos, esportivos ou luxo)
        fun getCarros(context: Context, tipo: TipoCarro): List<Carro> {
            val url = "$BASE_URL/tipo/${tipo.name.toLowerCase()}"

            val json = URL(url).readText()

            return fromJson(json)
        }
    }
}