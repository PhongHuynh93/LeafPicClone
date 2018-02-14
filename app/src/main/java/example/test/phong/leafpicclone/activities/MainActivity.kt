package example.test.phong.leafpicclone.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import example.test.phong.leafpicclone.BuildConfig
import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.activities.base.SharedMediaActivity
import example.test.phong.leafpicclone.data.Album
import example.test.phong.leafpicclone.fragments.AlbumClickListener
import example.test.phong.leafpicclone.fragments.AlbumsFragment
import example.test.phong.leafpicclone.fragments.EditModeListener
import example.test.phong.leafpicclone.fragments.NothingToShowListener
import example.test.phong.leafpicclone.util.addFragment
import example.test.phong.leafpicclone.util.whenNull
import example.test.phong.leafpicclone.view.navigation_drawer.NavigationDrawer
import example.test.phong.leafpicclone.view.themeable.ThemedToolbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class MainActivity : SharedMediaActivity(), AnkoLogger, AlbumClickListener, EditModeListener, NothingToShowListener, NavigationDrawer.ItemListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    override fun onPostResume() {
        super.onPostResume()
    }

    override fun changedNothingToShow(nothingToShow: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changedEditMode(editMode: Boolean, selected: Int, total: Int, listener: View.OnClickListener?, title: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAlbumClick(album: Album) {
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

    override fun onItemSelected(navigationItemSelected: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
