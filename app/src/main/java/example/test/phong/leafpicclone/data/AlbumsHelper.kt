package example.test.phong.leafpicclone.data

import example.test.phong.leafpicclone.data.local.Prefs

/**
 * Created by user on 2/14/2018.
 */
class AlbumsHelper {
    companion object {
        fun getSortingMode(): SortingMode {
            return Prefs.getAlbumSortingMode()
        }

        fun getSortingOrder(): SortingOrder {
            return Prefs.getAlbumSortingOrder()
        }
    }
}