package example.test.phong.leafpicclone.view.themeable

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import org.horaapps.liz.ThemeHelper
import org.horaapps.liz.Themed

/**
 * Created by Huỳnh Đức Thanh Phong on 2/9/2018.
 */
class ThemedCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr), Themed {
    override fun refreshTheme(themeHelper: ThemeHelper) {
        setCardBackgroundColor(themeHelper.cardBackgroundColor)
    }
}