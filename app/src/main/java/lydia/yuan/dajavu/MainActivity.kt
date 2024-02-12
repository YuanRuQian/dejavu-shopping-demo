package lydia.yuan.dajavu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.libraries.places.api.Places


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty()) {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        // Initialize the SDK
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)


        if (savedInstanceState == null) {
            val searchFragment = SearchFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit()
        }
    }

}
