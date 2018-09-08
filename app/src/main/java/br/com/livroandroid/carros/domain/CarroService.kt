package br.com.livroandroid.carros.domain

import android.content.Context
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.extensions.fromJson
import com.google.gson.Gson

class CarroService {
    companion object {
        private const val TAG = "livro"

        // Busca os carros por tipo (clássicos, esportivos ou luxo)
        fun getCarros(context: Context, tipo: TipoCarro): List<Carro> {
            // Este é o arquivo que temos que ler
            val raw = getArquivoRaw(tipo)
            // Abre o arquivo para leitura
            val resources = context.resources
            val inputStream = resources.openRawResource(raw)
            inputStream.bufferedReader().use {
                // Lê o XML e cria a lista de carros
                val json = it.readText()
                return fromJson(json)
            }
        }

        // Retorna o arquivo que temos que ler para o tipo informado
        private fun getArquivoRaw(tipo: TipoCarro) = when (tipo) {
            TipoCarro.Classicos -> R.raw.carros_classicos
            TipoCarro.Esportivos -> R.raw.carros_esportivos
            else -> R.raw.carros_luxo
        }

    }
}