package example.test.phong.leafpicclone

import android.app.Application
import android.content.Context
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.Iconics
import com.orhanobut.hawk.Hawk
import example.test.phong.leafpicclone.data.local.Prefs

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
        registerFontIcons()
        initialiseStorage()
    }

    private fun registerFontIcons() {
        Iconics.registerFont(GoogleMaterial())
        Iconics.registerFont(CommunityMaterial())
        Iconics.registerFont(FontAwesome())
    }

    private fun initialiseStorage() {
        Prefs.init(this)
        Hawk.init(this).build()
    }
}