package lydia.yuan.dajavu.network


import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File


// provide the app with access to the ArtworkRepository as a global state
interface AppContainer {
    val pokemonRepository: PokemonRepository
    val citiesRepository: CitiesRepository
}

//class TokenInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val newRequest = request.newBuilder()
//            .addHeader("Authorization", "Bearer $TOKEN")
//            .build()
//        return chain.proceed(newRequest)
//    }
//}

class CachingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Check if the response should be cached based on your conditions
        if (shouldCacheResponse(response)) {
            // Add caching headers to the response
            return response.newBuilder()
                .header("Cache-Control", "public, max-age=86400") // Adjust the max-age as needed
                .build()
        }

        return response
    }

    private fun shouldCacheResponse(response: Response): Boolean {
        // Cache only when the response status code is 200 (OK)
        return response.isSuccessful
    }
}

private val json = Json { ignoreUnknownKeys = true }

class DefaultAppContainer(application: Application) : AppContainer {
    override val pokemonRepository: PokemonRepository by lazy {
        NetworkPokemonRepository(pokemonRetrofitService)
    }

    override val citiesRepository: CitiesRepository by lazy {
        NetworkCitiesRepository(citiesRetrofitService)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            HttpLoggingInterceptor.Level.HEADERS // You can use other levels like BASIC or HEADERS
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY // You can use other levels like BASIC or HEADERS
    }

    private val pokemonOkHttpClient = OkHttpClient.Builder()
        .cache(
            Cache(
                directory = File(application.cacheDir, "http_cache"),
                // $0.05 worth of phone storage in 2020
                maxSize = 50L * 1024L * 1024L // 50 MiB
            )
        )
        .addInterceptor(loggingInterceptor)
        .addInterceptor(CachingInterceptor())
        .build()

    private val citiesOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        // .addInterceptor(TokenInterceptor())
        .build()

    private val pokemonBaseUrl = "https://pokeapi.co"
    private val citiesBaseUrl = "https://dejavu-shopping-demo-default-rtdb.firebaseio.com"

    @OptIn(ExperimentalSerializationApi::class)
    private val pokemonRetrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/java".toMediaType()))
        .client(pokemonOkHttpClient)
        .baseUrl(pokemonBaseUrl)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    private val citiesRetrofit = Retrofit.Builder()
        .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/java".toMediaType()))
        .client(citiesOkHttpClient)
        .baseUrl(citiesBaseUrl)
        .build()

    private val pokemonRetrofitService by lazy {
        pokemonRetrofit.create(PokemonApiServices::class.java)
    }

    private val citiesRetrofitService by lazy {
        citiesRetrofit.create(CitiesApiServices::class.java)
    }
}