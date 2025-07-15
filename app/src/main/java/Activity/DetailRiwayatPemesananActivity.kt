package Activity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityDetailRiwayatPemesananBinding

class DetailRiwayatPemesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRiwayatPemesananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRiwayatPemesananBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // Ambil data dari intent (dari HistoryFragment)
        val NamaMapel = intent.getStringExtra("nama_mapel") ?: "-"
        val NamaGuru = intent.getStringExtra("nama_guru") ?: "-"
        val TanggalMulai = intent.getStringExtra("tanggal_mulai") ?: "-"
        val Jenjang = intent.getStringExtra("jenjang") ?: "-"
        val NamaLayanan = intent.getStringExtra("nama_layanan") ?: "-"
        val biaya = intent.getIntExtra("biaya", 150)


        // Set data ke TextView
        binding.txtMapel.text = "Mapel: $NamaMapel"
        binding.txtGuru.text = "Guru: $NamaGuru"
        binding.txtTanggalBooking.text = "Tanggal: $TanggalMulai"
        binding.txtJenjang.text = "jenjang: $Jenjang"
        binding.txtTipe.text = "namalayanan: $NamaLayanan"
        binding.txtTotalHarga.text = "Nominal Registrasi : Rp.${biaya / 150} ribu"


        // (Opsional) Jika kamu ingin mengganti gambar bukti pembayaran dari URL atau URI, bisa juga ditambahkan di sini
        // binding.imgBuktiPembayaran.setImageURI(uri)
        // atau pakai Glide/Picasso jika dari URL
    }
}
