package DataJson

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class RegisterRequest(
    @field:SerializedName("no_hp")
    val noHp: String? = null,

    @field:SerializedName("agama")
    val agama: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null,


    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("tempat_lahir")
    val tempatLahir: String? = null,

    @field:SerializedName("foto")
    val foto: MultipartBody.Part? = null,

    @field:SerializedName("nis")
    val nis: String? = null,


    @field:SerializedName("jenis_kelamin")
    val jenisKelamin: String? = null,

    @field:SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("password_confirmation")
    val passwordConfirmation: String? = null
)
