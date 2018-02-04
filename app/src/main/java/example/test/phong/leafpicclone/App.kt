package example.test.phong.leafpicclone

import android.app.Application
import android.content.Context

/**
 * Created by user on 2/4/2018.
 */
class App : Application() {
    companion object {
        lateinit var sAppContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        sAppContext = this
    }
}