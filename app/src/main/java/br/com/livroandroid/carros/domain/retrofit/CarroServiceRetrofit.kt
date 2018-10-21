package br.com.livroandroid.carros.domain.retrofit

import android.util.Base64
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.rest.Response
import retrofit2.Call
import java.io.File

/**
 * Implementação com Retrofit
 */
class CarroServiceRetrofit {

    companion object {
        private const val BASE_URL = "http://livrowebservices.com.br/rest/carros/"

        private var api: CarrosAPI

        init {
            api = RetrofitFactory.getService(BASE_URL, CarrosAPI::class.java)
        }

        fun getCarrosAPI(): CarrosAPI {
            return api
        }

        fun getCarrosAsync(tipo: TipoCarro): Call<MutableList<Carro>> {
            return api.getCarros(tipo.name)
        }

        // Busca os carros por tipo (clássicos, esportivos ou luxo)
        fun getCarros(tipo: TipoCarro): MutableList<Carro>? {
            val call = api.getCarros(tipo.name)
            return call.execute().body()
        }

        // Salva um carro
        fun save(carro: Carro): Response? {
            val call = api.save(carro)
            return call.execute().body()
        }

        // Deleta um carro
        fun delete(carro: Carro): Response? {
            val call = api.delete(carro.id)
            return call.execute().body()
        }

        // Upload de Foto
        fun postFoto(file: File): Response? {
            // Converte para Base64
            val bytes = file.readBytes()
            val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
            val call = api.postFoto(file.name, base64)
            return call.execute().body()
        }
    }
}