package example.test.phong.leafpicclone.data

import android.annotation.SuppressLint
import android.os.Parcelable
import example.test.phong.leafpicclone.data.sort.SortingMode
import kotlinx.android.parcel.Parcelize

/**
 * Created by user on 2/14/2018.
 */
@SuppressLint("ParcelCreator")
@Parcelize
class AlbumSettings(var coverPath: String? = null, var sortingMode: Int, var sortingOrder: Int, var pinned: Boolean) : Parcelable {
    constructor(cover: String? = null, sortingMode: Int, sortingOrder: Int, pinned: Int): this(cover, sortingMode, sortingOrder, pinned == 1)

    companion object {
        fun getDefaults(): AlbumSettings {
            return AlbumSettings(sortingMode = SortingMode.DATE.value, sortingOrder = 1, pinned = 0)
        }
    }
}