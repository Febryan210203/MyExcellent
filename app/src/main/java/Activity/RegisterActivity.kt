package Activity
import android.app.Activity
import ApiService.ApiClient
import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.SmartTutor.R
import com.example.SmartTutor.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
//    private var jenisKelaminValue: String = ""
//    private var selectedImageUri: Uri? = null
private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    private var photo: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Izin diberikan → buka galeri
                    openGallery()
                } else {
                    Toast.makeText(this, "Izin akses ditolak", Toast.LENGTH_SHORT).show()
                }
            }

        binding.imageUploadView.setOnClickListener {
            checkAndRequestPermission()

        }
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    // Tampilkan ke ImageView (optional)
                    binding.imageUploadView.setImageURI(it)
                    Log.d("TAG", "cekfoto: $it")
                    photo = convertImageToMultipart(this@RegisterActivity, it)
                }
            }
        }



        binding.emailEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Index drawable kanan
                val drawable = binding.emailEditText.compoundDrawables[drawableEnd]
                if (drawable != null) {
                    val drawableWidth = drawable.bounds.width()
                    val touchAreaStart =
                        binding.emailEditText.width - binding.emailEditText.paddingEnd - drawableWidth
                    if (event.x >= touchAreaStart) {
                        val email = binding.emailEditText.text.toString().trim()
                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(this, "Email valid ✔️", Toast.LENGTH_SHORT).show()
                            // Tambahkan logika lain jika diperlukan
                        } else {
                            Toast.makeText(this, "Format email tidak valid ❌", Toast.LENGTH_SHORT)
                                .show()
                        }
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }


        // Variabel status tampilan password
        var isPasswordVisible = false
        var isConfirmPasswordVisible = false

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

// Handle klik pada drawableEnd di EditText confirm password
        binding.EdtConfirm.setOnTouchListener { v, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Right drawable index
                val drawable = binding.EdtConfirm.compoundDrawables[drawableEnd]
                if (drawable != null) {
                    val drawableWidth = drawable.bounds.width()
                    val touchAreaStart =
                        binding.EdtConfirm.width - binding.EdtConfirm.paddingEnd - drawableWidth
                    if (event.x >= touchAreaStart) {
                        isConfirmPasswordVisible = !isConfirmPasswordVisible
                        if (isConfirmPasswordVisible) {
                            binding.EdtConfirm.inputType =
                                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            binding.EdtConfirm.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.baseline_visibility_24,
                                0
                            )
                        } else {
                            binding.EdtConfirm.inputType =
                                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                            binding.EdtConfirm.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.visibilityof,
                                0
                            )
                        }
                        binding.EdtConfirm.setSelection(binding.EdtConfirm.text.length)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.imageUploadView.setOnClickListener {
            checkAndRequestPermission()
        }

        val calendar = Calendar.getInstance()


        binding.tanggalLahirEditText.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate =
                    String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.tanggalLahirEditText.setText(formattedDate)
            }, year, month, day)

            // Tidak perlu set minDate, atau:
            datePicker.datePicker.maxDate = System.currentTimeMillis()


            datePicker.show()
        }


        binding.registerButton.setOnClickListener {
            val nama = binding.namaEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val no_hp = binding.noHpEditText.text.toString().trim()
            val alamat = binding.alamatEditText.text.toString().trim()
            val password_confirmation = binding.EdtConfirm.text.toString().trim()
            val tanggal_lahir = binding.tanggalLahirEditText.text.toString().trim()
            val tempat_lahir = binding.tempatLahirEditText.text.toString().trim()
            val agama = binding.agamaEditText.text.toString().trim()
            val nis = binding.nisEditText.text.toString().trim()

            val jenisKelamin = when {
                binding.rbLaki.isChecked -> "L"
                binding.rbPerempuan.isChecked -> "P"
                else -> ""
            }
            Log.d("YOLO", "cek jeniskelamin: $jenisKelamin")


            //validasi

//            if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || no_hp.isEmpty() || alamat.isEmpty() || toString().isNullOrEmpty()
//                || password_confirmation.isEmpty() || jenisKelamin.isEmpty() || tanggal_lahir.isEmpty() || tempat_lahir.isEmpty() || agama.isEmpty() || nis.isEmpty()) {
//                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
//            } else if (password != password_confirmation) {
//                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
//            } // Panggil API login
            Log.d("TAG", "cek registerbutton: $jenisKelamin ")

            registerFetching(
                nama,
                email,
                password,
                password_confirmation,
                alamat,
                photo,
                no_hp,
                tempat_lahir,
                tanggal_lahir,
                agama,
                nis,
                jenisKelamin
            )
        }
    }


    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                requiredPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Sudah diizinkan → langsung buka galeri
            openGallery()
        } else {
            //minta izin
            permissionLauncher.launch(requiredPermission)
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
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
        return MultipartBody.Part.createFormData("foto", file.name, requestFile)
    }


    private fun registerFetching(
        nama: String,
        email: String,
        password: String,
        passwordConfirm: String,
        alamat: String,
        photo: MultipartBody.Part?,
        noHp: String,
        ttl: String,
        tglLahir: String,
        agama: String,
        nis: String,
        jenisKelamin: String
    ) {

        lifecycleScope.launch {
            if (photo == null){
                Toast.makeText(this@RegisterActivity, "Foto tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val response = ApiClient.instance.register(
                createPartFromString(noHp),
                createPartFromString(agama),
                createPartFromString(alamat),
                createPartFromString(nama),
                createPartFromString(ttl),
                photo,
                createPartFromString(nis),
                createPartFromString(jenisKelamin),
                createPartFromString(tglLahir),
                createPartFromString(email),
                createPartFromString(password),
                createPartFromString(passwordConfirm)
            )
            try {
                if (response.isSuccessful) {

                    val user = response.body()?.data
                    Log.d("TAG", "registerFetching: $response")



                    val intent = Intent(this@RegisterActivity, MyLogin::class.java)
                    intent.putExtra("nama", response.body()!!.data!!.nama.toString())
                    startActivity(intent)

                } else {
                    Log.d("TAG", "registerFetchingfailed: $response")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("error", e.message.toString())
            }

        }
    }
}
