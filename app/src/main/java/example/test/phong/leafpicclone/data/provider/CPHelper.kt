package example.test.phong.leafpicclone.data.provider

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.orhanobut.hawk.Hawk
import example.test.phong.leafpicclone.data.Album
import example.test.phong.leafpicclone.data.Media
import example.test.phong.leafpicclone.data.SortingOrder
import example.test.phong.leafpicclone.data.StorageHelper
import example.test.phong.leafpicclone.data.filter.FoldersFileFilter
import example.test.phong.leafpicclone.data.filter.ImageFileFilter
import example.test.phong.leafpicclone.data.local.Prefs
import example.test.phong.leafpicclone.data.sort.SortingMode
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.jetbrains.anko.AnkoLogger
import java.io.File
import java.util.*

/**
 * Created by user on 2/14/2018.
 */
class CPHelper: AnkoLogger {
    companion object {
        fun getAlbums(context: Context, hidden: Boolean, excluded: ArrayList<String>, sortingMode: SortingMode, sortingOrder: SortingOrder): Observable<Album> {
            return if (hidden) getHiddenAlbums(context, excluded) else getAlbums(context, excluded, sortingMode, sortingOrder)
        }

        private fun getHiddenAlbums(context: Context, excludedAlbums: ArrayList<String>): Observable<Album> {
            val includeVideo = Prefs.showVideos()
            return Observable.create { subscriber ->
                try {

                    val lastHidden = Hawk.get("h", ArrayList<String>())
                    for (s in lastHidden)
                        checkAndAddFolder(File(s), subscriber, includeVideo)

                    lastHidden.addAll(excludedAlbums)

                    for (storage in StorageHelper.getStorageRoots(context))
                        fetchRecursivelyHiddenFolder(storage, subscriber, lastHidden, includeVideo)
                    subscriber.onComplete()
                } catch (err: Exception) {
                    subscriber.onError(err)
                }
            }
        }

        private fun fetchRecursivelyHiddenFolder(dir: File, emitter: ObservableEmitter<Album>, excludedAlbums: ArrayList<String>, includeVideo: Boolean) {
            if (!isExcluded(dir.path, excludedAlbums)) {
                val folders = dir.listFiles(FoldersFileFilter())
                if (folders != null) {
                    for (temp in folders!!) {
                        val nomedia = File(temp, ".nomedia")
                        if (!isExcluded(temp.getPath(), excludedAlbums) && (nomedia.exists() || temp.isHidden()))
                            checkAndAddFolder(temp, emitter, includeVideo)

                        fetchRecursivelyHiddenFolder(temp, emitter, excludedAlbums, includeVideo)
                    }
                }
            }
        }

        private fun isExcluded(path: String, excludedAlbums: ArrayList<String>): Boolean {
            for (s in excludedAlbums) if (path.startsWith(s)) return true
            return false
        }


        private fun checkAndAddFolder(dir: File, emitter: ObservableEmitter<Album>, includeVideo: Boolean) {
            val files = dir.listFiles(ImageFileFilter(includeVideo))
            if (files != null && files!!.size > 0) {
                //valid folder

                var lastMod = java.lang.Long.MIN_VALUE
                var choice: File? = null
                for (file in files!!) {
                    if (file.lastModified() > lastMod) {
                        choice = file
                        lastMod = file.lastModified()
                    }
                }
                if (choice != null) {
                    val asd = Album(path = dir.absolutePath, name = dir.name, count = files!!.size, dateModifier = lastMod)
                    asd.lastMedia = Media(choice.absolutePath)
                    emitter.onNext(asd)
                }
            }
        }

        private val TAG: String = CPHelper.javaClass.name

        fun getAlbums(context: Context, excludedAlbums: List<String>, sortingMode: SortingMode, sortingOrder: SortingOrder): Observable<Album> {
            val query = Query.Builder()
                    .uri(MediaStore.Files.getContentUri("external"))
                    .projection(Album.getProjection())
                    .sort(sortingMode.albumsColumn)
                    .ascending(sortingOrder.isAscending())

            // used list because we dont know the size
            val args = ArrayList<Any>()

            if (Prefs.showVideos()) {
                query.selection(String.format("%s=? or %s=?) group by (%s) %s ",
                        MediaStore.Files.FileColumns.MEDIA_TYPE,
                        MediaStore.Files.FileColumns.MEDIA_TYPE,
                        MediaStore.Files.FileColumns.PARENT,
                        getHavingCluause(excludedAlbums.size)))
                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
            } else {
                query.selection(String.format("%s=?) group by (%s) %s ",
                        MediaStore.Files.FileColumns.MEDIA_TYPE,
                        MediaStore.Files.FileColumns.PARENT,
                        getHavingCluause(excludedAlbums.size)))
                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            }

            //NOTE: LIKE params for query
            for (s in excludedAlbums)
                args.add(s + "%")


            query.args(args.toTypedArray())

            Log.e(TAG, "query string " + query.build())
            return QueryUtils.query(query.build(), context.contentResolver, Album())
        }

        private fun getHavingCluause(excludedCount: Int): String {
            if (excludedCount == 0)
                return "("

            val res = StringBuilder()
            res.append("HAVING (")

            res.append(MediaStore.Images.Media.DATA).append(" NOT LIKE ?")

            for (i in 1 until excludedCount)
                res.append(" AND ")
                        .append(MediaStore.Images.Media.DATA)
                        .append(" NOT LIKE ?")

            // NOTE: dont close ths parenthesis it will be closed by ContentResolver
            //res.append(")");

            return res.toString()
        }
    }
}