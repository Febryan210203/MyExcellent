package DataJson

import com.google.gson.annotations.SerializedName

data class PermohonanRequest(
	@SerializedName("id_pelajar") val idPelajar: String? = null,
	@SerializedName("id_mapel")val idMapel: String? = null,
	@SerializedName("id_guru")val idGuru: String? = null,
	@SerializedName("id_layanan")val idLayanan: String? = null,
	@SerializedName("status_permohonan")val statusPermohonan: String,
	@SerializedName("tanggal_mulai")val tanggalMulai: String,

)

