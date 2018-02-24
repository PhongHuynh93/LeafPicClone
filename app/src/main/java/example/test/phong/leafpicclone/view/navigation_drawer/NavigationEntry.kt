package example.test.phong.leafpicclone.view.navigation_drawer

import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mikepenz.iconics.IconicsDrawable
import example.test.phong.leafpicclone.R
import kotlinx.android.synthetic.main.view_navigation_entry.view.*

/**
 * Created by user on 2/14/2018.
 */
class NavigationEntry@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        setupView(context)
        LayoutInflater.from(context).inflate(R.layout.view_navigation_entry, this, true)

        attrs?.let {
            setupData(context, attrs!!)
        }
    }

    private fun setupView(context: Context) {
        orientation = LinearLayout.HORIZONTAL
        background = ContextCompat.getDrawable(context, R.drawable.ripple)
        gravity = Gravity.CENTER_VERTICAL

        val verticalPadding = resources.getDimensionPixelOffset(R.dimen.nav_entry_vertical_spacing)
        val horizontalPadding = resources.getDimensionPixelOffset(R.dimen.nav_entry_horizontal_spacing)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
    }

    private fun setupData(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.NavigationEntry)
        val displayText = typedArray.getString(R.styleable.NavigationEntry_itemText)
        val displayIcon = typedArray.getString(R.styleable.NavigationEntry_itemIcon)
        typedArray.recycle()

        setText(displayText)

        if (displayIcon == null) return
        setIcon(displayIcon)
    }

    private fun setText(text: String?) {
        navigation_item_text.setText(text)
    }

    private fun setIcon(iconText: String) {
        navigation_item_icon.setIcon(IconicsDrawable(context, iconText))
    }

    fun setTextColor(@ColorInt colorRes: Int) {
        navigation_item_text.setTextColor(colorRes)
    }

    fun setIconColor(@ColorInt colorRes: Int) {
        navigation_item_icon.setColor(colorRes)
    }
}