package example.test.phong.leafpicclone.data.filter

import java.io.File
import java.io.FileFilter

/**
 * Created by user on 2/14/2018.
 */
class FoldersFileFilter: FileFilter {
    override fun accept(pathname: File): Boolean {
        return pathname.isDirectory()
    }
}