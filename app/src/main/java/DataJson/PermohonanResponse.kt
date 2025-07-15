package DataJson


import Domain.PermohonanModel

data class PermohonanResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<PermohonanModel>
)
