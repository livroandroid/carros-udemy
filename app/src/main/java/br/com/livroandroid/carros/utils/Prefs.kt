package br.com.livroandroid.carros.utils

import android.content.SharedPreferences
import br.com.livroandroid.carros.CarrosApplication
import java.lang.reflect.Array.setInt

class Prefs {

    companion object {

        private const val PREF_ID = "carros"

        private fun prefs(): SharedPreferences {
            val context = CarrosApplication.getInstance().applicationContext
            return context.getSharedPreferences(PREF_ID, 0)
        }

        fun putInt(key: String, valor: Int) = prefs().edit().putInt(key, valor).apply()

        fun getInt(key: String) = prefs().getInt(key, 0)

        fun putString(key: String, valor: String) = prefs().edit().putString(key, valor).apply()

        fun getString(key: String) = prefs().getString(key, "")!!

        var lastTabIdx: Int
            get() = getInt("tabIdx")
            set(value) = putInt("tabIdx", value)
    }
}
