package example.test.phong.leafpicclone.activities

import android.os.Bundle
import android.view.View
import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.activities.base.SharedMediaActivity
import example.test.phong.leafpicclone.data.Album
import example.test.phong.leafpicclone.fragments.AlbumClickListener
import example.test.phong.leafpicclone.fragments.AlbumsFragment
import example.test.phong.leafpicclone.fragments.EditModeListener
import example.test.phong.leafpicclone.fragments.NothingToShowListener
import example.test.phong.leafpicclone.util.replaceFragmentSafely
import example.test.phong.leafpicclone.util.whenNull
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class MainActivity : SharedMediaActivity(), AnkoLogger, AlbumClickListener, EditModeListener, NothingToShowListener {
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
                        setListener(this@MainActivity)
                        setEditModeListener(this@MainActivity)
                        setNothingToShowListener(this@MainActivity)
                    }
            replaceFragmentSafely(albumsFragment, containerViewId = R.id.content)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
