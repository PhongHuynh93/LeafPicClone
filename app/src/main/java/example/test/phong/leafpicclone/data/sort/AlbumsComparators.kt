package example.test.phong.leafpicclone.data.sort

import example.test.phong.leafpicclone.data.Album
import example.test.phong.leafpicclone.data.SortingOrder
import example.test.phong.leafpicclone.util.NumericComparator
import kotlin.Comparator

/**
 * Created by user on 2/14/2018.
 */
class AlbumsComparators {
    companion object {
        fun getComparator(sortingMode: SortingMode, sortingOrder: SortingOrder): Comparator<Album> {

            val comparator = getComparator(sortingMode, getBaseComparator(sortingOrder))

            return if (sortingOrder === SortingOrder.ASCENDING)
                comparator
            else
                reverse(comparator)
        }

        fun getComparator(sortingMode: SortingMode, base: Comparator<Album>): Comparator<Album> {
            when (sortingMode) {
                SortingMode.NAME -> return getNameComparator(base)
                SortingMode.SIZE -> return getSizeComparator(base)
                SortingMode.DATE -> return getDateComparator(base)
                SortingMode.NUMERIC -> return getNumericComparator(base)
                else -> return getDateComparator(base)
            }
        }

        private fun getBaseComparator(sortingOrder: SortingOrder): Comparator<Album> {
            return if (sortingOrder === SortingOrder.ASCENDING)
                getPinned()
            else
                getReversedPinned()
        }

        private fun getPinned(): Comparator<Album> {
            return Comparator { o1, o2 ->
                if (o1.isPinned() == o2.isPinned()) return@Comparator 0
                if (o1.isPinned()) -1 else 1
            }
        }

        private fun getReversedPinned(): Comparator<Album> {
            return Comparator { o1, o2 -> getPinned().compare(o2, o1) }
        }

        private fun reverse(comparator: Comparator<Album>): Comparator<Album> {
            return Comparator { o1, o2 -> comparator.compare(o2, o1) }
        }

        private fun getNumericComparator(base: Comparator<Album>): Comparator<Album> {
            return Comparator { a1, a2 ->
                val res = base.compare(a1, a2)
                if (res == 0)
                    return@Comparator NumericComparator.filevercmp(a1.name!!.toLowerCase(), a2.name!!.toLowerCase())
                res
            }
        }

        private fun getDateComparator(base: Comparator<Album>): Comparator<Album> {
            return Comparator { a1, a2 ->
                val res = base.compare(a1, a2)
                if (res == 0)
                    return@Comparator a1.dateModifier.compareTo(a2.dateModifier)
                res
            }
        }

        private fun getSizeComparator(base: Comparator<Album>): Comparator<Album> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        private fun getNameComparator(base: Comparator<Album>): Comparator<Album> {
            return Comparator { a1, a2 ->
                val res = base.compare(a1, a2)
                if (res == 0)
                    return@Comparator a1.name!!.toLowerCase().compareTo(a2.name!!.toLowerCase())
                res
            }
        }
    }
}