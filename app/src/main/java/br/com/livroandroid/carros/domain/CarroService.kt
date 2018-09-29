package br.com.livroandroid.carros.domain

import android.util.Log
import br.com.livroandroid.carros.domain.rest.Response
import br.com.livroandroid.carros.extensions.fromJson
import br.com.livroandroid.carros.extensions.toJson
import br.com.livroandroid.carros.utils.HttpHelper
import br.com.livroandroid.carros.utils.Prefs

class CarroService {
    companion object {
        private const val TAG = "carros"

        private const val BASE_URL = "http://livrowebservices.com.br/rest/carros"

        // Busca os carros por tipo (clássicos, esportivos ou luxo)
        fun getCarros(tipo: TipoCarro, refresh: Boolean = false): List<Carro> {
            val url = "$BASE_URL/tipo/${tipo.name.toLowerCase()}"

            Log.d(TAG, "> CarroService.getCarros() -> ${tipo.name}" )

            val cache = Prefs.isCacheOn()

            // Tenta buscar do banco
            if(cache && !refresh) {
                val json = Prefs.getString(url)
                if(! json.isEmpty()) {
                    return fromJson(json)
                }
            }

            // Acessa a internet
            val json = HttpHelper.get(url)

            // Salva json no banco
            Prefs.putString(url,json)

            return fromJson(json)
        }

        fun save(carro: Carro): Response {
            val json = carro.toJson()

            val response = HttpHelper.post(BASE_URL, json)

            Log.d(TAG,response)

            return fromJson(response)
        }
    }
}