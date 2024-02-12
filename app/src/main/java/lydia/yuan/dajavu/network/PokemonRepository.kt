package lydia.yuan.dajavu.network

interface PokemonRepository {
    suspend fun getEggGroup(name: String): EggGroupResponse
}

class NetworkPokemonRepository(
    private val apiServices: ApiServices
) : PokemonRepository {
    override suspend fun getEggGroup(name: String): EggGroupResponse = apiServices.getEggGroup(name)

}