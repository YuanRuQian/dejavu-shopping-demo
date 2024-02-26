package lydia.yuan.dajavu.network

import retrofit2.http.GET

interface CitiesApiServices {
    @GET("/cities.json")
    suspend fun getCities(): Cities
}