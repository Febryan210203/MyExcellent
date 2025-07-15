package Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.SmartTutor.PemesananFragment
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityDetailMapelBinding

class DetailMapelActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailMapelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailMapelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent (misalnya dikirim dari halaman sebelumnya)
        val namaMapel = intent.getStringExtra("nama_mapel")
        val kodeMapel = intent.getStringExtra("kode_mapel")
        val deskripsi = intent.getStringExtra("deskripsi")
        val status = intent.getStringExtra("status")
        val jenjang = intent.getStringExtra("jenjang")
        val namaLayanan = intent.getStringExtra("nama_layanan")
        val idMapel = intent.getStringExtra("id_mapel")
        val gambarUrl = intent.getStringExtra("gambar_mapel")

        //ambil id_mapel

        // Set data ke tampilan
        binding.tvNamaMapel.text = namaMapel
        binding.tvKodeMapel.text = "Kode Mapel: $kodeMapel"
        binding.tvStatus.text = "Status: $status"
        binding.tvJenjang.text = "Jenjang: $jenjang"
        binding.tvDeskripsi.text = "deskripsi: $deskripsi"
        binding.tvLayanan.text = "namaLayanan: $namaLayanan"

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // Tombol Booking Sekarang
        binding.btnBooking.setOnClickListener {
            val namaMapel = binding.tvNamaMapel.text.toString()
            val namaLayanan = binding.tvLayanan.text.toString()
            val jenjang = binding.tvJenjang.text.toString()

            val bundle = Bundle().apply {
                putString("namaMapel", namaMapel)
                putString("namaLayanan", namaLayanan)
                putString("jenjang", jenjang)
                putString("id_mapel", idMapel)
                putString("statusmapel", "mapel")

                //kirim id mapel
            }

            // Inisialisasi Fragment dan kirim data
            val fragment = PemesananFragment()
            fragment.arguments = bundle

            // Replace fragment ke container (misal dengan id: R.id.fragment_container)
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment)
                .addToBackStack(null).commit()

            // Tampilkan gambar menggunakan Glide
//            Glide.with(this)
//                .load(gambarUrl) // URL gambar dari API atau intent
//                .placeholder(R.drawable.bindo) // gambar default sementara loading
//                .into(binding.imgMapel)
        }
    }
}