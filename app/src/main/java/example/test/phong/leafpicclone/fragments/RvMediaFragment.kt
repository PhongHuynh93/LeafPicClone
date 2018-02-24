package example.test.phong.leafpicclone.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.data.Album
import org.horaapps.liz.ThemeHelper


/**
 * A simple [Fragment] subclass.
 */
class RvMediaFragment : BaseFragment() {
    var clickListener: AlbumClickListener? = null

    companion object {
        val TAG = "RvMediaFragment"
        private val BUNDLE_ALBUM = "album"

        fun make(album: Album): RvMediaFragment = RvMediaFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_ALBUM, album)
            arguments = bundle
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rv_media, container, false)
    }

    override fun refreshTheme(themeHelper: ThemeHelper?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editMode(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearSelected(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}// Required empty public constructor
