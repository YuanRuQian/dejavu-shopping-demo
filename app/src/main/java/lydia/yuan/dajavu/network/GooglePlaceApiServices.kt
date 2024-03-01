package lydia.yuan.dajavu.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GooglePlaceApiServices {
    @POST("/v1/places:searchText")
    suspend fun searchPlace(@Body searchPlaceRequest: SearchPlaceRequest): SearchPlaceResponse
}