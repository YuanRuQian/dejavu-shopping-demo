package lydia.yuan.dajavu.network

interface PokemonRepository {
    suspend fun getEggGroup(name: String): EggGroupResponse

    suspend fun getAbility(limit: Int, offset: Int): AbilityResponse
}

class NetworkPokemonRepository(
    private val pokemonApiServices: PokemonApiServices
) : PokemonRepository {
    override suspend fun getEggGroup(name: String): EggGroupResponse = pokemonApiServices.getEggGroup(name)

    override suspend fun getAbility(limit: Int, offset: Int): AbilityResponse = pokemonApiServices.getAbility(limit, offset)
}