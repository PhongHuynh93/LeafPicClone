package example.test.phong.leafpicclone.data

import com.bumptech.glide.signature.ObjectKey

/**
 * Created by user on 2/8/2018.
 */
data class Album @JvmOverloads constructor(var name: String, val path: String, var id: Long = -1, var dateModifier: Long = -1, var count: Int = -1,
                                           var selected: Boolean = false, var settings: AlbumsSetting? = null, var lastMedia: Media? = null) {
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

}

data class AlbumsSetting(var coverPath: String? = null, var sortingMode: Int = -1, var sortingOrder: Int = -1, var pinned: Boolean = false, var filterMode: FilterMode = FilterMode.ALL) {

}


data class Media @JvmOverloads constructor(var path: String? = null, var dateModifier: Long = -1, var mimeType: String = "unknow", var orientation: Int = 0,
                                           var uriString: String? = null, var size: Int = -1, var selected: Boolean = false) {
    fun getSignature(): ObjectKey {
        return ObjectKey(dateModifier.toString() + path + orientation)
    }
}

