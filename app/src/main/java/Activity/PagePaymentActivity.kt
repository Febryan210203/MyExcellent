package Activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityPagePaymentBinding

class PagePaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagePaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagePaymentBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Data dari intent atau API
        val namaMapel = intent.getStringExtra("NAMA_MAPEL") ?: "Matematika"
        val totalBayar = intent.getIntExtra("TOTAL_BAYAR", 150000)
        val virtualAccount = intent.getStringExtra("VA") ?: "8808123456789012"

        // Tampilkan data
        binding.tvNamaMapel.text = "Nama Mapel: $namaMapel"
        binding.tvTotalBayar.text = "Total: Rp ${totalBayar}"
        binding.tvVirtualAccount.text = virtualAccount

        // Salin nomor VA
        binding.btnSalinVA.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Virtual Account", virtualAccount)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Nomor VA disalin", Toast.LENGTH_SHORT).show()
        }

        // Tombol cek status (sementara tampilkan Toast)
        binding.btnCekStatusPembayaran.setOnClickListener {
            Toast.makeText(this, "Memeriksa status pembayaran...", Toast.LENGTH_SHORT).show()

            // Di sini kamu bisa fetch API status pembayaran dan tampilkan dialog status
        }
    }
}
