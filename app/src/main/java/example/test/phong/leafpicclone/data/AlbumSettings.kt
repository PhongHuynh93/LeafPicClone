package example.test.phong.leafpicclone.data

/**
 * Created by user on 2/14/2018.
 */
class AlbumSettings(var coverPath: String? = null, var sortingMode: Int, var sortingOrder: Int, var pinned: Boolean) {
    constructor(cover: String? = null, sortingMode: Int, sortingOrder: Int, pinned: Int): this(cover, sortingMode, sortingOrder, pinned == 1)

    companion object {
        fun getDefaults(): AlbumSettings {
            return AlbumSettings(sortingMode = SortingMode.DATE.value, sortingOrder = 1, pinned = 0)
        }
    }
}