package lydia.yuan.dajavu.network

import kotlinx.serialization.Serializable

@Serializable
data class AbilityResponse(
    val count: Long,
    val next: String?,
    val previous: String?,
    val results: List<Ability>,
)

@Serializable
data class Ability(
    val name: String,
    val url: String,
)
