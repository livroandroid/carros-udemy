package br.com.livroandroid.carros.activity.dialog

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.livroandroid.carros.R

@Suppress("DEPRECATION")
class AboutDialog {
    companion object {
        // Retorna a versão do app "versionName" do app/build.gradle
        private fun getAppVersionName(activity: AppCompatActivity): String? {
            val pm = activity.packageManager
            val packageName = activity.packageName
            val versionName: String?
            versionName = try {
                val info = pm?.getPackageInfo(packageName, 0)
                info?.versionName
            } catch (ex: PackageManager.NameNotFoundException) {
                "N/A"
            }
            return versionName
        }

        // Mostra um alerta
        fun show(activity: AppCompatActivity) {
            val aboutBody = SpannableStringBuilder()
            val versionName = getAppVersionName(activity)
            val html = Html.fromHtml(activity.getString(R.string.about_dialog_text, versionName))
            aboutBody.append(html)
            // Infla o layout
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.dialog_about, null) as TextView
            view.text = aboutBody
            view.movementMethod = LinkMovementMethod()
            // Cria o dialog customizado
            val builder = AlertDialog.Builder(activity)
                    .setTitle(R.string.about_dialog_title)
                    .setView(view)
                    .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .create()
            builder.show()
        }
    }
}