package DataJson

import com.google.gson.annotations.SerializedName
import java.io.File

data class DisetujuiResponse(
	@SerializedName("status") val status: Boolean,
	@SerializedName("code") val code: Int,
	@SerializedName("message") val message: String,
	@SerializedName("data") val data: List<DisetujuiDataItem>
)

data class DisetujuiDataItem(
	@SerializedName("id_permohonan") val idPermohonan: String,
	@SerializedName("id_pelajar") val idPelajar: String,
	@SerializedName("id_mapel") val idMapel: String,
	@SerializedName("id_layanan") val idLayanan: String,
	@SerializedName("id_guru") val idGuru: String,
	@SerializedName("status_permohonan") val statusPermohonan: String,
	@SerializedName("tanggal_mulai") val tanggalMulai: String,
	@SerializedName("created_at") val createdAt: String,
	@SerializedName("updated_at") val updatedAt: String,
	@SerializedName("deleted_at") val deletedAt: String?, // bisa null
	@SerializedName("nama_guru") val namaGuru: String,
	@SerializedName("nama_pelajar") val namaPelajar: String,
	@SerializedName("nama_mapel") val namaMapel: String,
	@SerializedName("jenjang") val jenjang: String,
	@SerializedName("nama_layanan") val namaLayanan: String,
	@SerializedName("file")  val file: String?
)
