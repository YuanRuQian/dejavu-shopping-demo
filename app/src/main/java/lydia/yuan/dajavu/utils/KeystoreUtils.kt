package lydia.yuan.dajavu.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import kotlin.text.Charsets.UTF_8


class KeystoreUtils {

    companion object {

        lateinit var encryptedPairData: Pair<ByteArray, ByteArray>

        val cipherTransformation = "AES/CBC/PKCS7Padding"

        val keyStoreAlias = "token"

        val keyStoreType = "AndroidKeyStore"

        init {
            getKeyGenerator()
        }
        fun encryptWithKeyStore(plainText: String): String {
            encryptedPairData = getEncryptedDataPair(plainText)
            return encryptedPairData.second.toString(UTF_8)
        }

        private fun getKeyGenerator() {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, keyStoreType)
            val keyGeneratorSpec = KeyGenParameterSpec.Builder(
                keyStoreAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(false)
                .build()
            keyGenerator.init(keyGeneratorSpec)
            keyGenerator.generateKey()
        }

        private fun getKey(): SecretKey {
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null)
            val secreteKeyEntry: KeyStore.SecretKeyEntry =
                keyStore.getEntry(keyStoreAlias, null) as KeyStore.SecretKeyEntry
            return secreteKeyEntry.secretKey
        }

        private fun getEncryptedDataPair(data: String): Pair<ByteArray, ByteArray> {
            val cipher = Cipher.getInstance(cipherTransformation)
            cipher.init(Cipher.ENCRYPT_MODE, getKey())

            val iv: ByteArray = cipher.iv
            val encryptedData = cipher.doFinal(data.toByteArray(UTF_8))
            return Pair(iv, encryptedData)
        }

        fun getToken(): String {
            return decryptData(encryptedPairData.first, encryptedPairData.second)
        }

        private fun decryptData(iv: ByteArray, encData: ByteArray): String {
            val cipher = Cipher.getInstance(cipherTransformation)
            val keySpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, getKey(), keySpec)
            return cipher.doFinal(encData).toString(UTF_8)
        }

        fun clearToken() {
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null)
            keyStore.deleteEntry(keyStoreAlias)
        }
    }
}