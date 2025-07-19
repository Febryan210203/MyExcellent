package Activity


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.SmartTutor.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Token baru: $token")




        // Simpan token ke SharedPreferences atau kirim langsung ke server
        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("fcm_token", token).apply()

//        // Kirim ke server
//        sendTokenToServer(token)
    }

//    private fun sendTokenToServer(token: String) {
//        val bearerToken = "119|tGecog44WLe3Vu7v6MPk1Jfq9saL24AkwcbgN6S43a6e4c69" // Ganti token ini dengan token login user sebenarnya
//
//        ApiClient.instance.sendFcmToken("Bearer $bearerToken", token)
//            .enqueue(object : retrofit2.Callback<TokenResponse> {
//                override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
//                    if (response.isSuccessful) {
//                        val body = response.body()
//                        if (body?.success == true) {
//                            Log.d("SENDTOKEN", "Token berhasil disimpan: ${body.message}")
//                        } else {
//                            Log.d("BACKSEND", "Gagal simpan token: ${body?.message}")
//                        }
//                    } else {
//                        Log.d("ERORSEND", "Respon error: ${response.code()} ${response.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                    Log.d("FCM", "Error koneksi: ${t.message}")
//                }
//            })
//
//        Log.d("FCM_SEND", "Mengirim token ke server: $token")
//    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM_MESSAGE", "Pesan masuk: ${remoteMessage.data}")

        val title = remoteMessage.notification?.title ?: "SmartTutor"
        val body = remoteMessage.notification?.body ?: "Ada pemberitahuan baru!"
        val message = remoteMessage.notification?.body ?: "Pesan kosong"


        showNotification(title,body)
//
    }

    private fun showNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(
            this,0,intent,PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = "default_chanel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logofix) // Ganti dengan ikon notif kamu
            .setContentTitle(title)
            .setContentText(body)
//            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Untuk Android Oreo ke atas, buat notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SmartTutor Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
