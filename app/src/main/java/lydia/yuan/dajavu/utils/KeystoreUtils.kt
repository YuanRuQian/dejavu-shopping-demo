package lydia.yuan.dajavu.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import io.github.nefilim.kjwt.JWT
import io.github.nefilim.kjwt.sign
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.interfaces.ECPrivateKey
import java.time.Instant
import javax.crypto.Cipher


class KeystoreUtils {
    fun generateAKeyPair(): KeyPair? {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore"
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            "keystore_alias",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .build()

        keyPairGenerator.initialize(keyGenParameterSpec)
        return keyPairGenerator.generateKeyPair()
    }

    fun encryptAndStoreToken(token: String, context: Context) {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val privateKeyEntry = keyStore.getEntry("keystore_alias", null) as KeyStore.PrivateKeyEntry
        val publicKey = privateKeyEntry.certificate.publicKey
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(token.toByteArray())
        val encryptedToken = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        // Store the encrypted token in SharedPreferences
        val sharedPreferences =
            context.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", encryptedToken).apply()
    }

    fun decryptToken(keyPair: KeyPair, context: Context): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)

        val encryptedToken: ByteArray = Base64.decode(
            context.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
                .getString("token", ""), Base64.DEFAULT
        )

        val decryptedToken = cipher.doFinal(encryptedToken)
        val originalToken = String(decryptedToken)
        return originalToken
    }

    suspend fun generateJWT(): String {
        val jwt = JWT.es256 {
            subject("42")
            issuer("lydia")
            claim("name", "Lydia Yuan")
            claim("admin", true)
            issuedAt(Instant.now())
        }

        val keyPair = generateAKeyPair()
        val ecPrivateKey = keyPair?.private
        if (ecPrivateKey != null) {
            jwt.sign(ecPrivateKey as ECPrivateKey)
        }
        return jwt.toString()
    }

}