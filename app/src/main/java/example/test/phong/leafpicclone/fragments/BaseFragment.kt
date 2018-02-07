package example.test.phong.leafpicclone.fragments

import org.horaapps.liz.Themed
import org.horaapps.liz.ThemedFragment

/**
 * Created by user on 2/8/2018.
 */
open abstract class BaseFragment : ThemedFragment(), Themed {
    private var editModeListener: EditModeListener? = null
    private var nothingToShowListener: NothingToShowListener? = null

    fun setEditModeListener(listener: EditModeListener) {
        this.editModeListener = listener

    }

    fun setNothingToShowListener(listener: NothingToShowListener) {
        this.nothingToShowListener = listener

    }

}