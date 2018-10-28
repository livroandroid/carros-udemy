package br.com.livroandroid.carros.fcm

import android.content.Intent
import android.util.Log
import br.com.livroandroid.carros.activity.MainActivity
import br.com.livroandroid.carros.extensions.toBundle
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.utils.NotificationUtil

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val TAG = "firebase"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Log.d(TAG, "From: ${remoteMessage?.from}")

        val notification = remoteMessage?.notification
        val data = remoteMessage?.data

        if(data != null) {
            Log.d(TAG, "Data payload: $data")
        }

        if(notification != null) {
            Log.d(TAG, "Notification Title: ${notification.title}")
            Log.d(TAG, "Notification Body: ${notification.body}")

            showNotification(notification,data)
        }
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
        // Envia token para o servidor
    }

    // Mostra uma notificação
    private fun showNotification(notification: RemoteMessage.Notification, data: MutableMap<String, String>?) {
        // Intent para abrir a MainActivity
        val intent = Intent(this, MainActivity::class.java)
        // Adiciona os parâmetros na intent
        if(data != null) {
            intent.putExtras(data.toBundle())
        }

        // Title: O título é opcional...
        val title = notification.title ?: getString(R.string.app_name)

        // Mensagem
        val msg = notification.body!!

        // Cria uma notificação.
        NotificationUtil.create(this, intent,1, title, msg)
    }
}