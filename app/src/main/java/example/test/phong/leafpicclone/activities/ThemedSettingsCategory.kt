package example.test.phong.leafpicclone.activities

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import org.horaapps.liz.ThemeHelper
import org.horaapps.liz.Themed

/**
 * Created by Huỳnh Đức Thanh Phong on 2/9/2018.
 */
class ThemedSettingsCategory : AppCompatTextView, Themed {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun refreshTheme(themeHelper: ThemeHelper) {
        themeHelper.setTextViewColor(this, themeHelper.accentColor)
    }
}