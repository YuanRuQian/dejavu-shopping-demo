package lydia.yuan.dajavu.network


interface CitiesRepository {
    suspend fun getCities(): Cities
}

class NetworkCitiesRepository(
    private val citiesApiServices: CitiesApiServices
) : CitiesRepository {
    override suspend fun getCities(): Cities = citiesApiServices.getCities()
}