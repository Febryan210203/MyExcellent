package DataJson

import com.google.gson.annotations.SerializedName

data class UploadBuktiResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataUpload? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataUpload(

	@field:SerializedName("id_bukti_pembayaran")
	val idBuktiPembayaran: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("path_file")
	val pathFile: String? = null,

	@field:SerializedName("id_permohonan")
	val idPermohonan: String? = null
)
