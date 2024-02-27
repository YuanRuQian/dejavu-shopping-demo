package lydia.yuan.dajavu.network


import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header

interface TestTokenApiServices {
    @GET("/api/test/user")
    suspend fun getUserContent(@Header("accept") type: String = "*/*"): ResponseBody
}