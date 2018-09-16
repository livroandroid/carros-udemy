package br.com.livroandroid.carros.domain

import android.content.Context
import android.util.Log
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.extensions.fromJson
import br.com.livroandroid.carros.utils.Prefs
import com.google.gson.Gson
import java.net.URL

class CarroService {
    companion object {

        private const val BASE_URL = "http://livrowebservices.com.br/rest/carros"

        // Busca os carros por tipo (clássicos, esportivos ou luxo)
        fun getCarros(context: Context, tipo: TipoCarro, refresh: Boolean = false): List<Carro> {
            val url = "$BASE_URL/tipo/${tipo.name.toLowerCase()}"

            val cache = Prefs.isCacheOn()

            // Tenta buscar do banco
            if(cache && !refresh) {
                val json = Prefs.getString(url)
                if(! json.isEmpty()) {
                    return fromJson(json)
                }
            }

            // Acessa a internet
            val json = URL(url).readText()

            // Salva json no banco
            Prefs.putString(url,json)

            return fromJson(json)
        }
    }
}