package lydia.yuan.dajavu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val searchFragment = SearchFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, searchFragment)
                .commit()
        }
    }

}
