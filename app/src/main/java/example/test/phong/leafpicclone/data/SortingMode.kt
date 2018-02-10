package example.test.phong.leafpicclone.data

import android.provider.MediaStore

/**
 * Created by user on 2/10/2018.
 */
enum class SortingMode @JvmOverloads constructor(var value: Int, var mediaColumn: String, var albumsColumn: String = mediaColumn){
    NAME(0, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME),
    DATE(1, MediaStore.MediaColumns.DATE_MODIFIED, "max(" + MediaStore.Images.Media.DATE_MODIFIED + ")"),
    SIZE(2, MediaStore.MediaColumns.SIZE, "count(*)"),
    TYPE(3, MediaStore.MediaColumns.MIME_TYPE),
    NUMERIC(4, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);

    fun fromValue(value: Int): SortingMode {
        when (value) {
            0 -> return NAME
            1 -> return DATE
            2 -> return SIZE
            3 -> return TYPE
            4 -> return NUMERIC
            else -> return DATE
        }
    }
}