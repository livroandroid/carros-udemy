package br.com.livroandroid.carros.domain

import android.util.Log
import br.com.livroandroid.carros.domain.dao.DatabaseManager

object FavoritosService {
    private const val TAG = "carros"

    private val dao = DatabaseManager.getCarroDAO()
    
    // Retorna todos os carros favoritados
    fun getCarros(): MutableList<Carro> {
        Log.d(TAG, "> FavoritosService.getCarros()")
        return dao.findAll().toMutableList()
    }

    // Verifica se um carro está favoritado
    fun isFavorito(carro: Carro) : Boolean {
        return dao.getById(carro.id) != null
    }

    // Salva o carro ou deleta
    fun favoritar(carro: Carro): Boolean {
        val favorito = isFavorito(carro)
        if(favorito) {
            // Remove dos favoritos
            dao.delete(carro)
            return false
        }
        // Adiciona nos favoritos
        dao.insert(carro)
        return true
    }

    fun desfavoritar(carros: List<Carro>) {
        // Remove os carros do banco de dados do favoritos
        for (c in carros) {
            dao.delete(c)
        }
    }
}