package br.com.livroandroid.carros

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        fab.setOnClickListener {view ->
            Snackbar.make(view, "Teste", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show()
        }

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
            R.id.nav_item_carros_esportivos -> {
                toast("Esportivos")
            }
            R.id.nav_item_carros_classicos -> {
                toast("Classicos")
            }
            R.id.nav_item_carros_luxo -> {
                toast("Luxo")
            }
            R.id.nav_item_site_livro -> {
                toast("Site Livro")
            }
            R.id.nav_item_settings -> {
                toast("Config")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
