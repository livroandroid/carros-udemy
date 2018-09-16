package br.com.livroandroid.carros.domain.dao

import androidx.room.Room
import br.com.livroandroid.carros.CarrosApplication

object DatabaseManager {
    // Singleton do Room: banco de dados
    private var dbInstance: CarrosDatabase

    init {
        val appContext = CarrosApplication.getContext()

        // Configura o Room
        dbInstance = Room.databaseBuilder(
                appContext,
                CarrosDatabase::class.java,
                "carros.sqlite")
                .build()
    }

    fun getCarroDAO(): CarroDAO {
        return dbInstance.carroDAO()
    }
}