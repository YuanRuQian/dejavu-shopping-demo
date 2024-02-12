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
}

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


class DefaultAppContainer(application: Application) : AppContainer {
    override val pokemonRepository: PokemonRepository by lazy {
        NetworkPokemonRepository(retrofitService)
    }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // You can use other levels like BASIC or HEADERS
    }

    private val okHttpClient = OkHttpClient.Builder()
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

    private val baseUrl = "https://pokeapi.co"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/java".toMediaType()))
        .client(okHttpClient) // Set the OkHttpClient with the logging interceptor
        .baseUrl(baseUrl)
        .build()

    private val retrofitService by lazy {
        retrofit.create(ApiServices::class.java)
    }
}