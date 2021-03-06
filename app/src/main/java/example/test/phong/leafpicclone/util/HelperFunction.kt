package example.test.phong.leafpicclone.util

import android.support.annotation.AnimRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

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


fun AppCompatActivity.addFragment(fragmentNew: Fragment, tag: String? = null, @IdRes containerViewId: Int) {
    var fragmentOld = supportFragmentManager.findFragmentByTag(tag)
    fragmentOld.whenNull {
        supportFragmentManager
                .beginTransaction()
                .add(containerViewId, fragmentNew, tag)
                .commit()
    }
}

inline fun <T> Observable<T>.workBgDoneMain(crossinline f: (t: T) -> Unit) {
    subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<T> {
                override fun accept(t: T) {
                    f(t)
                }
            })
}

//Observable
fun <T> Observable<T>.applyIoScheduler() = applyScheduler(Schedulers.io())
fun <T> Observable<T>.applyComputationScheduler() = applyScheduler(Schedulers.computation())
private fun <T> Observable<T>.applyScheduler(scheduler: Scheduler) =
        subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

//Single
fun <T> Single<T>.applyIoScheduler() = applyScheduler(Schedulers.io())
fun <T> Single<T>.applyComputationScheduler() = applyScheduler(Schedulers.computation())
private fun <T> Single<T>.applyScheduler(scheduler: Scheduler) =
        subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

//Completable
//..



