package example.test.phong.leafpicclone.util

import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View

/**
 * Created by user on 2/13/2018.
 */
@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("showHideRefresh")
fun showHideRefresh(view: SwipeRefreshLayout, show: Boolean) {
    view.isRefreshing = if (show) true else false
}
