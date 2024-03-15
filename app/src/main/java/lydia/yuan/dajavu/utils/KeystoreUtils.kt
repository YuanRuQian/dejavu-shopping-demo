package lydia.yuan.dajavu.utils


import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlin.properties.Delegates


class KeyStoreUtils(private val context: Context) {

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
    }


    private var sharedPreferences: SharedPreferences by Delegates.notNull()

    init {
        initializeSharedPreferences()
    }

    private fun initializeSharedPreferences() {
        val masterKey =
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN_KEY).apply()
    }
}
