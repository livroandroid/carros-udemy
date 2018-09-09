package br.com.livroandroid.carros.activity

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import br.com.livroandroid.carros.R
import androidx.preference.PreferenceFragmentCompat
import br.com.livroandroid.carros.extensions.toast
import br.com.livroandroid.carros.utils.Prefs

class PreferencesActivity : BaseActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_preferences)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment, PrefsFragment())
                    .commit()
        }

        toast("Cache On ${Prefs.isCacheOn()}")
    }

    class PrefsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences)
        }
    }
}
