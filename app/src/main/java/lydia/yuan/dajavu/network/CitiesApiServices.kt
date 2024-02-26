package lydia.yuan.dajavu.network

import retrofit2.http.GET

interface CitiesApiServices {
    @GET("/v1/projects/dejavu-shopping-demo/databases/(default)/documents/cities")
    suspend fun getCities(): CitiesResponse
}