package example.test.phong.leafpicclone.fragments

import android.view.View

/**
 * Created by user on 2/8/2018.
 */
interface EditModeListener {
    fun changedEditMode(editMode: Boolean, selected: Int, total: Int, listener: View.OnClickListener?, title: String?)
}