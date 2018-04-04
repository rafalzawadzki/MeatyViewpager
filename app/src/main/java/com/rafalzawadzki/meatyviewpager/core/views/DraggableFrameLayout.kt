package com.rafalzawadzki.meatyviewpager.core.views

import android.content.Context
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout


class DraggableFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var dX: Float = 0f
    private var dY: Float = 0f
    private var prevRawX: Float = 0f
    private var prevRawY: Float = 0f
    val dragTreshold: Int = 500

    var closeAction: (() -> Unit)? = null
    var dragAction: ((Float, Float) -> Unit)? = null

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                dX = x - event.rawX
                dY = y - event.rawY

                prevRawX = event.rawX
                prevRawY = event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                val angle = Math.abs(Math.toDegrees(Math.atan2((prevRawY - event.rawY).toDouble(), (event.rawX - prevRawX).toDouble())))
                if (angle > 45 && angle <= 135) {
                    return true
                }
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                dX = x - event.rawX
                dY = y - event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                y = event.rawY + dY
                x = event.rawX + dX
                dragAction?.invoke(x, y)
            }

            MotionEvent.ACTION_UP -> {
                if (y < dragTreshold) {
                    animateBack()
                } else {
                    closeAction?.invoke()
                }
            }

            else -> return false
        }
        return true
    }

    private fun animateBack() {
        animate()
                .y(0f)
                .x(0f)
                .setDuration(500)
                .setInterpolator(FastOutSlowInInterpolator())
                .setUpdateListener { dragAction?.invoke(x, y) }
                .start()
    }


}