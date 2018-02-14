package example.test.phong.leafpicclone.data

import android.database.Cursor
import java.sql.SQLException

/**
 * Created by user on 2/14/2018.
 */
interface CursorHandler<T> {
    @Throws(SQLException::class)
    fun handle(cu: Cursor): T

    fun getProjection(): Array<String> {
        return arrayOf()
    }
}