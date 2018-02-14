package example.test.phong.leafpicclone.fragments

import org.horaapps.liz.Themed
import org.horaapps.liz.ThemedFragment

/**
 * Created by user on 2/8/2018.
 */
abstract class BaseFragment : ThemedFragment(), Themed, IFragment {
    var editModeListener: EditModeListener? = null
    var nothingToShowListener: NothingToShowListener? = null
}