package example.test.phong.leafpicclone.fragments


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.view.animation.OvershootInterpolator
import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.data.Album
import example.test.phong.leafpicclone.data.local.Prefs
import example.test.phong.leafpicclone.util.GridSpacingItemDecoration
import example.test.phong.leafpicclone.util.Measure
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.fragment_albums.*
import org.horaapps.liz.ThemeHelper
/**
 * A simple [Fragment] subclass.
 */
class AlbumsFragment : BaseFragment() {

    companion object {
        fun newInstance() = AlbumsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // todo set toolbar
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_albums, container, false)
        // todo declare rcv and adapter


        rv.apply {
            setHasFixedSize(true)
            addItemDecoration(GridSpacingItemDecoration(columnCounts(), Measure.pxToDp(3, context), true))
            layoutManager = GridLayoutManager(context, columnCounts())
            itemAnimator = LandingAnimator(OvershootInterpolator(1f))
        }

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo display albums
        displayAlbums()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setupColumns()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
//      todo  inflate menu
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun displayAlbums() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        // todo call the presenter to display album
    }

    private fun setupColumns() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // depend on the orientation, show different count
    private fun columnCounts(): Int {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            Prefs.getFolderColumnsPortrait()
        else
            Prefs.getFolderColumnsLandscape()    }

    fun setListener(listener: AlbumClickListener) {

    }

    override fun refreshTheme(themeHelper: ThemeHelper?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}// Required empty public constructor

interface AlbumClickListener {
    fun onAlbumClick(album: Album)
}
