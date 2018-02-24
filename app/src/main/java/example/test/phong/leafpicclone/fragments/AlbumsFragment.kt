package example.test.phong.leafpicclone.fragments


import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.view.animation.OvershootInterpolator
import com.orhanobut.hawk.Hawk
import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.adapter.AlbumsAdapter
import example.test.phong.leafpicclone.data.Album
import example.test.phong.leafpicclone.data.AlbumsHelper
import example.test.phong.leafpicclone.data.HandlingAlbums
import example.test.phong.leafpicclone.data.SortingOrder
import example.test.phong.leafpicclone.data.local.Prefs
import example.test.phong.leafpicclone.data.provider.CPHelper
import example.test.phong.leafpicclone.data.sort.SortingMode
import example.test.phong.leafpicclone.databinding.FragmentAlbumsBinding
import example.test.phong.leafpicclone.util.GridSpacingItemDecoration
import example.test.phong.leafpicclone.util.Measure
import example.test.phong.leafpicclone.util.applyIoScheduler
import example.test.phong.leafpicclone.util.workBgDoneMain
import io.reactivex.rxkotlin.subscribeBy
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.fragment_albums.*
import org.horaapps.liz.ThemeHelper
import org.jetbrains.anko.AnkoLogger
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AlbumsFragment : BaseFragment(), AnkoLogger {
    companion object {
        val TAG = "AlbumsFragment"

        fun newInstance() = AlbumsFragment()
    }

    private var hidden: Boolean = false
    private var excluded = ArrayList<String>()
    private var mAdapter: AlbumsAdapter? = null
    var clickListener: AlbumClickListener? = null
    private lateinit var mBinding: FragmentAlbumsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        excluded = db().getExcludedFolders(context!!)
    }

    private fun db(): HandlingAlbums {
        return HandlingAlbums.getInstance(context!!.getApplicationContext())
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate<FragmentAlbumsBinding>(inflater, R.layout.fragment_albums, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = AlbumsAdapter(context!!, sortingMode(), sortingOrder()).apply {
            onClickSubject.workBgDoneMain {
                clickListener?.onAlbumClick(it)
            }

            onChangeSelectedSubject.workBgDoneMain {
                swipe_refresh.isEnabled = !this.selecting()
                updateToolbar()
                activity?.invalidateOptionsMenu()
            }
        }
        rv.apply {
            setHasFixedSize(true)
            addItemDecoration(GridSpacingItemDecoration(columnCounts(), Measure.pxToDp(3, context), true))
            layoutManager = GridLayoutManager(context, columnCounts())
            itemAnimator = LandingAnimator(OvershootInterpolator(1f))
            this.adapter = mAdapter
        }

        swipe_refresh.setOnRefreshListener(this::displayAlbums)
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

    private fun updateToolbar() {
        editModeListener?.let {
            if (editMode()) {
                it.changedEditMode(true, mAdapter!!.selectedCount, mAdapter!!.itemCount, View.OnClickListener {
                    mAdapter?.clearSelected()
                }, null)
            } else {
                it.changedEditMode(false, 0, 0, null, null)
            }
        }
    }

    override fun clearSelected(): Boolean = mAdapter?.clearSelected() ?: false

    override fun editMode(): Boolean = mAdapter?.selecting() ?: false

    private fun sortingOrder(): SortingOrder {
        return mAdapter?.sortingOrder ?: AlbumsHelper.getSortingOrder()
    }

    private fun sortingMode(): SortingMode {
        return mAdapter?.sortingMode ?: AlbumsHelper.getSortingMode()
    }

    private fun displayAlbums() {
        val db = HandlingAlbums.getInstance(context!!.applicationContext).readableDatabase
        CPHelper.getAlbums(context!!, hidden, excluded, sortingMode(), sortingOrder())
                .map { album -> album.withSettings(HandlingAlbums.getSettings(db, album.path!!)) }
                .applyIoScheduler()
                .doOnSubscribe {
                    mAdapter?.clear()
                    mBinding.isRefreshing = true
                }
                .doFinally {
                    mBinding.isRefreshing = false
                    db.close()
                    nothingToShowListener?.changedNothingToShow(getCount() == 0)
                }
                .subscribeBy(
                        onNext = { mAdapter?.add(it) },
                        onError = { it.printStackTrace() },
                        onComplete = { Hawk.put(if (hidden) "h" else "albums", mAdapter?.getAlbumsPaths()) })
    }

    fun displayAlbums(hidden: Boolean) {
        this.hidden = hidden
        displayAlbums()
    }

    private fun getCount(): Int = mAdapter?.itemCount ?: 0

    private fun setupColumns() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // depend on the orientation, show different count
    private fun columnCounts(): Int {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            Prefs.getFolderColumnsPortrait()
        else
            Prefs.getFolderColumnsLandscape()
    }

    override fun refreshTheme(t: ThemeHelper) {
        rv.setBackgroundColor(t.getBackgroundColor())
        mAdapter?.refreshTheme(t)
        swipe_refresh.setColorSchemeColors(t.getAccentColor())
        swipe_refresh.setProgressBackgroundColorSchemeColor(t.getBackgroundColor())
    }
}// Required empty public constructor

interface AlbumClickListener {
    fun onAlbumClick(album: Album)
}
