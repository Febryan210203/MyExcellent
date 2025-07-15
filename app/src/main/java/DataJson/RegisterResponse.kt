package DataJson

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class Data(

    @field:SerializedName("no_hp")
    val noHp: String? = null,

    @field:SerializedName("agama")
    val agama: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null,

    @field:SerializedName("id_pelajar")
    val idPelajar: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("tempat_lahir")
    val tempatLahir: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("nis")
    val nis: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("jenis_kelamin")
    val jenisKelamin: String? = null,

    @field:SerializedName("tanggal_lahir")
    val tanggalLahir: String? = null
)
