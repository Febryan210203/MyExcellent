package Domain

data class PermohonanModel(
    val id_permohonan: String,
    val status_permohonan: String,
    val nama_mapel: String,
    val id_guru: String,
    val jenjang: String,
    val nama_pelajar: String,
    val nama_layanan: String
)
