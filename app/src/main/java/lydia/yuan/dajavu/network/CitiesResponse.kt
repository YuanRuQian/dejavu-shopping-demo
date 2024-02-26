package lydia.yuan.dajavu.network

import kotlinx.serialization.Serializable

@Serializable
data class CitiesResponse(
    val documents: List<Document>,
)

@Serializable
data class Document(
    val name: String,
    val fields: Fields,
    val createTime: String,
    val updateTime: String,
)

@Serializable
data class Fields(
    val name: CityName,
)

@Serializable
data class CityName(
    val stringValue: String,
)
