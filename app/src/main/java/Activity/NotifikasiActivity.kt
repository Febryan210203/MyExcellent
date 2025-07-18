//package Activity
//
//import ApiService.ApiClient
//import DataJson.TokenRequest
//import DataJson.TokenResponse
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.widget.RemoteViews
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.SmartTutor.R
//import com.example.SmartTutor.databinding.ActivityNotifikasiBinding
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.firebase.messaging.FirebaseMessaging
//import okhttp3.ResponseBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class NotifikasiActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityNotifikasiBinding
//
//    private val channelId = "i.apps.notifications"
//    private val description = "Test notification"
//    private val notificationId = 1234
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.tvTitleNotifikasi.text = "Ini halaman notifikasi"
//        // Di sini kamu bisa tambahkan recyclerView atau konten notifikasi lainnya
//
////        // Buat notification channel
////        createNotificationChannel()
//
//        // Aksi tombol ditekan
//        binding.btn.setOnClickListener {
//            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
//                    return@addOnCompleteListener
//                }
//
//
//                // Kirim ke server
//                val token = task.result
//                Log.d("FCM", "Token: $token")
//                Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
//                val userId = "29" // ganti sesuai ID user login
//
//                ApiClient.instance.(userId, token)
//                    .enqueue(object : Callback<TokenResponse> {
//                        override fun onResponse(
//                            call: Call<TokenResponse>,
//                            response: Response<TokenResponse>
//                        ) {
//                            if (response.isSuccessful) {
//                                val body = response.body()
//                                if (body != null && body.success) {
//                                    Log.d("FCM", "Token berhasil disimpan: ${body.message}")
//                                } else {
//                                    Log.e("FCM", "Token gagal disimpan: ${body?.message}")
//                                }
//                            } else {
//                                Log.e("FCM", "Response gagal: ${response.code()}")
//                            }
//                        }
//
//                        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                            Log.e("FCM", "Gagal koneksi: ${t.message}")
//                        }
//                    })
//
//            }
//        }
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(
//                channelId,
//                description,
//                NotificationManager.IMPORTANCE_HIGH
//            ).apply {
//                enableLights(true)
//                lightColor = Color.BLUE
//                enableVibration(true)
//            }
//
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//    }
//    @SuppressLint("MissingPermission")
//    private fun sendNotification() {
//        val intent = Intent(this, AfterNotificationActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val contentView = RemoteViews(packageName, R.layout.setting_notification).apply {
//            setTextViewText(R.id.notification_title, "Hello")
//            setTextViewText(R.id.notification_text, "Selamat datang di SmartTutor!")
//        }
//
//        val builder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.logofix)
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//            .setCustomContentView(contentView)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        with(NotificationManagerCompat.from(this)) {
//            notify(notificationId, builder.build())
//        }
//    }
//}