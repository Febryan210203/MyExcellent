package Domain

data class GuruModel(
    val id_guru: String,
    val nip: String,
    val id: Int,
    val biodata: String?,
    val pendidikan_terakhir: String?,
    val created_at: String,
    val updated_at: String,
    val deleted_at: String?,
    val foto: String?,
    val no_hp: String?,
    val alamat: String?,
    val id_mapel: String,
    val nama_guru: String,
    val nama_mapel: String
)
