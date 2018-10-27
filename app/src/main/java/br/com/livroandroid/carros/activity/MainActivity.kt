package br.com.livroandroid.carros.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import br.com.livroandroid.carros.BuildConfig
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.adapter.TabsAdapter
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.utils.Prefs
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mFirebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(this)
    }

    private val mFirebaseRemoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        initNavDrawer()
        initViewPagerTabs()

        fab.setOnClickListener {
            startActivity<CarroFormActivity>()
        }

        // GA
        val bundle = Bundle()
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)

        // Remote Config
        initFirebaseConfig()
    }

    private fun initFirebaseConfig() {

        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        mFirebaseRemoteConfig.setConfigSettings(configSettings)

        //fetchFirebaseConfig()
    }

    override fun onResume() {
        super.onResume()

        fetchFirebaseConfig()
    }

    @SuppressLint("SetTextI18n")
    private fun fetchFirebaseConfig() {
        mFirebaseAnalytics.logEvent("fetchFirebaseConfig", Bundle())

        var cacheExpiration: Long = 3600

        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)!!
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        toast("Fetch Succeeded")

                        // After config data is successfully fetched, it must be activated before newly fetched
                        // values are returned.
                        mFirebaseRemoteConfig.activateFetched()

                        val cache = FirebaseRemoteConfig.getInstance().getBoolean("cache_lista_carros")
                        toast("Cache $cache")
                    } else {
                        toast("Fetch Failed")
                    }
                }
    }

    private fun initNavDrawer() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initViewPagerTabs() {
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = TabsAdapter(this, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        // Cor branca no texto (a cor de fundo é definido no layout)
        val cor = ContextCompat.getColor(this, R.color.white)
        tabLayout.setTabTextColors(cor, cor)

        // Salva e Recupera a última Tab acessada.
        val tabIdx = Prefs.lastTabIdx
        viewPager.currentItem = tabIdx
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p: Int) { }
            override fun onPageScrolled(p: Int, p1: Float, p2: Int) { }
            override fun onPageSelected(page: Int) {
                // Salva o índice da página/tab selecionada
                Prefs.lastTabIdx = page
            }
        })
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity<PreferencesActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_item_carros_todos -> {
                toast("Carros")
            }
            R.id.nav_item_carros_classicos -> {
                /*val intent = Intent(this,CarrosActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("tipo",TipoCarro.Classicos)
                intent.putExtras(bundle)
                startActivity(intent)*/

                startActivity<CarrosActivity>("tipo" to TipoCarro.Classicos)
            }
            R.id.nav_item_carros_esportivos -> {
                startActivity<CarrosActivity>("tipo" to TipoCarro.Esportivos)

            }
            R.id.nav_item_carros_luxo -> {
                startActivity<CarrosActivity>("tipo" to TipoCarro.Luxo)
            }
            R.id.nav_item_site_livro -> {
                startActivity<WebViewActivity>()
            }
            R.id.nav_item_settings -> {
                startActivity<PreferencesActivity>()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
