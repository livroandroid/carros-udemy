package br.com.livroandroid.carros.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.adapter.TabsAdapter
import br.com.livroandroid.carros.domain.TipoCarro
import br.com.livroandroid.carros.utils.Prefs
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        initNavDrawer()
        initViewPagerTabs()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Adicionar um carro!", Snackbar.LENGTH_LONG).show()
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
