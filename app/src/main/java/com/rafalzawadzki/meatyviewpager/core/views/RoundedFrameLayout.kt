package com.rafalzawadzki.meatyviewpager.core.views


import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout

class RoundedFrameLayout
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    var cornerRadius = 0f
        set(value) {
            field = value
            createPath()
            invalidate()
        }

    private var path = Path()
    private var width = 0f
    private var height = 0f

    init {
        setWillNotDraw(false)
    }

    private fun createPath() {
        val r = RectF(0f, 0f, width, height)
        path = Path()
        path.addRoundRect(r, cornerRadius, cornerRadius, Path.Direction.CW)
        path.close()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = w.toFloat()
        height = h.toFloat()
        createPath()
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restore()
    }
}
