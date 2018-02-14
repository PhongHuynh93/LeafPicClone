package example.test.phong.leafpicclone.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import example.test.phong.leafpicclone.data.sort.SortingMode
import example.test.phong.leafpicclone.util.StringUtils
import example.test.phong.leafpicclone.util.whenNull

/**
 * Created by user on 2/14/2018.
 */
class HandlingAlbums(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 12
        private val DATABASE_NAME = "folders.db"
        private val TABLE_ALBUMS = "folders"

        val EXCLUDED = 1
        val INCLUDED = 2

        private val ALBUM_PATH = "path"
        private val ALBUM_ID = "id"
        private val ALBUM_PINNED = "pinned"
        private val ALBUM_COVER_PATH = "cover_path"
        private val ALBUM_STATUS = "status"
        private val ALBUM_SORTING_MODE = "sorting_mode"
        private val ALBUM_SORTING_ORDER = "sorting_order"

        private var mInstance: HandlingAlbums? = null

        fun getInstance(context: Context): HandlingAlbums {
            mInstance.whenNull {
                synchronized(HandlingAlbums::class.java) {
                    mInstance.whenNull {
                        mInstance = HandlingAlbums(context)
                    }
                }
            }
            return mInstance!!
        }


        fun getSettings(db: SQLiteDatabase, path: String): AlbumSettings {
            var cursor: Cursor? =
                    null
            try {
                if (exist(db, path)) {
                    cursor = db.query(
                            TABLE_ALBUMS,
                            StringUtils.asArray(
                                    ALBUM_COVER_PATH,
                                    ALBUM_SORTING_MODE,
                                    ALBUM_SORTING_ORDER,
                                    ALBUM_PINNED),
                            ALBUM_PATH + "=?",
                            arrayOf(path), null, null, null)

                    if (cursor!!.moveToFirst())
                        return AlbumSettings(
                                cursor.getString(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getInt(3))
                } else
                    db.insert(
                            TABLE_ALBUMS, null,
                            getDefaults(path))

                return AlbumSettings.getDefaults()
            } finally {
                if (cursor != null)
                    cursor.close()
            }
        }


        fun getDefaults(path: String): ContentValues {
            val values = ContentValues()
            values.put(ALBUM_PATH, path)
            values.put(ALBUM_PINNED, 0)
            values.put(ALBUM_SORTING_MODE, SortingMode.DATE.value)
            values.put(ALBUM_SORTING_ORDER, SortingOrder.DESCENDING.value)
            values.put(ALBUM_ID, -1)
            return values
        }


        private fun exist(db: SQLiteDatabase, path: String): Boolean {
            val cur = db.rawQuery(
                    String.format("SELECT EXISTS(SELECT 1 FROM %s WHERE %s=? LIMIT 1)", TABLE_ALBUMS, ALBUM_PATH),
                    arrayOf(path))
            val tracked = cur.moveToFirst() && cur.getInt(0) == 1
            cur.close()
            return tracked
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " +
                TABLE_ALBUMS + "(" +
                ALBUM_PATH + " TEXT," +
                ALBUM_ID + " INTEGER," +
                ALBUM_PINNED + " INTEGER," +
                ALBUM_COVER_PATH + " TEXT, " +
                ALBUM_STATUS + " INTEGER, " +
                ALBUM_SORTING_MODE + " INTEGER, " +
                ALBUM_SORTING_ORDER + " INTEGER)")

        db.execSQL(String.format("CREATE UNIQUE INDEX idx_path ON %s (%s)", TABLE_ALBUMS, ALBUM_PATH))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS)
        db.execSQL("DROP INDEX IF EXISTS idx_path")
        onCreate(db)
    }

}