@file:Suppress("DEPRECATION")

package br.com.livroandroid.carros.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun AppCompatActivity.toast(message: CharSequence, length: Int = Toast.LENGTH_SHORT) =
        runOnUiThread { Toast.makeText(this, message, length).show() }

fun AppCompatActivity.toast(@StringRes message: Int, length: Int = Toast.LENGTH_SHORT) =
        runOnUiThread { Toast.makeText(this, message, length).show() }

// Precisa da permissão ACCESS_NETWORK_STATE
fun FragmentActivity.isNetworkAvailable(): Boolean {
    val connectivity = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networks = connectivity.allNetworkInfo
    if (networks != null) {
        for (n in networks) {
            if (n.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
    }
    return false
}