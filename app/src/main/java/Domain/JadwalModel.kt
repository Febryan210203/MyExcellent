package Domain

data class JadwalModel(
    val id_jadwal_guru: String,
    val hari: String,
    val jam_mulai: String,
    val jam_selesai: String,
    val id_guru: String,
    val id_mapel: String,
    val deleted_at: String?,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val nama_guru: String,
    val foto_guru: String,
    val email_guru: String,
    val nama_mapel: String,
    val id_layanan: String,
    val nama_layanan: String
)
