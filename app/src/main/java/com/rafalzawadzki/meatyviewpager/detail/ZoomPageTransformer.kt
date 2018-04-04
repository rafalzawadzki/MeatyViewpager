package com.rafalzawadzki.meatyviewpager.detail

import android.support.v4.view.ViewPager
import android.view.View
import com.rafalzawadzki.meatyviewpager.core.Util.mapRange
import com.rafalzawadzki.meatyviewpager.core.views.RoundedFrameLayout

class ZoomPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        val absolutePosition = Math.abs(position)

        if (absolutePosition > 1) return

        when {
            (position <= -0.5) -> /* (-1, -0.5] */ makeBig(absolutePosition, view)
            (position <= 0) -> /* (-0.5, 0] */ makeSmall(absolutePosition, view)
            (position <= 0.5) -> /* (0, 0.5] */ makeSmall(absolutePosition, view)
            (position <= 1) -> /* (0.5, 1] */ makeBig(absolutePosition, view)
        }
    }

    fun makeBig(absolutePosition: Float, view: View) {
        val scaleFactor = Math.max(MIN_SCALE, absolutePosition)
        setScale(scaleFactor, view)

        val cornerRadius = mapRange(MIN_SCALE.rangeTo(1f), MAX_RADIUS.rangeTo(0f), scaleFactor)
        setCornerRadius(cornerRadius, view)
    }

    fun makeSmall(absolutePosition: Float, view: View) {
        val scaleFactor = Math.max(MIN_SCALE, 1 - absolutePosition)
        setScale(scaleFactor, view)

        val cornerRadius = mapRange(MIN_SCALE.rangeTo(1f), MAX_RADIUS.rangeTo(0f), scaleFactor)
        setCornerRadius(cornerRadius, view)
    }

    private fun setScale(scale: Float, view: View) {
        view.scaleX = scale
        view.scaleY = scale
    }

    private fun setCornerRadius(cornerRadius: Float, view: View) {
        (view as RoundedFrameLayout).cornerRadius = cornerRadius
    }

    companion object {
        private const val MIN_SCALE = 0.75f
        private const val MAX_RADIUS = 150f
    }
}
