package br.com.livroandroid.carros.fragments

import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    override fun getContext(): Context {
        return super.getContext()!!
    }
}