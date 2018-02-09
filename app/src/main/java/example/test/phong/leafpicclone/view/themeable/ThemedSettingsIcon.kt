package example.test.phong.leafpicclone.view.themeable

import android.content.Context
import android.util.AttributeSet
import org.horaapps.liz.ThemeHelper
import org.horaapps.liz.Themed
import org.horaapps.liz.ui.ThemedIcon

/**
 * Created by Huỳnh Đức Thanh Phong on 2/9/2018.
 */
class ThemedSettingsIcon @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ThemedIcon(context, attrs, defStyleAttr), Themed {

    override fun refreshTheme(themeHelper: ThemeHelper) {
        setColor(themeHelper.iconColor)
    }
}
