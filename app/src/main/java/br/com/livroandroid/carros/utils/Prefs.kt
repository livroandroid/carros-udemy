package br.com.livroandroid.carros.utils

import android.content.SharedPreferences
import br.com.livroandroid.carros.CarrosApplication

class Prefs {

    companion object {

        private const val PREF_ID = "carros"

        private fun prefs(): SharedPreferences {
            val context = CarrosApplication.getInstance().applicationContext
            return context.getSharedPreferences(PREF_ID, 0)
        }

        fun setInt(flag: String, valor: Int) = prefs().edit().putInt(flag, valor).apply()

        fun getInt(flag: String) = prefs().getInt(flag, 0)

        var lastTabIdx: Int
            get() = getInt("tabIdx")
            set(value) = setInt("tabIdx", value)
    }
}
