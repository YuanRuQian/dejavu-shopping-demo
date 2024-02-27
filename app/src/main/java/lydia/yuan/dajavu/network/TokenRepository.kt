package lydia.yuan.dajavu.network


interface TokenRepository {
    suspend fun signIn(loginRequest: LoginRequest): LoginResponse
}

class NetworkTokenRepository(
    private val tokenApiServices: TokenApiServices
) : TokenRepository {
    override suspend fun signIn(loginRequest: LoginRequest): LoginResponse = tokenApiServices.login(loginRequest)
}