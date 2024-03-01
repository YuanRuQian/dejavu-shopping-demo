package lydia.yuan.dajavu.network

import kotlinx.serialization.Serializable

@Serializable
data class SearchPlaceResponse(
    val places: List<Place>,
)

@Serializable
data class Place(
    val formattedAddress: String,
    val displayName: DisplayName,
)

@Serializable
data class DisplayName(
    val text: String,
    val languageCode: String,
)

