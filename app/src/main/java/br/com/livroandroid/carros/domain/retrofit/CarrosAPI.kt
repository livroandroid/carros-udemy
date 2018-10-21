package br.com.livroandroid.carros.domain.retrofit

import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.rest.Response
import retrofit2.Call
import retrofit2.http.*

interface CarrosAPI {

    // http://livrowebservices.com.br/rest/carros/

    @GET("tipo/{tipo}")
    fun getCarros(@Path("tipo") tipo: String): Call<MutableList<Carro>>

    @POST("./")
    fun save(@Body carro: Carro): Call<Response>

    @DELETE("{id}")
    fun delete(@Path("id") id: Long): Call<Response>

    @FormUrlEncoded
    @POST("postFotoBase64")
    fun postFoto(@Field("fileName") fileName:String, @Field("base64") base64:String): Call<Response>
}