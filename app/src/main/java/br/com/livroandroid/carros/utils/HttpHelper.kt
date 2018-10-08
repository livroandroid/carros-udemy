package br.com.livroandroid.carros.utils

import android.util.Log
import okhttp3.*
import java.io.IOException

class HttpHelper {

    companion object {
        private const val TAG = "http"
        private const val LOG_ON = true

        private val JSON = MediaType.parse("application/json; charset=utf-8")
        private var client = OkHttpClient()

        // GET
        fun get(url: String): String {
            log("HttpHelper.get: $url")
            val request = Request.Builder().url(url).get().build()
            return getResponseString(request)
        }

        // POST com JSON
        fun post(url: String, json: String): String {
            log("HttpHelper.post: $url > $json")
            val body = RequestBody.create(JSON, json)
            val request = Request.Builder().url(url).post(body).build()
            return getResponseString(request)
        }

        // POST com parâmetros (form-urlencoded)
        fun postForm(url: String, params: Map<String, String>): String {
            log("HttpHelper.postForm: $url > $params")
            // Adiciona os parâmetros chave=valor na request POST
            val builder = FormBody.Builder()
            for ((key, value) in params) {
                builder.add(key, value)
            }
            val body = builder.build()
            // Faz a request
            val request = Request.Builder().url(url).post(body).build()
            return getResponseString(request)
        }

        // DELETE
        fun delete(url: String): String {
            log("HttpHelper.delete: $url")
            val request = Request.Builder().url(url).delete().build()
            return getResponseString(request)
        }

        // Lê a resposta do servidor no formato String
        private fun getResponseString(request: Request): String {
            val response = client.newCall(request).execute()
            val responseBody = response.body()
            if (responseBody != null) {
                val json = responseBody.string()
                log("	 << : $json")
                return json
            }
            throw IOException("Erro ao fazer a requisição")
        }

        private fun log(s: String) {
            if (LOG_ON) {
                Log.d(TAG, s)
            }
        }
    }

}
