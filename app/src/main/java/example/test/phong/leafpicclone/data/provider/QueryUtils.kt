package example.test.phong.leafpicclone.data.provider

import android.content.ContentResolver
import android.database.Cursor
import example.test.phong.leafpicclone.data.CursorHandler
import io.reactivex.Observable

/**
 * Created by user on 2/14/2018.
 */
class QueryUtils {
    companion object {
        fun <T> query(q: Query, cr: ContentResolver, ch: CursorHandler<T>): Observable<T> {
            return Observable.create { subscriber ->
                var cursor: Cursor? = null
                try {
                    cursor = q.getCursor(cr)
                    if (cursor != null && cursor.count > 0)
                        while (cursor.moveToNext()) subscriber.onNext(ch.handle(cursor))
                    subscriber.onComplete()
                } catch (err: Exception) {
                    subscriber.onError(err)
                } finally {
                    if (cursor != null) cursor.close()
                }
            }
        }

    }
}