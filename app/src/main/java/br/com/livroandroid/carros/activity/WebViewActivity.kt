package br.com.livroandroid.carros.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.activity.dialog.AboutDialog
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.include_progress.*
import org.jetbrains.anko.toast

class WebViewActivity : BaseActivity() {
    private val url = "http://www.livroandroid.com.br/sobre.htm"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web_view)

        webView.loadUrl(url)

        webView.settings.javaScriptEnabled = true

        initWebViewClient()

        // Swipe to Refresh
        swipeToRefresh.setOnRefreshListener {
            webView.reload()
        }
        swipeToRefresh.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)
    }

    private fun initWebViewClient() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                if(! swipeToRefresh.isRefreshing) {
                    progress.visibility = View.VISIBLE
                }

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                progress.visibility = View.INVISIBLE

                // Termina a animação do Swipe to Refresh
                swipeToRefresh.isRefreshing = false
            }

            @Suppress("OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.endsWith("sobre.htm")) {
                    AboutDialog.show(activity)
                    return true
                }

                @Suppress("DEPRECATION")
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }
}
