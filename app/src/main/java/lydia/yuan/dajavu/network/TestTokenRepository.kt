package lydia.yuan.dajavu.network

import okhttp3.ResponseBody

interface TestTokenRepository {
    suspend fun getUserContent(): ResponseBody
}

class NetworkTestTokenRepository(
    private val testTokenApiServices: TestTokenApiServices
) : TestTokenRepository {
    override suspend fun getUserContent(): ResponseBody = testTokenApiServices.getUserContent()
}