package example.test.phong.leafpicclone.data.filter

import java.io.File
import java.io.FilenameFilter
import java.util.regex.Pattern

/**
 * Created by user on 2/14/2018.
 */
class ImageFileFilter(includeVideo: Boolean): FilenameFilter {
    private var pattern: Pattern

    // info - get filter all file by case insensitive
    init {
        pattern = if (includeVideo)
            Pattern.compile(".(jpg|png|gif|jpe|jpeg|bmp|webp|mp4|mkv|webm|avi)$", Pattern.CASE_INSENSITIVE)
        else
            Pattern.compile(".(jpg|png|gif|jpe|jpeg|bmp|webp)$", Pattern.CASE_INSENSITIVE)
    }

    override fun accept(dir: File?, filename: String?): Boolean {
        return File(dir, filename).isFile && pattern.matcher(filename).find()
    }
}