package lydia.yuan.dajavu

import android.app.Application
import lydia.yuan.dajavu.network.AppContainer
import lydia.yuan.dajavu.network.DefaultAppContainer
import lydia.yuan.dajavu.utils.KeyStoreUtils


class MyApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}