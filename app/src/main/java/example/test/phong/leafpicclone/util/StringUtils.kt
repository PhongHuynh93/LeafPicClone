package example.test.phong.leafpicclone.util

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.webkit.MimeTypeMap
import android.widget.Toast
import org.horaapps.liz.ColorPalette
import java.util.*
import java.util.regex.Pattern

/**
 * Created by user on 2/10/2018.
 */
class StringUtils {
    companion object {
        fun getMimeType(path: String?): String {
            val index: Int? = path?.lastIndexOf('.')
            index?.let {
                if (index != -1) {
                    val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(path.substring(index + 1).toLowerCase())
                    return mime ?: "unknown"
                }
            }
            return "unknown"
        }

        fun asArray(vararg a: String): Array<out String> {
            return a
        }

        fun getGenericMIME(mime: String): String {
            return mime.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + "/*"
        }

        fun getPhotoNameByPath(path: String): String {
            val b = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val fi = b[b.size - 1]
            return fi.substring(0, fi.lastIndexOf('.'))
        }

        fun join(jointChar: String, vararg collection: Any): String {
            var s = ""
            for (o in collection) s += o.toString() + jointChar

            val i = s.lastIndexOf(jointChar)
            if (i != -1)
                s = s.substring(0, i)
            return s
        }

        fun html(s: String?): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY)
            else
                Html.fromHtml(s)
        }

        fun getName(path: String): String {
            val b = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return b[b.size - 1]
        }

        fun getPhotoPathRenamed(olderPath: String, newName: String): String {
            var c = ""
            val b = olderPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (x in 0 until b.size - 1) c += b[x] + "/"
            c += newName
            val name = b[b.size - 1]
            c += name.substring(name.lastIndexOf('.'))
            return c
        }

        fun incrementFileNameSuffix(name: String): String {
            val builder = StringBuilder()

            val dot = name.lastIndexOf('.')
            val baseName = if (dot != -1) name.subSequence(0, dot).toString() else name
            var nameWoSuffix = baseName
            val matcher = Pattern.compile("_\\d").matcher(baseName)
            if (matcher.find()) {
                val i = baseName.lastIndexOf("_")
                if (i != -1) nameWoSuffix = baseName.subSequence(0, i).toString()
            }
            builder.append(nameWoSuffix).append("_").append(Date().time)
            builder.append(name.substring(dot))
            return builder.toString()
        }

        fun getPhotoPathRenamedAlbumChange(olderPath: String, albumNewName: String): String {
            var c = ""
            val b = olderPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (x in 0 until b.size - 2) c += b[x] + "/"
            c += albumNewName + "/" + b[b.size - 1]
            return c
        }

        fun getAlbumPathRenamed(olderPath: String, newName: String): String {
            return olderPath.substring(0, olderPath.lastIndexOf('/')) + "/" + newName
        }

        fun getPhotoPathMoved(olderPath: String, folderPath: String): String {
            val b = olderPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val fi = b[b.size - 1]
            var path = folderPath + "/"
            path += fi
            return path
        }

        fun getBucketPathByImagePath(path: String): String {
            val b = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var c = ""
            for (x in 0 until b.size - 1) c += b[x] + "/"
            c = c.substring(0, c.length - 1)
            return c
        }

        fun showToast(x: Context, s: String) {
            val t = Toast.makeText(x, s, Toast.LENGTH_SHORT)
            t.show()
        }

        fun humanReadableByteCount(bytes: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (bytes < unit) return bytes.toString() + " B"
            val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
            return String.format("%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
        }

        fun b(content: String?): String {
            return String.format(Locale.ENGLISH, "<b>%s</b>", content)
        }

        fun i(content: String?): String {
            return String.format(Locale.ENGLISH, "<i>%s</i>", content)
        }

        fun htmlFormat(content: String?, color: Int, bold: Boolean, italic: Boolean): Spanned {
            var res = content
            if (color != -1) res = color(color, res)
            if (bold) res = b(res)
            if (italic) res = i(res)
            return html(res)
        }

        fun htmlFormat(content: String, hexcolor: String?, bold: Boolean, italic: Boolean): Spanned {
            var res = content
            if (hexcolor != null) res = color(hexcolor, res)
            if (bold) res = b(res)
            if (italic) res = i(res)
            return html(res)
        }

        fun color(color: Int, content: String?): String {
            return String.format(Locale.ENGLISH,
                    "<font color='%s'>%s</font>",
                    ColorPalette.getHexColor(color), content)
        }

        fun color(color: String, content: String?): String {
            return String.format(Locale.ENGLISH,
                    "<font color='%s'>%s</font>",
                    color, content)
        }
    }
}