package lydia.yuan.dajavu.network

import kotlinx.serialization.Serializable

@Serializable
data class Cities(
    val data : Map<String, City>
)

@Serializable
data class City(
    val name: String
)
