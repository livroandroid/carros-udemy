package br.com.livroandroid.carros.domain

import android.util.Log
import br.com.livroandroid.carros.domain.dao.DatabaseManager
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
        fun getCarros(tipo: TipoCarro, refresh: Boolean = false): MutableList<Carro> {
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

        // Deleta um carro
        fun delete(carro: Carro): Response {
            val url = "$BASE_URL/${carro.id}"

            val json = HttpHelper.delete(url)

            val response = fromJson<Response>(json)

            if(response.isOk()) {
                // Se removeu do servidor, remove dos favoritos
                val dao = DatabaseManager.getCarroDAO()
                dao.delete(carro)
            }

            return response
        }
    }
}