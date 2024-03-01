package lydia.yuan.dajavu.network

import lydia.yuan.dajavu.BuildConfig
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GooglePlaceApiServices {
    @POST("/v1/places:searchText")
    suspend fun searchPlace(@Body searchPlaceRequest: SearchPlaceRequest,
                            @Query("key") key: String = BuildConfig.PLACES_API_KEY,
                            @Query("fields") fieldMask: String = "places.displayName,places.formattedAddress"
    ): SearchPlaceResponse
}