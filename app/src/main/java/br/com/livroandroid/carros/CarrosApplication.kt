package br.com.livroandroid.carros

import android.app.Application
import android.content.Context
import android.util.Log
import java.lang.IllegalStateException

/**
 * MultiDexApplication:
 *
 * https://developer.android.com/studio/build/multidex.html?hl=pt-br
 */
class CarrosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "CarrosApplication.onCreate()")
        appInstance = this
    }

    companion object {
        private const val TAG = "CarrosApplication"

        // Singleton da classe Application
        private var appInstance: CarrosApplication? = null

        fun getInstance(): CarrosApplication {
            if (appInstance == null) {
                throw IllegalStateException("Configure a classe de Application no AndroidManifest.xml")
            }
            return appInstance!!
        }

        fun getContext(): Context {
            return getInstance().applicationContext
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG, "CarrosApplication.onTerminate()")
    }
}
