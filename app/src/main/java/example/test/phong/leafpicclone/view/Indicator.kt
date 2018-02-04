package example.test.phong.leafpicclone.view

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import example.test.phong.leafpicclone.App
import example.test.phong.leafpicclone.R

/**
 * Created by user on 2/4/2018.
 *
 *
 */
abstract class Indicator : Drawable(), Animatable {
    val mPaint: Paint = Paint()
    var mAlpha: Int = 255
    var mColor: Int = 0
        set(value) {
//            fixme  find out why the setcolor is wrong here
            mPaint.color = ContextCompat.getColor(App.sAppContext, R.color.colorAccent)
        }
    var mHasAnimators: Boolean = false
    var mAnimators: List<ValueAnimator>? = null
    var mUpdateListeners: HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener> = HashMap()
    lateinit var mDrawBounds: Rect
    var mWidth: Int = 0
        get() {
            return mDrawBounds.width()
        }
    var mHeight: Int = 0
        get() {
            return mDrawBounds.height()
        }

    init {
        mPaint.color = ContextCompat.getColor(App.sAppContext, R.color.colorAccent)
//        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        setDrawBounds(bounds)
    }

    private fun setDrawBounds(bounds: Rect) {
        setDrawBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
    }

    private fun setDrawBounds(left: Int, top: Int, right: Int, bottom: Int) {
        mDrawBounds = Rect(left, top, right, bottom)
    }

    // Kotlin function parameters are final. There is no val or final keyword because that's the default (and can't be changed).
    override fun draw(canvas: Canvas) {
        draw(canvas, mPaint)
    }

    abstract fun draw(canvas: Canvas, paint: Paint)

    override fun setAlpha(alpha: Int) {
        mAlpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isRunning(): Boolean {
        mAnimators?.let {
            for (animator in it) {
                return animator.isRunning
            }
        }
        return false
    }

    override fun start() {
        ensureAnimators()
        mAnimators?.let {
            // If the animators has not ended, do nothing.
            if (isStarted()) return
            startAnimators()
            postInvalidate()
        }
    }

    private fun startAnimators() {
        mAnimators?.let {
            for (animator in it) {
                val updateListener: ValueAnimator.AnimatorUpdateListener? = mUpdateListeners.get(animator)
                //when the animator restart , add the updateListener again because they
                // was removed by animator stop .
                updateListener?.let {
                    animator.addUpdateListener(it)
                }
                animator.start()
            }
        }
    }

    /**
     *  Your should use this to add AnimatorUpdateListener when
     *  create animator , otherwise , animator doesn't work when
     *  the animation r
     * @param updateListenerestart .
     */
    fun addUpdateListener(animator: ValueAnimator, updateListener: ValueAnimator.AnimatorUpdateListener) {
        mUpdateListeners.put(animator, updateListener)
    }

    private fun isStarted(): Boolean {
        mAnimators?.let {
            for (animator in it) {
                return animator.isStarted
            }
        }
        return false
    }

    // prepare animators
    private fun ensureAnimators() {
        if (!mHasAnimators) {
            mAnimators = onCreateAnimators()
            mHasAnimators = true
        }
    }

    abstract fun onCreateAnimators(): List<ValueAnimator>

    override fun stop() {
        stopAnimators()
    }

    private fun stopAnimators() {
        mAnimators?.let {
            for (animator in it) {
                animator?.let {
                    if (it.isStarted) {
                        it.removeAllUpdateListeners()
                        it.end()
                    }
                }
            }
        }
    }

    fun postInvalidate() {
        invalidateSelf()
    }
}