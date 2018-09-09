package br.com.livroandroid.carros.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import br.com.livroandroid.carros.CarrosApplication
import br.com.livroandroid.carros.R
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

        fun isCacheOn(): Boolean {
            val context = CarrosApplication.getInstance().applicationContext
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val key = context.getString(R.string.key_fazer_cache)
            return prefs.getBoolean(key, false)
        }
    }
}
