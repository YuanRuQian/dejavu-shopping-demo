package lydia.yuan.dajavu.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import lydia.yuan.dajavu.MyApplication
import lydia.yuan.dajavu.network.LoginRequest
import lydia.yuan.dajavu.network.TestTokenRepository
import lydia.yuan.dajavu.network.TokenRepository

class TokenViewModel(
    val tokenRepository: TokenRepository,
    val testTokenRepository: TestTokenRepository
) : ViewModel() {

    fun signIn(username: String, password: String, context: Context) {
        viewModelScope.launch {
            val loginResponse = LoginRequest(
                username = username,
                password = password
            )
            val response = tokenRepository.signIn(loginResponse)
            val application = context.applicationContext as MyApplication
            application.container.keyStoreUtils.saveToken(response.accessToken)
        }
    }

    fun loadUserContent() {
        viewModelScope.launch {
            testTokenRepository.getUserContent()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                TokenViewModel(
                    tokenRepository = application.container.tokenRepository,
                    testTokenRepository = application.container.testTokenRepository
                )
            }
        }
    }
}
