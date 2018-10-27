package br.com.livroandroid.carros.domain.retroft

import android.util.Base64
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.domain.rest.Response
import io.reactivex.Observable
import retrofit2.Call
import java.io.File

/**
 * Implementação com Retrofit
 */
object CarroServiceRetrofit {
    private const val BASE_URL = "http://livrowebservices.com.br/rest/carros/"
    private var service: CarrosAPI

    init {
        service = RetrofitFactory.getService(BASE_URL, CarrosAPI::class.java)
    }

    fun getCarrosAsync(tipo: TipoCarro): Call<MutableList<Carro>> {
        return service.getCarros(tipo.name)
    }

    fun getCarrosRx(tipo: TipoCarro): Observable<MutableList<Carro>> {
        return service.getCarrosRx(tipo.name)
    }

    // Busca os carros por tipo (clássicos, esportivos ou luxo)
    fun getCarros(tipo: TipoCarro): MutableList<Carro>? {
        val call = service.getCarros(tipo.name)
        return call.execute().body()
    }

    // Salva um carro
    fun save(carro: Carro): Response? {
        val call = service.save(carro)
        return call.execute().body()
    }

    // Deleta um carro
    fun delete(carro: Carro): Response? {
        val call = service.delete(carro.id)
        return call.execute().body()
    }

    // Upload de Foto
    fun postFoto(file: File): Response? {
        // Converte para Base64
        val bytes = file.readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        val call = service.postFoto(file.name, base64)
        return call.execute().body()
    }
}