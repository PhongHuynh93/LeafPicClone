package example.test.phong.leafpicclone.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import example.test.phong.leafpicclone.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

/**
 * Created by user on 2/4/2018.
 */
class AVLoadingIndicatorView : View, AnkoLogger {
    var mMinWidth = 24
    var mMaxWidth = 48
    var mMinHeight = 24
    var mMaxHeight = 48
    var mIndicatorColor: Int = 0
    var mIndicator: Indicator? = null
    var mShouldStartAnimationDrawable: Boolean = false
    private var mStartTime: Long = -1
    private var mDismissed: Boolean = false
    private var mPostedShow: Boolean = false
    private var mPostedHide: Boolean = false
    val mDelayedHide = Runnable {
        mPostedHide = false
        mStartTime = -1
        visibility = GONE
    }

    val mDelayedShow = Runnable {
        mPostedShow = false
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis()
            visibility = VISIBLE
        }
    }

    private val MIN_DELAY: Long = 500

    // the default indicator will be used and instance only if we didn't point the indicator name.
    val DEFAULT_INDICATOR: Indicator by lazy {
        BallPulseIndicator()
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, 0, 0)
    }


    private fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AVLoadingIndicatorView, defStyleAttr, defStyleRes)
        mMinWidth = a.getDimensionPixelSize(R.styleable.AVLoadingIndicatorView_minWidth, mMinWidth)
        mMaxWidth = a.getDimensionPixelSize(R.styleable.AVLoadingIndicatorView_maxWidth, mMaxWidth)
        mMinHeight = a.getDimensionPixelSize(R.styleable.AVLoadingIndicatorView_minHeight, mMinHeight)
        mMinHeight = a.getDimensionPixelSize(R.styleable.AVLoadingIndicatorView_maxHeight, mMaxHeight)
        val indicatorName = a.getString(R.styleable.AVLoadingIndicatorView_indicatorName)
        mIndicatorColor = a.getColor(R.styleable.AVLoadingIndicatorView_indicatorColor, Color.WHITE)

        setIndicator(indicatorName)
        if (mIndicator == null) {
            setIndicator(DEFAULT_INDICATOR)
        }
        a.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        removeCallbacks(mDelayedHide)
        removeCallbacks(mDelayedShow)
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        // This should come after stopAnimation(), otherwise an invalidate message remains in the
        // queue, which can prevent the entire view hierarchy from being GC'ed during a rotation
        removeCallbacks()
        super.onDetachedFromWindow()
    }

    /**
     * we implement custom drawable within view, need to implement this method
     * Hotspots are used to pipe touch events into RippleDrawable, but can be used by custom drawables as well. If you are implementing a custom View that manages its own drawables, you will need to call setHotspot() from the drawableHotspotChanged() method for touch-centered ripples to work correctly.
     * <a href="https://stackoverflow.com/questions/26461636/whats-the-purpose-of-drawable-sethotspot-on-android-5-0-api-21">
     */
    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        mIndicator?.let {
            it.setHotspot(x, y)
        }
    }

    override fun invalidateDrawable(drawable: Drawable) {
        if (verifyDrawable(drawable)) {
            val dirty = drawable.getBounds()
            val scrollX = scrollX + paddingLeft
            val scrollY = scrollY + paddingTop

            invalidate(dirty.left + scrollX, dirty.top + scrollY,
                    dirty.right + scrollX, dirty.bottom + scrollY)
        } else super.invalidateDrawable(drawable)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who == mIndicator || super.verifyDrawable(who)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var dw = 0
        var dh = 0
        val d = mIndicator
        d?.let {
            // only allow the width and height is in a range
            dw = Math.max(mMinWidth, Math.min(d.intrinsicWidth, mMaxWidth))
            dh = Math.max(mMinHeight, Math.min(d.intrinsicHeight, mMaxHeight))
        }
        updateDrawableState()
        dw += paddingLeft + paddingRight
        dh += paddingTop + paddingBottom

        // get the correct size depend on the default size and the size which we measured
        val measureWidth = resolveSizeAndState(dw, widthMeasureSpec, 0)
        val measureHeight = resolveSizeAndState(dh, heightMeasureSpec, 0)
        setMeasuredDimension(measureWidth, measureHeight)
    }

    // set state for drawable if the state change
    override fun drawableStateChanged() {
        super.drawableStateChanged()
        updateDrawableState()
    }

    private fun updateDrawableState() {
        val states = drawableState
        mIndicator?.let {
            if (it.isStateful) {
                it.setState(states)
            }
        }
    }

    // we want to keep the aspect if the size is changed
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updateDrawableBound(w, h)
    }

    private fun updateDrawableBound(w: Int, h: Int) {
        // onDraw will translate the canvas so we draw starting at 0,0.
        // Subtract out padding for the purposes of the calculations below.
        var newWidth = w
        var newHeight = h
        newWidth -= paddingLeft + paddingRight
        newHeight -= paddingTop + paddingBottom

        var right = newWidth
        var bottom = newHeight
        var top = 0
        var left = 0

        mIndicator?.let {
            // Maintain aspect ratio. Certain kinds of animated drawables
            // get very confused otherwise.
            val intrinsicWidth = it.intrinsicWidth
            val intrinsicHeight = it.intrinsicHeight
            val intrinsicAspect = intrinsicWidth.toFloat() / intrinsicHeight
            val aspect = newWidth.toFloat() / newHeight
            if (intrinsicAspect < aspect) {
                // need to change the right and left (make the drawable center)
                val finalWidth = (newHeight * intrinsicAspect).toInt()
                left = (newWidth - finalWidth) / 2
                right = left + finalWidth
            } else {
                val finalHeight = (newWidth * (1 / aspect)).toInt()
                // need to change the top and bottom
                top = (newHeight - finalHeight) / 2
                bottom = top + finalHeight
            }
            it.setBounds(left, top, right, bottom)
        }

    }

    /**
     * draw and start animation if not start yet
     * used save and restore to before complex anim
     * <a href="https://stackoverflow.com/questions/29040064/save-canvas-then-restore-why-is-that">
     */
    override fun onDraw(canvas: Canvas) {
        mIndicator?.let {
            // test remove save and restore
            canvas.save()
            // Translate canvas so a indeterminate circular progress bar with padding
            // rotates properly in its animation
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            it.draw(canvas)
            canvas.restore()
            if (mShouldStartAnimationDrawable) {
                it.start()
                mShouldStartAnimationDrawable = false
            }
        }
    }

    // info - start/ stop anim when the visibility change
    override fun setVisibility(visibility: Int) {
        if (visibility != visibility) {
            super.setVisibility(visibility)
            if (visibility == GONE || visibility == INVISIBLE) stopAnimation()
            else startAnimation()
        }
    }

    // very helper to stop anim when app in background
    override fun onVisibilityChanged(changedView: View?, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == GONE || visibility == INVISIBLE) stopAnimation()
        else startAnimation()
    }

    /**
     * depend on the name of class, create the corresponding object.
     *
     * You should pay attention to pass this parameter with two way:
     * for example:
     * 1. Only class Name,like "SimpleIndicator".(This way would use default package name with
     * "com.wang.avi.indicators")
     * 2. Class name with full package,like "com.my.android.indicators.SimpleIndicator".
     * @param indicatorName the class must be extend Indicator .
     */
    private fun setIndicator(indicatorName: String) {
        if (TextUtils.isEmpty(indicatorName)) return
        val drawableClassName: StringBuilder = StringBuilder()
        if (!drawableClassName.contains('.')) {
            drawableClassName.append(this.javaClass.`package`.name)
                    .append('.')
        }
        drawableClassName.append(indicatorName)
        try {
            val drawableClass = Class.forName(drawableClassName.toString())
            val indicator = drawableClass.newInstance() as Indicator
            setIndicator(indicator)
        } catch (e: ClassNotFoundException) {
            warn("Not found class")
            e.printStackTrace()
        } catch (e: InstantiationError) {
            warn(" cannot instance class with name" + drawableClassName)
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    // create the indicator and draw it
    private fun setIndicator(d: Indicator?) {
        if (d != mIndicator) {
            // remember before any new events
            mIndicator?.let {
                it.callback = null
                unscheduleDrawable(it)
            }
            mIndicator = d

            //need to set indicator color again if you didn't specified when you update the indicator .
            setIndicatorColor(mIndicatorColor)
            d?.let {
                d.callback = this
            }
            // redraw this view
            // compare invalidate() and postInvalidate()
            // <a href="https://stackoverflow.com/questions/5521596/what-does-postinvalidate-do">
            postInvalidate()
        }

    }

    /**
     * setIndicatorColor(0xFF00FF00)
     * or
     * setIndicatorColor(Color.BLUE)
     * or
     * setIndicatorColor(Color.parseColor("#FF4081"))
     * or
     * setIndicatorColor(0xFF00FF00)
     * or
     * setIndicatorColor(getResources().getColor(android.R.color.black))
     * @param color
     */
    private fun setIndicatorColor(color: Int) {
        mIndicatorColor = color
        mIndicator?.mColor = color
    }


    // the anim will start in the first draw
    fun startAnimation() {
        if (visibility != VISIBLE) return
        mIndicator?.let {
            mShouldStartAnimationDrawable = true
        }
        postInvalidate()
    }

    fun stopAnimation() {
        mIndicator?.let {
            it.stop()
            mShouldStartAnimationDrawable = false
        }
        postInvalidate()
    }

    fun show() {
        // reset the start time
        mStartTime = -1
        mDismissed = false
        removeCallbacks(mDelayedHide)
        if (!mPostedShow) {
            postDelayed(mDelayedShow, MIN_DELAY)
            mPostedShow = true
        }
    }

    private val MIN_SHOW_TIME: Long = 500

    fun hide() {
        mDismissed = true
        removeCallbacks(mDelayedShow)
        val diff = System.currentTimeMillis() - mStartTime

        if (diff >= MIN_SHOW_TIME || mStartTime == -1L) {
//            / The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            visibility = GONE
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            if (!mPostedHide) {
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff)
                mPostedHide = true
            }
        }
    }

    fun smoothToShow() {
        startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in))
        visibility = VISIBLE
    }

    fun smoothToHide() {
        startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out))
        visibility = GONE
    }
}
