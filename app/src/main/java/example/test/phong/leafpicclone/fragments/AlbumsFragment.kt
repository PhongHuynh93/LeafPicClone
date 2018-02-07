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
class AlbumsFragment : BaseFragment() {

    companion object {
        fun newInstance() = AlbumsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    fun setListener(listener: AlbumClickListener) {

    }

    override fun refreshTheme(themeHelper: ThemeHelper?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}// Required empty public constructor

interface AlbumClickListener {
    fun onAlbumClick(album: Album)
}
