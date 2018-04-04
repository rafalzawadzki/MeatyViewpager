package com.rafalzawadzki.meatyviewpager.core

object Util {

    fun mapRange(range1: ClosedRange<Float>, range2: ClosedRange<Float>, value: Float): Float {
        if (value !in range1) throw IllegalArgumentException("value is not within the first range")
        if (range1.endInclusive == range1.start) throw IllegalArgumentException("first range cannot be single-valued")
        return range2.start + (value - range1.start) * (range2.endInclusive - range2.start) / (range1.endInclusive - range1.start)
    }

}