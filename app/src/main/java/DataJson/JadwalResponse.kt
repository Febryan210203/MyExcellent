package DataJson

import Domain.JadwalModel

data class JadwalResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<JadwalModel>
)
