package br.com.livroandroid.carros.domain

import br.com.livroandroid.carros.domain.dao.DatabaseManager

object FavoritosService {
    private val dao = DatabaseManager.getCarroDAO()
    
    // Retorna todos os carros favoritados
    fun getCarros(): List<Carro> {
        return dao.findAll()
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
}