package lydia.yuan.dajavu.network

import kotlinx.serialization.Serializable

@Serializable
data class SearchPlaceRequest(
    val textQuery: String,
)

