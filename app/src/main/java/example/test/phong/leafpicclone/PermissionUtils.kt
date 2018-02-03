package example.test.phong.leafpicclone

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by user on 2/3/2018.
 */
class PermissionUtils {

    // object in kotlin mean singleton
    companion object {
        // and then a will exist as a real Java static function, accessible from both java and kotlin as Foo.a(), not contains companion when calling from java file
        @JvmStatic
        fun checkPermissions(context: Context, vararg permissions: String): Boolean {
            for (permission in permissions) {
                if (!checkPermission(context, permission)) return false
            }
            return true
        }

        @JvmStatic
        private fun checkPermission(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        @JvmStatic
        fun isStoragePermissionGranted(context: Context): Boolean {
            return checkPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        @JvmStatic
        fun requestPermissions(o: Any, permissionId: Int, vararg permissions: String) {
            if (o is Activity) {
                ActivityCompat.requestPermissions(o, permissions, permissionId)
            }
        }
    }
}