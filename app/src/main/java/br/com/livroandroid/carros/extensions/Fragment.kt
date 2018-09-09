package br.com.livroandroid.carros.extensions

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun Fragment.runOnUiThread(action: () -> Unit) {
    activity?.runOnUiThread(action)
}

fun Fragment.toast(message: CharSequence, length: Int = Toast.LENGTH_SHORT) =
        activity?.runOnUiThread { Toast.makeText(activity, message, length).show() }

fun Fragment.toast(@StringRes message: Int, length: Int = Toast.LENGTH_SHORT) =
        activity?.runOnUiThread { Toast.makeText(activity, message, length).show() }

fun Fragment.visible(vararg views: View) {
    for (v in views) {
        v.visibility = View.VISIBLE
    }
}

fun Fragment.invisible(vararg views: View) {
    for (v in views) {
        v.visibility = View.INVISIBLE
    }
}

fun Fragment.isNetworkAvailable(): Boolean {
    val a = activity
    if(a != null) {
        return a.isNetworkAvailable()
    }
    return false
}