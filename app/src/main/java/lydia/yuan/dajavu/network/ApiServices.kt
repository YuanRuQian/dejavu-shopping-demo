package lydia.yuan.dajavu.network


import retrofit2.http.GET
import retrofit2.http.Path

// Retrofit service interface for user-related API calls
interface ApiServices {
    @GET("/api/v2/egg-group/{name}")
    suspend fun getEggGroup(@Path("name") name: String): EggGroupResponse
}