package br.com.livroandroid.carros.activity

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.dialog.AboutDialog
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.include_progress.*

@Suppress("DEPRECATION")
class WebViewActivity : BaseActivity() {
    private val url = "http://www.livroandroid.com.br/sobre.htm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webview.loadUrl(url)

        initWebViewClient()

        // Swipe to Refresh
        swipeToRefresh.setOnRefreshListener {
            webview.reload()
        }
        swipeToRefresh.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)
    }

    private fun initWebViewClient() {
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(webview: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(webview, url, favicon)
                // Liga o progress
                if(!swipeToRefresh.isRefreshing) {
                    progress.visibility = View.VISIBLE
                }
            }

            override fun onPageFinished(webview: WebView, url: String) {
                // Desliga o progress
                progress.visibility = View.INVISIBLE
                // Termina a animação do Swipe to Refresh
                swipeToRefresh.isRefreshing = false
            }

            @Suppress("OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.endsWith("sobre.htm")) {
                    AboutDialog.show(activity)
                    // Retorna true para informar que interceptamos o evento
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if(request != null) {
                    val url = request.url.toString()
                    if (url.endsWith("sobre.htm")) {
                        // Alerta customizado
                        AboutDialog.show(activity)
                        // Retorna true para informar que interceptamos o evento
                        return true
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    }
}
