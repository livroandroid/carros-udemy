package br.com.livroandroid.carros.domain.retroft

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    companion object {
        fun <T : Any?> getService(url: String, cls: Class<T>): T {
            // Log
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            // Cria a CarrosAPI
            val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()

            return retrofit.create(cls)
        }
    }
}