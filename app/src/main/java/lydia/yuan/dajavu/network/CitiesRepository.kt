package lydia.yuan.dajavu.network


interface CitiesRepository {
    suspend fun getCities(): CitiesResponse
}

class NetworkCitiesRepository(
    private val citiesApiServices: CitiesApiServices
) : CitiesRepository {
    override suspend fun getCities(): CitiesResponse = citiesApiServices.getCities()
}