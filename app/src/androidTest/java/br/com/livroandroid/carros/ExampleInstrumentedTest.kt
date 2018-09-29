package br.com.livroandroid.carros

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import br.com.livroandroid.carros.domain.CarroService
import br.com.livroandroid.carros.domain.TipoCarro

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

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

        val carros = CarroService.getCarros(TipoCarro.Classicos, true)
        println(carros)
        assertEquals("Carros deveria ter 10",10, carros.size)
    }
}
