package Activity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.SmartTutor.PemesananFragment
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityDetailJadwalMapelBinding

class DetailJadwalGuruActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJadwalMapelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailJadwalMapelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val mapel = intent.getStringExtra("nama_mapel")
        val hari = intent.getStringExtra("hari")
        val jam = intent.getStringExtra("jam")
        val idJadwalGuru = intent.getStringExtra("id_jadwalguru")
        val guru = intent.getStringExtra("nama_guru")
        val layanan = intent.getStringExtra("nama_layanan")
        val fotoGuru = intent.getStringExtra("foto_guru")

        // Tampilkan ke tampilan
        binding.tvNamaMapel.text = mapel
        binding.tvHari.text = "Hari: $hari"
        binding.tvJam.text = "Jam: $jam"
        binding.tvGuru.text = "Guru: $guru"
        binding.tvLayanan.text = "Layanan: $layanan"

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // Tombol Booking Sekarang
        binding.btnBooking.setOnClickListener {
            val namaMapel = binding.tvNamaMapel.text.toString()
            val namaLayanan = binding.tvLayanan.text.toString()
            val namaGuru = binding.tvGuru.text.toString()

            val bundle = Bundle().apply {
                putString("namaGuru", namaMapel)
                putString("namaLayanan", namaLayanan)
                putString("namaLayanan", namaGuru)
                putString("id_jadwalguru", idJadwalGuru)
                putString("statusjadwalguru", "jadwalguru")
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
                .load(fotoGuru)
                .placeholder(R.drawable.ruangkelas)
                .into(binding.imgMapel)

        }
    }
}