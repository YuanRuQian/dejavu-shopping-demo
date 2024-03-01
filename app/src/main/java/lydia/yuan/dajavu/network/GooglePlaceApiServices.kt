package lydia.yuan.dajavu.network

import retrofit2.http.Body
import retrofit2.http.POST

interface GooglePlaceApiServices {
    @POST("/v1/places:searchText")
    suspend fun searchPlace(@Body searchPlaceRequest: SearchPlaceRequest): SearchPlaceResponse
}