package lydia.yuan.dajavu.network

interface PokemonRepository {

    suspend fun getAbility(limit: Int, offset: Int): AbilityResponse
}

class NetworkPokemonRepository(
    private val pokemonApiServices: PokemonApiServices
) : PokemonRepository {
    override suspend fun getAbility(limit: Int, offset: Int): AbilityResponse = pokemonApiServices.getAbility(limit, offset)
}