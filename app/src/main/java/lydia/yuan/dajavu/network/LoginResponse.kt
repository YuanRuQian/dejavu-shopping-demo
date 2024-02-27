package lydia.yuan.dajavu.network

import kotlinx.serialization.Serializable


@Serializable
data class LoginResponse(
    val id: String,
    val username: String,
    val email: String,
    val roles: List<String>,
    val accessToken: String,
)
