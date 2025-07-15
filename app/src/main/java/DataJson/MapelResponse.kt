package DataJson

import Domain.Mapel

data class MapelResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<Mapel>
)

