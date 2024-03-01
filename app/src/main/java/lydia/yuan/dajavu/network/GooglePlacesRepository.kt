package lydia.yuan.dajavu.network

interface GooglePlacesRepository {
    suspend fun searchPlace(searchPlaceRequest: SearchPlaceRequest): SearchPlaceResponse
}

class NetworkGooglePlacesRepository(
    private val googlePlaceApiServices: GooglePlaceApiServices
) : GooglePlacesRepository {
    override suspend fun searchPlace(searchPlaceRequest: SearchPlaceRequest): SearchPlaceResponse = googlePlaceApiServices.searchPlace(searchPlaceRequest)
}