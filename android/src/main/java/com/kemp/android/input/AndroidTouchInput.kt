package com.kemp.android.input

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.kemp.core.input.methods.TouchInput
import com.kemp.core.utils.Float2
import com.kemp.core.utils.Pool
import kotlin.math.roundToInt


@SuppressLint("ClickableViewAccessibility")
class AndroidTouchInput(view: View) : TouchInput, View.OnTouchListener {
    private var pointersIds = IntArray(10)
    private var pointersX = IntArray(10)
    private var pointersY = IntArray(10)
    private var pointersDeltaX = IntArray(10)
    private var pointersDeltaY = IntArray(10)

    init {
        view.setOnTouchListener(this)
    }

    override fun update() {
        for (i in pointersDeltaX.indices) {
            pointersDeltaX[i] = 0
            pointersDeltaY[i] = 0
        }
    }

    override fun pointerX(index: Int): Float {
        return pointersX[index].toFloat()
    }

    override fun pointerY(index: Int): Float {
        return pointersY[index].toFloat()
    }

    override fun pointer(index: Int): Float2 {
        val float2 = Pool.useFloat2()
        float2.x = pointersX[index].toFloat()
        float2.y = pointersY[index].toFloat()
        return float2
    }

    override fun pointerDeltaX(index: Int): Float {
        return pointersDeltaX[index].toFloat()
    }

    override fun pointerDeltaY(index: Int): Float {
        return pointersDeltaY[index].toFloat()
    }

    override fun pointerDelta(index: Int): Float2 {
        val float2 = Pool.useFloat2()
        float2.x = pointersDeltaX[index].toFloat()
        float2.y = pointersDeltaY[index].toFloat()
        return float2
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null) return true

        val action = event.action and MotionEvent.ACTION_MASK

        var pointerIndex =
            event.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT

        var pointerId = event.getPointerId(pointerIndex)

        var x: Int
        var y: Int

        var realPointerIndex: Int

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                realPointerIndex = getFreePointerIndex()
                if (realPointerIndex >= 10) return true

                pointersIds[realPointerIndex] = pointerId

                x = event.getX(pointerIndex).roundToInt()
                y = event.getY(pointerIndex).roundToInt()

                pointersX[realPointerIndex] = x
                pointersY[realPointerIndex] = y

                pointersDeltaX[realPointerIndex] = 0
                pointersDeltaY[realPointerIndex] = 0
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_OUTSIDE -> {
                realPointerIndex = lookUpPointerIndex(pointerId)

                if (realPointerIndex == -1) return true
                if (realPointerIndex >= 10) return true

                pointersIds[realPointerIndex] = -1

                x = event.getX(pointerIndex).roundToInt()
                y = event.getY(pointerIndex).roundToInt()

                pointersX[realPointerIndex] = x
                pointersY[realPointerIndex] = y

                pointersDeltaX[realPointerIndex] = 0
                pointersDeltaY[realPointerIndex] = 0
            }

            MotionEvent.ACTION_CANCEL ->
                for (i in pointersIds.indices) {
                    pointersIds[i] = -1

                    pointersX[i] = 0
                    pointersY[i] = 0

                    pointersDeltaX[i] = 0
                    pointersDeltaY[i] = 0
                }

            MotionEvent.ACTION_MOVE -> {
                val pointerCount = event.pointerCount

                for (i in 0 until pointerCount) {
                    pointerIndex = i
                    pointerId = event.getPointerId(pointerIndex)

                    x = event.getX(pointerIndex).roundToInt()
                    y = event.getY(pointerIndex).roundToInt()

                    realPointerIndex = lookUpPointerIndex(pointerId)

                    if (realPointerIndex == -1) continue
                    if (realPointerIndex >= 10) break

                    pointersDeltaX[realPointerIndex] = x - pointersX[realPointerIndex]
                    pointersDeltaY[realPointerIndex] = y - pointersY[realPointerIndex]

                    pointersX[realPointerIndex] = x
                    pointersY[realPointerIndex] = y
                }
            }
        }

        return true
    }

    private fun getFreePointerIndex(): Int {
        val len: Int = pointersIds.size

        for (i in 0 until len) {
            if (pointersIds[i] == -1) return i
        }

        pointersIds = resize(pointersIds)
        pointersX = resize(pointersX)
        pointersY = resize(pointersY)
        pointersDeltaX = resize(pointersDeltaX)
        pointersDeltaY = resize(pointersDeltaY)

        return len
    }

    private fun lookUpPointerIndex(pointerId: Int): Int {
        val len: Int = pointersIds.size

        for (i in 0 until len) {
            if (pointersIds[i] == pointerId) return i
        }

        return -1
    }


    private fun resize(orig: IntArray): IntArray {
        val tmp = IntArray(orig.size + 2)
        System.arraycopy(orig, 0, tmp, 0, orig.size)
        return tmp
    }
}