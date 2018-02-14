package example.test.phong.leafpicclone.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.content.edit
import example.test.phong.leafpicclone.data.CardViewStyle
import example.test.phong.leafpicclone.data.sort.SortingMode
import example.test.phong.leafpicclone.data.SortingOrder

/**
 * Created by user on 2/9/2018.
 */
class Prefs {
    companion object {
        lateinit var sharedPrefs: SharedPrefs

        /**
         * Initialise the Prefs object for future static usage.
         * Make sure to initialise this in Application class.
         *
         * @param context The context to initialise with.
         */
        fun init(context: Context) {
            sharedPrefs = SharedPrefs(context)
        }

        fun getFolderColumnsPortrait(): Int {
            return sharedPrefs.get(Keys.FOLDER_COLUMNS_PORTRAIT, Defaults.FOLDER_COLUMNS_PORTRAIT)
        }

        fun getFolderColumnsLandscape(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        fun getCardStyle(): CardViewStyle {
            return CardViewStyle.fromValue(sharedPrefs.get(Keys.CARD_STYLE, Defaults.CARD_STYLE))
        }

        fun showMediaCount(): Boolean {
            return sharedPrefs.get(Keys.SHOW_MEDIA_COUNT, Defaults.SHOW_MEDIA_COUNT)
        }

        fun showAlbumPath(): Boolean {
            return sharedPrefs.get(Keys.SHOW_ALBUM_PATH, Defaults.SHOW_ALBUM_PATH)
        }

        fun getAlbumSortingMode(): SortingMode {
            return SortingMode.fromValue(sharedPrefs.get(Keys.ALBUM_SORTING_MODE, Defaults.ALBUM_SORTING_MODE))
        }

        fun setAlbumSortingMode(sortingMode: SortingMode) {
            sharedPrefs.put(Keys.ALBUM_SORTING_MODE, sortingMode.value)
        }

        fun getAlbumSortingOrder(): SortingOrder {
            return SortingOrder.fromValue(sharedPrefs.get(Keys.ALBUM_SORTING_ORDER, Defaults.ALBUM_SORTING_ORDER))
        }

        fun showVideos(): Boolean {
            return sharedPrefs.get(Keys.SHOW_VIDEOS, Defaults.SHOW_VIDEOS)
        }
    }
}

private class Defaults {
    companion object {
        val FOLDER_COLUMNS_PORTRAIT = 2
        val FOLDER_COLUMNS_LANDSCAPE = 3

        val MEDIA_COLUMNS_PORTRAIT = 3
        val MEDIA_COLUMNS_LANDSCAPE = 4

        val ALBUM_SORTING_MODE = SortingMode.DATE.value
        val ALBUM_SORTING_ORDER = SortingOrder.DESCENDING.value
        val CARD_STYLE = CardViewStyle.MATERIAL.value

        val SHOW_VIDEOS = true
        val SHOW_MEDIA_COUNT = true
        val SHOW_ALBUM_PATH = false

        val LAST_VERSION_CODE = 0
        val SHOW_EASTER_EGG = false
    }
}

private class Keys {
    companion object {
        val FOLDER_COLUMNS_PORTRAIT = "folder_columns_portrait"
        val FOLDER_COLUMNS_LANDSCAPE = "folder_columns_landscape"

        val MEDIA_COLUMNS_PORTRAIT = "media_columns_portrait"
        val MEDIA_COLUMNS_LANDSCAPE = "media_columns_landscape"

        val ALBUM_SORTING_MODE = "album_sorting_mode"
        val ALBUM_SORTING_ORDER = "album_sorting_order"

        val SHOW_VIDEOS = "show_videos"
        val SHOW_MEDIA_COUNT = "show_media_count"
        val SHOW_ALBUM_PATH = "show_album_path"

        val CARD_STYLE = "card_style"

        val LAST_VERSION_CODE = "last_version_code"
        val SHOW_EASTER_EGG = "show_easter_egg"
    }
}

class SharedPrefs(var context: Context) {
    private val PREFERENCES_NAME = "example.test.phong.leafpicclone.SHARED_PREFS"
    private val PREFERENCES_MODE = Context.MODE_PRIVATE

    lateinit var sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.applicationContext
                .getSharedPreferences(PREFERENCES_NAME, PREFERENCES_MODE)
    }

    fun get(key: String, defaultValue: Int): Int {
        return sharedPrefs.getInt(key, defaultValue)
    }

    fun get(key: String, defaultValue: Boolean): Boolean {
        return sharedPrefs.getBoolean(key, defaultValue)
    }

    fun put(key: String, value: Int) {
        sharedPrefs.edit {
            putInt(key, value)
        }
    }

    fun put(key: String, value: Boolean) {
        sharedPrefs.edit {
            putBoolean(key, value)
        }
    }
}
