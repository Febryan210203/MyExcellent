package Activity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.SmartTutor.PemesananFragment
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityDetailGuruBinding

class DetailGuruActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailGuruBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGuruBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val nama = intent.getStringExtra("namaGuru")
        val pendidikan = intent.getStringExtra("pendidikanGuru")
        val Nip = intent.getStringExtra("nipGuru")
        val noHp = intent.getStringExtra("noHpGuru")
        val mapel = intent.getStringExtra("mapelGuru")
        val biodata = intent.getStringExtra("biodataGuru")
        val alamat = intent.getStringExtra("alamatGuru")
        val idGuru = intent.getStringExtra("id_guru")
        val foto = intent.getStringExtra("fotoGuru")



        binding.txtNamaGuru.text = nama ?: "Nama tidak tersedia"
        binding.txtPendidikan.text = pendidikan ?: "Pendidikan tidak tersedia"
        binding.txtNonip.text = Nip ?: "nonip tidak tersedia"
        binding.txtNohp.text = noHp ?: "nohp tidak tersedia"
        binding.txtMapel.text = mapel ?: "Mapel tidak tersedia"
        binding.txtBiodataGuru.text = biodata ?: "Biodata tidak tersedia"
        binding.txtAlamat.text = alamat ?: "alamat tidak tersedia"

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Tombol Booking Sekarang
        binding.btnBooking.setOnClickListener {
            val namaGuru = binding.txtNamaGuru.text.toString()
            val namaMapel = binding.txtMapel.text.toString()

            val bundle = Bundle().apply {
                putString("namaGuru", namaGuru)
                putString("namaLayanan", namaMapel)
                putString("id_guru", idGuru )
                putString("statusguru", "guru")
            }

            // Inisialisasi Fragment dan kirim data
            val fragment = PemesananFragment()
            fragment.arguments = bundle

            // Replace fragment ke container (misal dengan id: R.id.fragment_container)
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit()

            Glide.with(this)
                .load("https://smarttutor.desabinor.id/storage/$foto")
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.guru1)
                .into(binding.imgGuru)
        }
    }
}