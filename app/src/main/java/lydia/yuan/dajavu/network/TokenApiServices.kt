package lydia.yuan.dajavu.network

import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApiServices {
    @POST("/api/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}