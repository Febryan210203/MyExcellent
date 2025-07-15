package DataJson

import Domain.GuruModel

data class GuruResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<GuruModel>
)
