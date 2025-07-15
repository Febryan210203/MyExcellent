package DataJson

import Domain.LayananModel

data class LayananResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<LayananModel>
)
