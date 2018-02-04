package example.test.phong.leafpicclone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import org.horaapps.liz.ColorPalette

/**
 * Created by user on 2/3/2018.
 */
class SplashScreen : AppCompatActivity() {
    private val EXTERNAL_STORAGE_PERMISSIONS = 12

    companion object {
        val ACTION_OPEN_ALBUM = "example.test.phong.leafpicclone.OPEN_ALBUM"
    }

    val mColorSystem: Int by lazy {
        ColorPalette.getTransparentColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), 70)
    }

    var mPickmode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // make screen full screen
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setNavBarColor()
        setStatusBarColor()

//        mPickmode = intent.action.equals(Intent.ACTION_GET_CONTENT) || intent.action.equals(Intent.ACTION_PICK)
        // check permission here
        if (PermissionUtils.isStoragePermissionGranted(this)) {
            // open album activity
            if (intent.action.equals(ACTION_OPEN_ALBUM)) {
                startAlbumScreen()
            } else startAlbumScreen()
        } else {
            PermissionUtils.requestPermissions(this, EXTERNAL_STORAGE_PERMISSIONS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun startAlbumScreen() {
//        startActivity<MainActivity>()
//        finish()
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = mColorSystem
        }
    }

    private fun setNavBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = mColorSystem
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            EXTERNAL_STORAGE_PERMISSIONS -> {
                var gotPermission = grantResults.size > 0

                for (result in grantResults) {
                    gotPermission = gotPermission and (result == PackageManager.PERMISSION_GRANTED)
                }

                if (gotPermission) startAlbumScreen()
                else {
                    Toast.makeText(this, getString(R.string.storage_permission_denied), Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}