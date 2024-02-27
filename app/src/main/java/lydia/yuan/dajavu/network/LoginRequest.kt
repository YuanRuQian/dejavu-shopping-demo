package lydia.yuan.dajavu.network

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)
