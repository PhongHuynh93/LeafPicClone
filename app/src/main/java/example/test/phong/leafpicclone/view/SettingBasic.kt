package example.test.phong.leafpicclone.view

import android.content.Context
import android.os.Build
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import example.test.phong.leafpicclone.R
import kotlinx.android.synthetic.main.view_setting_basic.view.*
import org.horaapps.liz.ThemeHelper
import org.horaapps.liz.Themed

/**
 * Created by Huỳnh Đức Thanh Phong on 2/9/2018.
 */
class SettingBasic: FrameLayout, Themed {
    private var iconString: String?
    @StringRes
    private var titleRes: Int
    private var captionRes: Int

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        setBackgroundResource(R.drawable.ripple)

        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.view_setting_basic, this)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingBasic)
        iconString = a.getString(R.styleable.SettingBasic_settingIcon)
        titleRes = a.getResourceId(R.styleable.SettingBasic_settingTitle, 0)
        captionRes = a.getResourceId(R.styleable.SettingBasic_settingCaption, 0)
        val minimumApi = a.getInteger(R.styleable.SettingBasic_settingMinApi, 0)
        a.recycle()

        if (Build.VERSION.SDK_INT < minimumApi) visibility = View.GONE
    }

    override fun onFinishInflate() {
        icon.setIcon(icon.icon.icon(iconString))
        title.setText(titleRes)
        caption.setText(captionRes)
        super.onFinishInflate()
    }

    override fun refreshTheme(themeHelper: ThemeHelper?) {
    }

}