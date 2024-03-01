package lydia.yuan.dajavu.network


import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiServices {

    @GET("/api/v2/ability")
    suspend fun getAbility(@Query("limit") limit: Int, @Query("offset") offset: Int): AbilityResponse
}