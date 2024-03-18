package lydia.yuan.dajavu

import android.media.session.MediaSession.Token
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import com.google.android.material.floatingactionbutton.FloatingActionButton
import lydia.yuan.dajavu.utils.TokenStore
import lydia.yuan.dajavu.viewmodel.TokenViewModel
import java.security.KeyStore


class MainActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var tokenViewModel: TokenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenStore.init(applicationContext)

        val apiKey = BuildConfig.PLACES_API_KEY

        tokenViewModel = ViewModelProvider(this, TokenViewModel.Factory)[TokenViewModel::class.java]

        if (apiKey.isEmpty()) {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)

        // if (KeystoreUtils.getToken().isEmpty()) {
            // showSignInPage()
       // } else {
             showDefaultPage()
//        }
    }

    fun signIn(view: android.view.View) {
        val email = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()
        tokenViewModel.signIn(email, password, applicationContext)
        showDefaultPage()
    }

    private fun showSignInPage() {
        setContentView(R.layout.activity_sign_in)

        editTextUsername = findViewById(R.id.editTextUsername)
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
        TokenStore.removeToken()
    }
}
