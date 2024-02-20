package lydia.yuan.dajavu.network

interface PokemonRepository {
    suspend fun getEggGroup(name: String): EggGroupResponse

    suspend fun getAbility(limit: Int, offset: Int): AbilityResponse
}

class NetworkPokemonRepository(
    private val apiServices: ApiServices
) : PokemonRepository {
    override suspend fun getEggGroup(name: String): EggGroupResponse = apiServices.getEggGroup(name)

    override suspend fun getAbility(limit: Int, offset: Int): AbilityResponse = apiServices.getAbility(limit, offset)
}