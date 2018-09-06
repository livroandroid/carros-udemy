package br.com.livroandroid.carros.domain
import android.content.Context

class CarroService {
    companion object {
        // Busca os carros por tipo
        fun getCarros(context: Context, tipo: TipoCarro): List<Carro> {
            val tipoString = context.getString(tipo.string)
            val carros = mutableListOf<Carro>()
            for (i in 1..100) {
                val c = Carro()
                c.nome = "Carro $tipoString: $i"
                c.desc = "Desc $i"
                c.urlFoto = "http://www.livroandroid.com.br/livro/carros/esportivos/Ferrari_FF.png"
                carros.add(c)
            }
            return carros
        }
    }
}
