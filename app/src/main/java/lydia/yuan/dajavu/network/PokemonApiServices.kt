package lydia.yuan.dajavu.network


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiServices {
    @GET("/api/v2/egg-group/{name}")
    suspend fun getEggGroup(@Path("name") name: String): EggGroupResponse

    @GET("/api/v2/ability")
    suspend fun getAbility(@Query("limit") limit: Int, @Query("offset") offset: Int): AbilityResponse
}