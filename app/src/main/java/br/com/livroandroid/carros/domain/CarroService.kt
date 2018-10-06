package br.com.livroandroid.carros.domain

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import br.com.livroandroid.carros.domain.dao.DatabaseManager
import br.com.livroandroid.carros.domain.rest.Response
import br.com.livroandroid.carros.extensions.fromJson
import br.com.livroandroid.carros.extensions.toJson
import br.com.livroandroid.carros.utils.HttpHelper
import br.com.livroandroid.carros.utils.Prefs
import java.io.File
import java.net.URL
import java.util.ArrayList

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

        fun deleteCarros(selectedCarros: List<Carro>): Response {
            lateinit var response: Response

            // Deleta os carros 1 a 1
            for (c in selectedCarros) {
                response = delete(c)

                if(!response.isOk()) {
                    return response
                }
            }
            return response
        }

        fun downloadFotos(context: Context, carros: List<Carro>): ArrayList<Uri> {
            // Lista de uris
            val fotoUris = ArrayList<Uri>()
            for (c in carros) {
                val url = c.urlFoto
                val fileName = url.substring(url.lastIndexOf("/"))

                // Cria o arquivo no SD card
                val dir = context.getExternalFilesDir(null)
                val file = File(dir, fileName)

                // Faz download para arquivo
                val bytes = URL(url).readBytes()
                val out = file.outputStream()
                file.writeBytes(bytes)
                out.close()

                Log.d(TAG,"Foto salva ${file.absolutePath}")

                // Cria a Uri para compartilhar a foto
                val authority = context.applicationContext.packageName + ".provider"
                val uri = FileProvider.getUriForFile(context, authority, file)
                fotoUris.add(uri)
            }

            return fotoUris
        }

        fun postFoto(file: File): Response {
            val url = "$BASE_URL/postFotoBase64"

            // Converte para Base64
            val bytes = file.readBytes()
            val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

            val params = mapOf("fileName" to file.name, "base64" to base64)

            val json = HttpHelper.postForm(url, params)

            return fromJson(json)
        }
    }
}