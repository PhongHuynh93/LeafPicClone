package example.test.phong.leafpicclone.view.themeable

import android.content.Context
import android.util.AttributeSet
import android.widget.Toolbar
import org.horaapps.liz.ThemeHelper
import org.horaapps.liz.Themed

/**
 * Created by user on 2/7/2018.
 */
class ThemedToolbar: Toolbar, Themed {
    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun refreshTheme(themeHelper: ThemeHelper) {
        setBackgroundColor(themeHelper.primaryColor)
    }
}