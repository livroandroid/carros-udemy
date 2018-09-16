package br.com.livroandroid.carros.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.livroandroid.carros.domain.Carro

@Dao
interface CarroDAO {
    @Query("SELECT * FROM carro where id = :id")
    fun getById(id: Long): Carro?

    @Query("SELECT * FROM carro")
    fun findAll(): List<Carro>

    @Insert
    fun insert(carro: Carro)

    @Delete
    fun delete(carro: Carro)
}