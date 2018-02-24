package example.test.phong.leafpicclone.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import android.widget.Toast
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.orhanobut.hawk.Hawk
import example.test.phong.leafpicclone.BuildConfig
import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.activities.base.SharedMediaActivity
import example.test.phong.leafpicclone.data.Album
import example.test.phong.leafpicclone.databinding.ActivityMainBinding
import example.test.phong.leafpicclone.fragments.*
import example.test.phong.leafpicclone.util.Security
import example.test.phong.leafpicclone.util.addFragment
import example.test.phong.leafpicclone.util.replaceFragmentSafely
import example.test.phong.leafpicclone.util.whenNull
import example.test.phong.leafpicclone.view.navigation_drawer.NavigationDrawer
import example.test.phong.leafpicclone.view.themeable.ThemedToolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class MainActivity : SharedMediaActivity(), AnkoLogger, AlbumClickListener, EditModeListener, NothingToShowListener, NavigationDrawer.ItemListener {
    private lateinit var mBinding: ActivityMainBinding
    private var albumsMode: Boolean = false
    private lateinit var albumsFragment: AlbumsFragment

    companion object {
        val ARGS_PICK_MODE = "pick_mode"
        private val SAVE_ALBUM_MODE = "album_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        initUi()

        // todo pickmode

        // todo add fragment album
        savedInstanceState.whenNull {
            debug { "add fragment album" }
            albumsFragment = AlbumsFragment.newInstance()
                    .apply {
                        clickListener = this@MainActivity
                        editModeListener = this@MainActivity
                        nothingToShowListener = this@MainActivity
                    }
            addFragment(albumsFragment, containerViewId = R.id.content)
            return
        }

        // restore to state
        restoreState(savedInstanceState!!)

        if (!albumsMode) {
            rvMediaFragment = supportFragmentManager.findFragmentByTag(RvMediaFragment.TAG) as RvMediaFragment
            rvMediaFragment.apply {
                clickListener = this@MainActivity
                editModeListener = this@MainActivity
                nothingToShowListener = this@MainActivity
            }
        }

        albumsFragment = supportFragmentManager.findFragmentByTag(AlbumsFragment.TAG) as AlbumsFragment
        albumsFragment.apply {
            clickListener = this@MainActivity
            editModeListener = this@MainActivity
            nothingToShowListener = this@MainActivity
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVE_ALBUM_MODE, albumsMode)
    }

    private fun restoreState(savedInstance: Bundle) {
        albumsMode = savedInstance.getBoolean(SAVE_ALBUM_MODE, true)
    }

    override fun changedNothingToShow(isLoading: Boolean) {
        mBinding.isLoading = isLoading
    }

    override fun changedEditMode(editMode: Boolean, selected: Int, total: Int, listener: View.OnClickListener?, title: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAlbumClick(album: Album) {
        displayMedia(album)
    }

    private lateinit var rvMediaFragment: RvMediaFragment

    private fun displayMedia(album: Album) {
        rvMediaFragment = RvMediaFragment.make(album).apply {
            clickListener = this@MainActivity
            editModeListener = this@MainActivity
            nothingToShowListener = this@MainActivity
        }

        albumsMode = false
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        replaceFragmentSafely(rvMediaFragment, containerViewId = R.id.content)
    }

    private fun initUi() {
        setSupportActionBar(toolbar as ThemedToolbar)
        setupNavigationDrawer()
        setupFAB()
    }

    private fun setupFAB() {
        fab_camera.setImageDrawable(IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_camera_alt).color(Color.WHITE))
        fab_camera.setOnClickListener({ v -> startActivity(Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)) })
    }

    private fun setupNavigationDrawer() {
        val drawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar as ThemedToolbar,
                R.string.drawer_open, R.string.drawer_close)

        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        home_navigation_drawer.itemListener = this
        home_navigation_drawer.setAppVersion(BuildConfig.VERSION_NAME)
    }

    // : 2/24/2018 when choosing a item in navigation drawer
    override fun onItemSelected(@NavigationDrawer.NavigationItem navigationItemSelected: Int) {
        closeDrawer()
        when (navigationItemSelected) {

            NavigationDrawer.NAVIGATION_ITEM_ALL_ALBUMS -> displayAlbums(false)

            NavigationDrawer.NAVIGATION_ITEM_ALL_MEDIA -> displayMedia(Album.getAllMediaAlbum())

            NavigationDrawer.NAVIGATION_ITEM_HIDDEN_FOLDERS -> if (Security.isPasswordOnHidden) {
                askPassword()
            } else {
                displayAlbums(true)
            }

            NavigationDrawer.NAVIGATION_ITEM_WALLPAPERS -> Toast.makeText(this@MainActivity, "Coming Soon!", Toast.LENGTH_SHORT).show()

            NavigationDrawer.NAVIGATION_ITEM_DONATE -> DonateActivity.startActivity(this)

            NavigationDrawer.NAVIGATION_ITEM_SETTINGS -> SettingsActivity.startActivity(this)

            NavigationDrawer.NAVIGATION_ITEM_ABOUT -> AboutActivity.startActivity(this)
        }
    }

    private fun askPassword() {
        Security.authenticateUser(this@MainActivity, object : Security.AuthCallBack {
            override fun onAuthenticated() {
                closeDrawer()
                displayAlbums(true)
            }

            override fun onError() {
                Toast.makeText(applicationContext, R.string.wrong_password, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayAlbums(hidden: Boolean) {
        albumsMode = true
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        albumsFragment.displayAlbums(hidden)
    }

    private fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun updateUiElements() {
        super.updateUiElements()
        toolbar.popupTheme = popupToolbarStyle
        toolbar.setBackgroundColor(primaryColor)

        setStatusBarColor()
        setNavBarColor()

        fab_camera.backgroundTintList = ColorStateList.valueOf(accentColor)
        fab_camera.visibility = if (Hawk.get(getString(R.string.preference_show_fab), false)) View.VISIBLE else View.GONE
        coordinator_main_layout.setBackgroundColor(backgroundColor)

        setScrollViewColor(home_navigation_drawer)

        home_navigation_drawer.setTheme(primaryColor, backgroundColor, textColor, iconColor)

        setRecentApp(getString(R.string.app_name))
    }
}
