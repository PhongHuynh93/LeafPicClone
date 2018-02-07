package example.test.phong.leafpicclone.util

import android.support.annotation.AnimRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by user on 2/8/2018.
 */
inline fun Any?.whenNull(f: () -> Unit) {
    if (this == null) {
        f()
    }
}

inline fun Any?.whenNotNull(f: () -> Unit) {
    this?.let {
        f()
    }
}

fun AppCompatActivity.replaceFragmentSafely(fragment: Fragment,
                                                   tag: String? = null,
                                                   allowStateLoss: Boolean = false,
                                                   @IdRes containerViewId: Int,
                                                   @AnimRes enterAnimation: Int = 0,
                                                   @AnimRes exitAnimation: Int = 0,
                                                   @AnimRes popEnterAnimation: Int = 0,
                                                   @AnimRes popExitAnimation: Int = 0) {
    val ft = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            .addToBackStack(null)
            .replace(containerViewId, fragment, tag)
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}


