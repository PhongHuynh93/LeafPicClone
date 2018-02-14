package example.test.phong.leafpicclone.data

import android.content.Context
import android.util.Log
import java.io.File
import java.util.*

/**
 * Created by user on 2/14/2018.
 */
class StorageHelper {
    companion object {
        fun getStorageRoots(context: Context): HashSet<File> {
            val paths = HashSet<File>()
            for (file in context.getExternalFilesDirs("external")) {
                if (file != null) {
                    val index = file.absolutePath.lastIndexOf("/Android/data")
                    if (index < 0)
                        Log.w("asd", "Unexpected external file dir: " + file.absolutePath)
                    else
                        paths.add(File(file.absolutePath.substring(0, index)))
                }
            }
            return paths
        }


    }
}