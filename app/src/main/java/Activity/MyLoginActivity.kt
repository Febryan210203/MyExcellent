package Activity

import ApiService.ApiClient
import DataJson.LoginRequest
import DataJson.LoginResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityMyLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyLogin : AppCompatActivity() {

    private lateinit var binding: ActivityMyLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi ViewBinding
        binding = ActivityMyLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Drawable kanan
                val drawables = binding.emailEditText.compoundDrawables
                val drawable = drawables[drawableEnd]

                if (drawable != null) {
                    val drawableWidth = drawable.bounds.width()

                    val touchAreaStart = binding.emailEditText.width - binding.emailEditText.paddingEnd - drawableWidth
                    if (event.x >= touchAreaStart) {
                        val email = binding.emailEditText.text.toString().trim()

                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(this, "Email valid ✔️", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Format email tidak valid ❌", Toast.LENGTH_SHORT).show()
                        }

                        // Hindari setSelection jika tidak perlu
//                        return@setOnTouchListener true
                    }
                }
            }
            false
        }




        // Variabel status tampilan password
        var isPasswordVisible = false

// Handle klik pada drawableEnd di EditText password
        binding.passwordEditText.setOnTouchListener { v, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Right drawable index
                val drawable = binding.passwordEditText.compoundDrawables[drawableEnd]
                if (drawable != null) {
                    val drawableWidth = drawable.bounds.width()
                    val touchAreaStart =
                        binding.passwordEditText.width - binding.passwordEditText.paddingEnd - drawableWidth
                    if (event.x >= touchAreaStart) {
                        isPasswordVisible = !isPasswordVisible
                        if (isPasswordVisible) {
                            binding.passwordEditText.inputType =
                                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            binding.passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.baseline_visibility_24,
                                0
                            )
                        } else {
                            binding.passwordEditText.inputType =
                                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                            binding.passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.visibilityof,
                                0
                            )
                        }
                        // Agar kursor tetap di akhir teks
                        binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        // Handle padding untuk sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Tombol login ditekan
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                // Panggil API login
                fetchLogin(email, password)
            }
        }
    }

    private fun fetchLogin(email: String, password: String) {
        Log.d("LOGIN", "Coba login dengan email: $email dan password: $password")

        ApiClient.instance.loginUser(LoginRequest(email, password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()
                        Log.d("TAG", "Login Berhasil: ${response}")

                        // Contoh: simpan token ke SharedPreferences (opsional)

                        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                        sharedPref.edit()
                            .putString("nama", result?.nama)
                            .putString("email", result?.email)
                            .putString("alamat", result?.alamat)
                            .putString("nohp", result?.noHp)
                           .putString("id_pelajar", result?.idPelajar)
                            .putString("access_token", result?.accessToken) // ✅ SIMPAN TOKEN DI SINI
                            .apply()

                        val editor = sharedPref.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()
                        Log.d("Login","cek data email: ${result}")

                        // Pindah ke MainActivity

                        val intent = Intent(this@MyLogin, MainActivity::class.java)
                        intent.putExtra("nama", result?.nama)
                        intent.putExtra("email", result?.email)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("LOGIN", "Login gagal: ${response.code()} - ${response.message()}")
                        Toast.makeText(
                            this@MyLogin,
                            "Email atau password salah!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LOGIN", "Gagal koneksi ke API: ${t.message}")
                    Toast.makeText(this@MyLogin, "Terjadi kesalahan jaringan", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}

// Teks "lupa password" diklik
//        binding.forgotPasswordText.setOnClickListener {
//            Toast.makeText(this, "Fitur lupa password belum tersedia", Toast.LENGTH_SHORT).show()
//        }