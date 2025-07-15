data class HistoryModel(
    val id_permohonan: String,
    val id_pelajar: String,
    val id_mapel: String,
    val id_layanan: String,
    val id_guru: String,
    val status_permohonan: String,
    val tanggal_mulai: String,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?,
    val nama_guru: String,
    val nama_pelajar: String,
    val nama_mapel: String,
    val jenjang: String,
    val nama_layanan: String
)