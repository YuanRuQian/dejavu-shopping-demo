package lydia.yuan.dajavu.network


import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import lydia.yuan.dajavu.BuildConfig
import lydia.yuan.dajavu.MyApplication
import lydia.yuan.dajavu.utils.TokenStore
import okhttp3.Authenticator
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


// provide the app with access to the ArtworkRepository as a global state
interface AppContainer {
    val pokemonRepository: PokemonRepository
    val tokenRepository: TokenRepository
    val testTokenRepository: TestTokenRepository
    val googlePlaceRepository: GooglePlacesRepository
}

class GooglePlaceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl: HttpUrl = original.url

        val url: HttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("key", BuildConfig.PLACES_API_KEY)
            .addQueryParameter("fields", "places.displayName,places.formattedAddress")
            .build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

class TokenInterceptor : Interceptor, Authenticator {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("x-access-token", TokenStore.getToken() ?: "")
            .build()
        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        var requestAvailable: Request? = null
        try {
            requestAvailable = response.request.newBuilder()
                .addHeader("Authorization", "Bearer ${TokenStore.getToken()}")
                .build()
        } catch (ex: Exception) {
            Log.d("TokenInterceptor", "authenticate: ${ex.message}")
        }
        return requestAvailable
    }
}


private val json = Json {
    ignoreUnknownKeys = true
}

class DefaultAppContainer(application: MyApplication) : AppContainer {
    override val pokemonRepository: PokemonRepository by lazy {
        NetworkPokemonRepository(pokemonRetrofitService)
    }

    override val tokenRepository: TokenRepository by lazy {
        NetworkTokenRepository(tokenRetrofitService)
    }

    override val testTokenRepository: TestTokenRepository by lazy {
        NetworkTestTokenRepository(testTokenRetrofitService)
    }

    override val googlePlaceRepository: GooglePlacesRepository by lazy {
        NetworkGooglePlacesRepository(googlePlaceRetrofitService)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            HttpLoggingInterceptor.Level.BODY // You can use other levels like BASIC or HEADERS
    }

    private val pokemonOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val googlePlaceOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(GooglePlaceInterceptor())
        .build()

    private val tokenOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val testTokenOkHttpClient = OkHttpClient.Builder()
        .authenticator(TokenInterceptor())
        .addInterceptor(loggingInterceptor)
        .addInterceptor(TokenInterceptor())
        .build()

    private val pokemonBaseUrl = "https://pokeapi.co"
    private val tokenBaseUrl = "http://10.0.2.2:8080"
    private val googlePlaceBaseUrl = "https://places.googleapis.com"

    @OptIn(ExperimentalSerializationApi::class)
    private val pokemonRetrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/java".toMediaType()))
        .client(pokemonOkHttpClient)
        .baseUrl(pokemonBaseUrl)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    private val tokenRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/java".toMediaType()))
        .client(tokenOkHttpClient)
        .baseUrl(tokenBaseUrl)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    private val testTokenRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/java".toMediaType()))
        .client(testTokenOkHttpClient)
        .baseUrl(tokenBaseUrl)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    private val googlePlaceRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/java".toMediaType()))
        .client(googlePlaceOkHttpClient)
        .baseUrl(googlePlaceBaseUrl)
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

    private val googlePlaceRetrofitService by lazy {
        googlePlaceRetrofit.create(GooglePlaceApiServices::class.java)
    }
}
