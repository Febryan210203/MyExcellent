package Activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.SmartTutor.databinding.ActivityEditProfile2Binding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfile2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfile2Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        // Tombol Simpan
        binding.btnSaveProfile.setOnClickListener {
            val nama = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val alamat = binding.etAddress.text.toString()
            val phone = binding.etPhone.text.toString()

            if (nama.isBlank() || email.isBlank() || phone.isBlank() || alamat.isBlank()) {
                Toast.makeText(this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                // Simpan data ke SharedPreferences
                val sharedPref = getSharedPreferences("UserProfile", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("nama", nama)
                    putString("email", email)
                    putString("alamat", alamat)
                    putString("phone", phone)
                    apply()
                }

                Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke ProfileFragment
            }
        }
    }
}