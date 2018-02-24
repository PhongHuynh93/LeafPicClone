package example.test.phong.leafpicclone.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
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
import example.test.phong.leafpicclone.fragments.AlbumClickListener
import example.test.phong.leafpicclone.fragments.AlbumsFragment
import example.test.phong.leafpicclone.fragments.EditModeListener
import example.test.phong.leafpicclone.fragments.NothingToShowListener
import example.test.phong.leafpicclone.util.Security
import example.test.phong.leafpicclone.util.addFragment
import example.test.phong.leafpicclone.util.whenNull
import example.test.phong.leafpicclone.view.navigation_drawer.NavigationDrawer
import example.test.phong.leafpicclone.view.themeable.ThemedToolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class MainActivity : SharedMediaActivity(), AnkoLogger, AlbumClickListener, EditModeListener, NothingToShowListener, NavigationDrawer.ItemListener {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        initUi()

        // todo pickmode

        // todo add fragment album
        savedInstanceState.whenNull {
            debug { "add fragment album" }
            val albumsFragment = AlbumsFragment.newInstance()
                    .apply {
                        clickListener = this@MainActivity
                        editModeListener = this@MainActivity
                        nothingToShowListener = this@MainActivity
                    }
            addFragment(albumsFragment, containerViewId = R.id.content)
            return
        }

        // todo restore to state

        // todo add fragment media
    }

    //    fixme should change this name
    override fun changedNothingToShow(isLoading: Boolean) {
        mBinding.isLoading = isLoading
    }

    override fun changedEditMode(editMode: Boolean, selected: Int, total: Int, listener: View.OnClickListener?, title: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAlbumClick(album: Album) {
        displayMedia(album)
    }

    private fun displayMedia(album: Album) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    // // TODO: 2/24/2018 when choosing a item in navigation drawer
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun displayAlbums(b: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun closeDrawer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUiElements() {
        super.updateUiElements()
        //TODO: MUST BE FIXED
        toolbar.popupTheme = popupToolbarStyle
        toolbar.setBackgroundColor(primaryColor)

        /**** SWIPE TO REFRESH ****/

        setStatusBarColor()
        setNavBarColor()

        fab_camera.backgroundTintList = ColorStateList.valueOf(accentColor)
        fab_camera.visibility = if (Hawk.get(getString(R.string.preference_show_fab), false)) View.VISIBLE else View.GONE
        coordinator_main_layout.setBackgroundColor(backgroundColor)

        setScrollViewColor(home_navigation_drawer)

        home_navigation_drawer.setTheme(primaryColor, backgroundColor, textColor, iconColor)

        // TODO Calvin: This performs a NO-OP. Find out what this is used for
        setRecentApp(getString(R.string.app_name))
    }
}
