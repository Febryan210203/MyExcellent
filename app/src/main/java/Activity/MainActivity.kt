package Activity


import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.SmartTutor.HistoryFragment
import com.example.SmartTutor.HomeFragment
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityMainBinding
import Profile.ProfileFragment
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.SmartTutor.PemesananFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String> // ✅ deklarasi launcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cek apakah user sudah login
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, MyLogin::class.java))
            finish()
            return
        }



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getBooleanExtra("showHistory", false)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, HistoryFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, HomeFragment())
                .commit()
        }

        // ✅ Inisialisasi permission launcher
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Izin notifikasi diberikan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
            }

        }



        val homeFragment = HomeFragment()
        val historyFragment = HistoryFragment()
        val pemeesananFragment = PemesananFragment()
        val profileFragment = ProfileFragment()

        replaceFragment(homeFragment)

        // Menampilkan semua label teks di BottomNavigation
        binding.bottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED


        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.homeee -> replaceFragment(homeFragment)
                R.id.history -> replaceFragment(historyFragment)
                R.id.pembokingan -> replaceFragment(pemeesananFragment)
                R.id.profile -> replaceFragment(profileFragment)
//                R.id.menu -> replaceFragment(menuFragment)
            }

            true
        }
        // === 4. Minta izin notifikasi ===
        askNotificationPermission()

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Sudah diizinkan
                    Toast.makeText(this, "Notifikasi diizinkan", Toast.LENGTH_SHORT).show()
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Bisa tampilkan dialog edukasi
                    Toast.makeText(
                        this,
                        "Aplikasi butuh izin notifikasi untuk memberitahu pemesanan",
                        Toast.LENGTH_LONG
                    ).show()

                    // Lalu langsung minta izin
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> {
                    // Langsung minta izin
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }

        }
    }

    private fun replaceFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commitNow()

}



