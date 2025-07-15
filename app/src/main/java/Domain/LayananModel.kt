package Domain

data class LayananModel(
    val id_layanan: String,
    val nama_layanan: String,
    val spp_bulanan: String,
    val lokasi_belajar: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val deleted: String,
) {
    override fun toString(): String {
        return nama_layanan
    }
}
