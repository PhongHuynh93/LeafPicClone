package example.test.phong.leafpicclone.view.navigation_drawer

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.IdRes
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.ScrollView
import example.test.phong.leafpicclone.R
import kotlinx.android.synthetic.main.view_navigation_drawer.view.*

/**
 * Created by user on 2/14/2018.
 */
class NavigationDrawer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ScrollView(context, attrs, defStyleAttr) {
    private var navigationEntries: Array<NavigationEntry>
    var itemListener: ItemListener? = null

    companion object {
        const val NAVIGATION_ITEM_ALL_ALBUMS = 1001
        const val NAVIGATION_ITEM_ALL_MEDIA = 1002
        const val NAVIGATION_ITEM_HIDDEN_FOLDERS = 1003
        const val NAVIGATION_ITEM_WALLPAPERS = 1004
        const val NAVIGATION_ITEM_DONATE = 1005
        const val NAVIGATION_ITEM_SETTINGS = 1006
        const val NAVIGATION_ITEM_ABOUT = 1007
    }

    @IntDef(NAVIGATION_ITEM_ALL_ALBUMS.toLong(), NAVIGATION_ITEM_ALL_MEDIA.toLong(), NAVIGATION_ITEM_HIDDEN_FOLDERS.toLong(), NAVIGATION_ITEM_WALLPAPERS.toLong(), NAVIGATION_ITEM_DONATE.toLong(), NAVIGATION_ITEM_SETTINGS.toLong(), NAVIGATION_ITEM_ABOUT.toLong())
    annotation class NavigationItem

    init {
        setupView()
        LayoutInflater.from(context).inflate(R.layout.view_navigation_drawer, this, true)

        navigationEntries = arrayOf<NavigationEntry>(navigation_item_albums,
                navigation_item_all_media,
                navigation_item_hidden_albums,
                navigation_item_wallpapers,
                navigation_item_donate,
                navigation_item_settings,
                navigation_item_about)
        setupListeners()
    }

    private fun setupListeners() {
        val onClickListener = OnClickListener { v ->
            itemListener?.onItemSelected(getNavigationItemSelected(v.id))
        }

        for (navigationEntry in navigationEntries) {
            navigationEntry.setOnClickListener(onClickListener)
        }
    }

    @NavigationItem
    private fun getNavigationItemSelected(@IdRes viewId: Int): Int {
        when (viewId) {
            R.id.navigation_item_albums -> return NAVIGATION_ITEM_ALL_ALBUMS
            R.id.navigation_item_all_media -> return NAVIGATION_ITEM_ALL_MEDIA
            R.id.navigation_item_hidden_albums -> return NAVIGATION_ITEM_HIDDEN_FOLDERS
            R.id.navigation_item_wallpapers -> return NAVIGATION_ITEM_WALLPAPERS
            R.id.navigation_item_donate -> return NAVIGATION_ITEM_DONATE
            R.id.navigation_item_settings -> return NAVIGATION_ITEM_SETTINGS
            R.id.navigation_item_about -> return NAVIGATION_ITEM_ABOUT
        }
        return NAVIGATION_ITEM_ABOUT
    }

    private fun setupView() {
        val scrollBarSize = resources.getDimensionPixelOffset(R.dimen.nav_drawer_scrollbar_size)
        setScrollBarSize(scrollBarSize)
    }

    /**
     * Interface for clients to listen to item selections.
     */
    interface ItemListener {

        fun onItemSelected(@NavigationItem navigationItemSelected: Int)
    }

    fun setAppVersion(versioN_NAME: String) {
        navigation_drawer_header_version.text = versioN_NAME
    }

    fun setTheme(@ColorInt primaryColor: Int, @ColorInt backgroundColor: Int,
                 @ColorInt textColor: Int, @ColorInt iconColor: Int) {

        setBackgroundColor(backgroundColor)
        navigation_drawer_header.setBackgroundColor(primaryColor)

        for (navigationEntry in navigationEntries) {
            navigationEntry.setTextColor(textColor)
            navigationEntry.setIconColor(iconColor)
        }
    }
}