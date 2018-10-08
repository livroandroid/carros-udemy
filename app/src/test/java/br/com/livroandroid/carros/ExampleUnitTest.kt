package br.com.livroandroid.carros

import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val carros = CarroService.getCarros(TipoCarro.Classicos, true)
        println(carros)
    }
}
