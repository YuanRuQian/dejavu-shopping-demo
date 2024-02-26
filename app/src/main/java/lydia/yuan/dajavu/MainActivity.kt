package lydia.yuan.dajavu

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import lydia.yuan.dajavu.utils.KeystoreUtils

class MainActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = BuildConfig.PLACES_API_KEY

        if (apiKey.isEmpty()) {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)

        if (FirebaseAuth.getInstance().currentUser == null) {
            showSignInPage()
        } else {
            cacheToken()
            showDefaultPage()
        }
    }

    fun signIn(view: android.view.View) {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    Toast.makeText(this, "Welcome, ${currentUser?.displayName}", Toast.LENGTH_SHORT).show()
                    cacheToken()
                } else {
                    Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun cacheToken() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnSuccessListener {
            Log.d("MainActivity", "Token: ${it.token}")
            it.token?.let { it1 ->
                KeystoreUtils.encryptWithKeyStore(it1)
                Log.d("MainActivity", "Token saved to keystore")
                Log.d("MainActivity", "Token retrieved from keystore: ${KeystoreUtils.getToken()}")
            }
            showDefaultPage()
        }
    }

    private fun showSignInPage() {
        setContentView(R.layout.activity_sign_in)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignIn = findViewById(R.id.buttonSignIn)
    }

    private fun showDefaultPage() {
        setContentView(R.layout.activity_main)

        val searchFragment = SearchFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, searchFragment)
            .commit()

        val signOutFab = findViewById<FloatingActionButton>(R.id.signOutFab)
        signOutFab.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut().run { KeystoreUtils.clearToken() }
        showSignInPage()
    }
}
