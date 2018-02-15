package example.test.phong.leafpicclone.data.provider

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import java.util.*

/**
 * Created by user on 2/14/2018.
 */
class Query {
    var uri: Uri? = null
    var projection: Array<String>? = null
    var selection: String? = null
    var args: Array<String?>? = null
    var sort: String? = null
    var ascending: Boolean = false
    var limit: Int = 0

    constructor(builder: Builder) {
        uri = builder.uri
        projection = builder.projection
        selection = builder.selection
        args = builder.stringArgs
        sort = builder.sort
        ascending = builder.ascending
        limit = builder.limit
    }


    fun getCursor(cr: ContentResolver): Cursor? {
        return cr.query(uri, projection, selection, args, hack())
    }

    private fun hack(): String? {
        if (sort == null && limit == -1) return null

        val builder = StringBuilder()
        if (sort != null)
            builder.append(sort)
        else
            builder.append(1)// Sorting by Relative Position
        // ORDER BY 1
        // sort by the first column in the PROJECTION
        // otherwise the LIMIT should not work

        builder.append(" ")

        if (!ascending)
            builder.append("DESC").append(" ")

        if (limit != -1)
            builder.append("LIMIT").append(" ").append(limit)

        return builder.toString()
    }

    override fun toString(): String {
        return "Query(uri=$uri, projection=${Arrays.toString(projection)}, selection=$selection, args=${Arrays.toString(args)}, sort=$sort, ascending=$ascending, limit=$limit)"
    }

    class Builder {
        internal var uri: Uri? = null
        internal var projection: Array<String>? = null
        internal var selection: String? = null
        internal var args: Array<Any>? = null
        internal var sort: String? = null
        internal var limit = -1
        internal var ascending = false

        val stringArgs: Array<String?>
            get() {
                val list = arrayOfNulls<String>(args!!.size)
                for (i in args!!.indices) list[i] = args!![i].toString()
                return list
            }

        fun uri(`val`: Uri): Builder {
            uri = `val`
            return this
        }

        fun projection(`val`: Array<String>): Builder {
            projection = `val`
            return this
        }

        fun selection(`val`: String): Builder {
            selection = `val`
            return this
        }

        fun args(`val`: Array<Any>): Builder {
            args = `val`
            return this
        }

        fun sort(`val`: String): Builder {
            sort = `val`
            return this
        }

        fun limit(`val`: Int): Builder {
            limit = `val`
            return this
        }

        fun ascending(`val`: Boolean): Builder {
            ascending = `val`
            return this
        }

        fun build(): Query {
            return Query(this)
        }
    }


}
