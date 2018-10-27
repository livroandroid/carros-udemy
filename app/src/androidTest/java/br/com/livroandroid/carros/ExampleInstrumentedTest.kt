package br.com.livroandroid.carros

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import br.com.livroandroid.carros.domain.Carro
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("br.com.livroandroid.carros", appContext.packageName)

        val c = Carro()
        c.tipo = TipoCarro.Esportivos.name.toLowerCase()
        c.nome = "RICARDO"
        c.desc = "Carro do Ricardo"

        val response = CarroService.save(c)
        assertEquals("OK",response.status)

        val id = response.id
        assertTrue(id > 0)

        // Deletar
    }
}
