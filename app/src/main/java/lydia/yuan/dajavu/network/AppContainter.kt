package lydia.yuan.dajavu.network


import android.app.Application
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import lydia.yuan.dajavu.utils.KeystoreUtils
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File


// provide the app with access to the ArtworkRepository as a global state
interface AppContainer {
    val pokemonRepository: PokemonRepository
    val tokenRepository: TokenRepository
    val testTokenRepository: TestTokenRepository
}

class CachingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (shouldCacheResponse(response)) {
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


class TokenInterceptor : Interceptor, Authenticator {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("x-access-token", KeystoreUtils.getToken())
            .build()
        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        var requestAvailable: Request? = null
        try {
            requestAvailable = response.request.newBuilder()
                .addHeader("Authorization", "Bearer ${KeystoreUtils.getToken()}")
                .build()
        } catch (ex: Exception) {
            Log.d("TokenInterceptor", "authenticate: ${ex.message}")
        }
        return requestAvailable
    }
}


class DefaultAppContainer(application: Application) : AppContainer {
    override val pokemonRepository: PokemonRepository by lazy {
        NetworkPokemonRepository(pokemonRetrofitService)
    }

    override val tokenRepository: TokenRepository by lazy {
        NetworkTokenRepository(tokenRetrofitService)
    }

    override val testTokenRepository: TestTokenRepository by lazy {
        NetworkTestTokenRepository(testTokenRetrofitService)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            HttpLoggingInterceptor.Level.BODY // You can use other levels like BASIC or HEADERS
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

    private val tokenOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val testTokenOkHttpClient = OkHttpClient.Builder()
        // .authenticator(TokenInterceptor())
        .addInterceptor(loggingInterceptor)
        .addInterceptor(TokenInterceptor())
        .build()

    private val pokemonBaseUrl = "https://pokeapi.co"
    private val tokenBaseUrl = "http://10.0.2.2:8080"

    @OptIn(ExperimentalSerializationApi::class)
    private val pokemonRetrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/java".toMediaType()))
        .client(pokemonOkHttpClient)
        .baseUrl(pokemonBaseUrl)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    private val tokenRetrofit = Retrofit.Builder()
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/java".toMediaType()))
        .client(tokenOkHttpClient)
        .baseUrl(tokenBaseUrl)
        .build()

    private val testTokenRetrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/java".toMediaType()))
        .client(testTokenOkHttpClient)
        .baseUrl(tokenBaseUrl)
        .build()

    private val pokemonRetrofitService by lazy {
        pokemonRetrofit.create(PokemonApiServices::class.java)
    }

    private val tokenRetrofitService by lazy {
        tokenRetrofit.create(TokenApiServices::class.java)
    }

    private val testTokenRetrofitService by lazy {
        testTokenRetrofit.create(TestTokenApiServices::class.java)
    }
}
