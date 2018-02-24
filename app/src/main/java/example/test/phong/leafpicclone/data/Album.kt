package example.test.phong.leafpicclone.data

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Parcelable
import android.provider.MediaStore
import com.bumptech.glide.signature.ObjectKey
import example.test.phong.leafpicclone.util.StringUtils
import kotlinx.android.parcel.Parcelize

/**
 * Created by user on 2/8/2018.
 */
@Parcelize
@SuppressLint("ParcelCreator")
data class Album @JvmOverloads constructor(var name: String? = null, val path: String? = null, var id: Long = -1, var dateModifier: Long = -1, var count: Int = -1,
                                           var selected: Boolean = false, var settings: AlbumSettings? = null, var lastMedia: Media? = null) : CursorHandler<Album>, Parcelable {

    constructor(cur: Cursor) : this(
            path = StringUtils.getBucketPathByImagePath(cur.getString(3)),
            name = cur.getString(1),
            id = cur.getLong(0),
            count = cur.getInt(2),
            dateModifier = cur.getLong(4),
            lastMedia = Media(cur.getString(3)))

    companion object {
        val ALL_MEDIA_ALBUM_ID: Long = 8000

        fun getEmptyAlbum(): Album {
            val album = Album(name = null, path = null, settings = AlbumSettings.getDefaults())
            return album
        }

        fun getProjection(): Array<String> {
            return arrayOf(MediaStore.Files.FileColumns.PARENT, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "count(*)", MediaStore.Images.Media.DATA, "max(" + MediaStore.Images.Media.DATE_MODIFIED + ")")
        }

        fun getAllMediaAlbum(): Album {
            val album = Album(name = "All Media", id = ALL_MEDIA_ALBUM_ID)
            album.settings = AlbumSettings.getDefaults()
            return album
        }
    }

    fun getCover(): Media {
        if (hasCover())
            return Media(path = settings?.coverPath)
        return lastMedia ?: Media()
        // TODO: 11/20/16 how should i handle this?
    }

    private fun hasCover(): Boolean {
        return settings?.coverPath != null
    }

    fun toggleSelected(): Boolean {
        selected = selected.not()
        return selected
    }

    fun setSelected(selected: Boolean): Boolean {
        if (this.selected == selected)
            return false
        this.selected = selected
        return true
    }

    fun withSettings(settings: AlbumSettings): Album {
        this.settings = settings
        return this
    }

    override fun handle(cu: Cursor): Album {
        return Album(cu)
    }

    override fun getProjection(): Array<String> {
        return arrayOf(MediaStore.Files.FileColumns.PARENT, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "count(*)", MediaStore.Images.Media.DATA, "max(" + MediaStore.Images.Media.DATE_MODIFIED + ")")
    }

    fun isPinned(): Boolean = settings?.coverPath != null
}

@SuppressLint("ParcelCreator")
@Parcelize
data class Media @JvmOverloads constructor(var path: String? = null, var dateModifier: Long = -1, var mimeType: String = "unknow", var orientation: Int = 0,
                                           var uriString: String? = null, var size: Int = -1, var selected: Boolean = false) : Parcelable {
    fun getSignature(): ObjectKey {
        return ObjectKey(dateModifier.toString() + path + orientation)
    }
}

