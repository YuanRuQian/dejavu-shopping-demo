package lydia.yuan.dajavu.network

data class SearchPlaceResponse(
    val places: List<Place>,
)

data class Place(
    val formattedAddress: String,
    val displayName: DisplayName,
)

data class DisplayName(
    val text: String,
    val languageCode: String,
)

