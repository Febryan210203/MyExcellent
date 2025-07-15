package Domain

import com.google.gson.annotations.SerializedName

data class PermohonanResponse(

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

	@field:SerializedName("id_pelajar")
	val idPelajar: String? = null,

	@field:SerializedName("id_layanan")
	val idLayanan: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("tanggal_mulai")
	val tanggalMulai: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_mapel")
	val idMapel: String? = null,

	@field:SerializedName("id_guru")
	val idGuru: String? = null,

	@field:SerializedName("status_permohonan")
	val statusPermohonan: String? = null,

	@field:SerializedName("id_permohonan")
	val idPermohonan: String? = null
)
