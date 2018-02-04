package example.test.phong.leafpicclone.view

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.Animation

/**
 * Created by user on 2/4/2018.
 */
class BallPulseIndicator : Indicator() {
    companion object {
        val SCALE = 1f
    }

    // there are 3 numb of ball so 3 scale
    var scaleFloats = arrayListOf<Float>(SCALE, SCALE, SCALE)

    private val NUMB_BALL = 3
    override fun draw(canvas: Canvas, paint: Paint) {
        val circleSpacing = 4f
        // radius must depend on size of width and height
        val radius = (Math.min(mWidth, mHeight) - circleSpacing * 2) / 6f
        val x = mWidth / 2f - radius * 2f - circleSpacing
        val y = mHeight / 2f
        // draw 3 circle with the scale in the array
        for (i in 0 until NUMB_BALL) {
            canvas.save()
            val translateX = x + (radius * 2 + circleSpacing) * i
            canvas.translate(translateX, y)
            canvas.scale(scaleFloats[i], scaleFloats[i])
            canvas.drawCircle(0f, 0f, radius, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimators(): List<ValueAnimator> {
        val listAnimators = ArrayList<ValueAnimator>()
        val delays = arrayListOf<Long>(120, 240, 360)

        // create 3 animator run parallel but with different delay
        for (i in 0 until NUMB_BALL) {
            val animator: ValueAnimator = ValueAnimator.ofFloat(1f, 0.3f, 1f)
            animator.duration = 750
            animator.repeatCount = Animation.INFINITE
            animator.startDelay = delays[i]

            addUpdateListener(animator, ValueAnimator.AnimatorUpdateListener {
                scaleFloats[i] = it.animatedValue as Float
                postInvalidate()
            })
            listAnimators.add(animator)
        }
        return listAnimators
    }
}