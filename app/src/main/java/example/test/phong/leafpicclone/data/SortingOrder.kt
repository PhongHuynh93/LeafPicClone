package example.test.phong.leafpicclone.data

/**
 * Created by user on 2/10/2018.
 */
enum class SortingOrder constructor(var value: Int) {
    ASCENDING(1), DESCENDING(0);

    fun isAscending(): Boolean {
        return value == ASCENDING.value
    }

    fun fromValue(value: Boolean): SortingOrder {
        return if (value) ASCENDING else DESCENDING
    }

    fun fromValue(value: Int): SortingOrder {
        return if (value == 0) DESCENDING else ASCENDING
    }
}