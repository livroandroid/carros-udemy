package br.com.livroandroid.carros.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.adapter.TabsAdapter
import br.com.livroandroid.carros.domain.TipoCarro
import com.google.android.material.navigation.NavigationView
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
    }

    private fun initViewPagerTabs() {
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = TabsAdapter(this, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        // Cor branca no texto (a cor de fundo é definido no layout)
        val cor = ContextCompat.getColor(this, R.color.white)
        tabLayout.setTabTextColors(cor, cor)
    }


    private fun initNavDrawer() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                toast("Settings")
                true
            }
            R.id.action_refresh -> {
                toast("Refresh")
                true
            }
            R.id.action_share -> {
                toast("Share")
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
                toast("Config")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
