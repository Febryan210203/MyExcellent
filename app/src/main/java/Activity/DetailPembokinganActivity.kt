package Activity

import ApiService.ApiClient
import DataJson.PermohonanRequest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityDetailPembokinganBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.*
import okhttp3.MultipartBody


class DetailPembokinganActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPembokinganBinding
    private var buktiPembayaranUri: Uri? = null
    private var photo: MultipartBody.Part? = null

    // Untuk memilih gambar dari galeri
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                buktiPembayaranUri = uri
                binding.imgBuktiPembayaran.setImageURI(uri)
                binding.btnLanjutPembayaran.isEnabled = true // Aktifkan tombol bayar
                photo = convertImageToMultipart(this@DetailPembokinganActivity, uri)

                Log.d("UPLOAD_BUKTI", "Bukti pembayaran dipilih: $uri")
                showToast("Bukti pembayaran berhasil dipilih.")
            } else {
                Log.e("UPLOAD_BUKTI", "Tidak ada gambar yang dipilih")
                showToast("Gagal memilih bukti pembayaran.")
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPembokinganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val namaMapel = intent.getStringExtra("nama_mapel")
        val idMapel = intent.getStringExtra("id_mapel")
        val jenjang = intent.getStringExtra("jenjang")
        val namaLayanan = intent.getStringExtra("nama_layanan")
        val idLayanan = intent.getStringExtra("id_layanan")
        val namaGuru = intent.getStringExtra("nama_guru")
        val idGuru = intent.getStringExtra("id_guru")
        val tanggalBooking = intent.getStringExtra("tanggal_mulai")
        val jadwal = intent.getStringExtra("jadwal")
        val idJadwal = intent.getStringExtra("id_jadwal")

// Log untuk debugging
        Log.d(
            "DetailPembokingan", """
    Mapel: $namaMapel (ID: $idMapel)
    Jenjang: $jenjang
    Guru: $namaGuru (ID: $idGuru)
    Layanan: $namaLayanan (ID: $idLayanan)
    Jadwal: $jadwal (ID: $idJadwal)
    Tanggal Booking: $tanggalBooking
""".trimIndent()
        )

// Tampilkan ke UI
        binding.txtMapel.text = "Mapel: $namaMapel"
        binding.txtGuru.text = "Guru: $namaGuru"
        binding.txtJenjang.text = "Jenjang: $jenjang"
        binding.txtTipe.text = "Layanan: $namaLayanan"
        binding.txtTanggalBooking.text = "Tanggal Booking: $tanggalBooking"




        // Tombol upload gambar bukti pembayaran
        binding.btnUploadBukti.setOnClickListener {

            pickImageLauncher.launch("image/*")
        }
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        // Tombol lanjut ke pembayaran
        binding.btnLanjutPembayaran.setOnClickListener {
            if (buktiPembayaranUri == null) {
                Log.e("UPLOAD_BUKTI", "Gagal: URI bukti pembayaran NULL")
                Toast.makeText(this, "Silahkan upload bukti pembayaran terlebih dahulu", Toast.LENGTH_LONG).show()
                return@setOnClickListener
                // Jika URI tidak null, lanjutkan proses
                Log.d("UPLOAD_BUKTI", "URI valid, lanjut ke fetchingPermohonan()")

            } else {
                Log.d("UPLOAD_BUKTI", "URI valid, lanjut proses upload")
                fetchingPermohonan()


                // Simpan ke riwayat sebelum tampilkan dialog sukses

                // Tampilkan animasi dan dialog sukses
                binding.imgChecklist.apply {
                    visibility = View.VISIBLE
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            this@DetailPembokinganActivity,
                            R.anim.anim_bounce
                        )
                    )
                }

                showPaymentSuccessDialog()
            }
        }

    }
    fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }
    fun convertImageToMultipart(context: Context, uri: Uri): MultipartBody.Part? {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "upload_image.jpg")

        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }


    private fun uploadBuktiPembayaran(idPermohonan: String, uri: MultipartBody.Part?) {
        lifecycleScope.launch {

            // Panggil endpoint
            val response = ApiClient.instance.upload( createPartFromString(idPermohonan), uri!!)

            if (response.isSuccessful) {
                Log.d("cek uplod bukti", "sukses: ${response.body()}")
                val body = response.body()
                if (body != null && body.status == true) {
                    showToast("Upload sukses: ${body.message}")
                    showPaymentSuccessDialog()
                } else {
                    showToast("Gagal: ${body?.message ?: "Respon kosong"}")
                    Log.d("Upload gagal", "gagal: ${body}")
                }
            } else {
                showToast("Upload gagal: ${response.message()}")
                Log.d("Upload gagal", "upload gagal: ${response}")

            }
        }
    }




    private fun fetchingPermohonan() {
        lifecycleScope.launch {
            try {
                // Ambil data dari intent
                val idMapel = intent.getStringExtra("id_mapel") ?: ""
                val idGuru = intent.getStringExtra("id_guru") ?: ""
                val idLayanan = intent.getStringExtra("id_layanan") ?: ""
                val tanggalMulai = intent.getStringExtra("tanggal_mulai") ?: ""

                // Ambil ID pelajar dari SharedPreferences
                val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                val idPelajar = sharedPref.getString("id_pelajar", null)

                if (idPelajar == null) {
                    showToast("ID Pelajar tidak ditemukan.")
                    return@launch
                }

                Log.d(
                    "fetchpermohonan",
                    "id_pelajar: $idPelajar, id_mapel: $idMapel, id_layanan: $idLayanan, id_guru: $idGuru"
                )

                // Kirim permohonan ke server
                val permohonanRequest = PermohonanRequest(
                    idPelajar,
                    idMapel,
                    idGuru,
                    idLayanan,
                    "menunggu",
                    tanggalMulai,
                )

                val response = ApiClient.instance.createPermohonanModel(permohonanRequest)

                if (response.isSuccessful && response.body()?.status == true) {
                    Log.d("cek permohonan", "Sukses: ${response.body()}")
                    val idPermohonan = response.body()!!.data?.idPermohonan.toString()
                    uploadBuktiPembayaran(idPermohonan, photo)



                    Toast.makeText(
                        this@DetailPembokinganActivity,
                        "Permohonan Berhasil",
                        Toast.LENGTH_SHORT
                    ).show()

// ✅ Delay opsional agar data disimpan dulu di server
                    delay(1000)

                    // ✅ Navigasi ke MainActivity dan tampilkan langsung HistoryFragment
                    val intent = Intent(this@DetailPembokinganActivity, MainActivity::class.java)
                    intent.putExtra("showHistory", true)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("Permohonan", "Gagal: ${response.errorBody()?.string()}")
                    showToast("Gagal mengirim permohonan.")
                }

            } catch (e: Exception) {
                Log.e("Permohonan", "Error: ${e.message}")
                showToast("Terjadi kesalahan jaringan.")
            }
        }
    }


    // Fungsi untuk format rupiah
    private fun Int.formatRupiah(): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        return format.format(this).replace(",00", "")
    }

    // Menampilkan toast
    private fun showToast(pesan: String) {
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
    }

    // Simpan data ke SharedPreferences (DIPERBAIKI)
    private fun simpanKeRiwayat(
        mapel: String,
        guru: String,
        tanggal: String,
        jenjang: String,
        tipe: String,
        biaya: String
    ) {
        val sharedPref = getSharedPreferences("riwayat_pembooking", Context.MODE_PRIVATE)
        val existingSet =
            sharedPref.getStringSet("riwayat", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        val result = sharedPref.getStringSet("riwayat", emptySet())
        Log.d("CEK_HISTORY", "Riwayat saat ini: $result")


// Gabungkan data dalam satu string, gunakan | sebagai pemisah
        val newEntry = "$mapel|$guru|$tanggal|$jenjang|$tipe|$biaya|Menunggu"

// Tambahkan ke set dan simpan kembali
        existingSet.add(newEntry)
        sharedPref.edit().putStringSet("riwayat", existingSet).apply()

    }

        // Dialog sukses pembayaran
        private fun showPaymentSuccessDialog() {
            val dialogView = layoutInflater.inflate(R.layout.dialog_payment_success, null)
            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create()

            val btnOke: View = dialogView.findViewById(R.id.btnOke)
            btnOke.setOnClickListener {
                alertDialog.dismiss()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
//                finish()
            }

            alertDialog.show()
        }
    }
