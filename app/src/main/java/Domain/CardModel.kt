package Domain

import java.io.Serializable
data class CardModel(
    val imageRes: Int,
    val name: String,
    val rating: String,
    val review: String
):Serializable
