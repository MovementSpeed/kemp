package com.kemp.android.input

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.kemp.core.input.TouchInput
import com.kemp.core.input.TouchPointer
import com.kemp.core.input.TouchPointerState
import com.kemp.core.utils.Float2
import com.kemp.core.utils.Pool

@SuppressLint("ClickableViewAccessibility")
class AndroidTouchInput(view: View) : TouchInput {
    private val pointers = mutableMapOf<Int, TouchPointer>()

    init {
        val maxPointers = 10
        repeat(maxPointers) { pointer ->
            pointers[pointer] = TouchPointer(pointer, 0f, 0f, 0f, 0f, TouchPointerState.UP)
        }

        val gestureDetectorCompat = GestureDetectorCompat(view.context, object : GestureDetector.OnGestureListener {
            override fun onDown(e: MotionEvent?): Boolean {
                val pointerCount = e?.pointerCount ?: 1
                repeat(pointerCount) { pointer ->
                    val pid = e?.getPointerId(pointer) ?: 0
                    val px = e?.getX(pid) ?: 0f
                    val py = e?.getY(pid) ?: 0f
                    pointer(pid, px, py, TouchPointerState.DOWN)
                }

                return true
            }

            override fun onShowPress(e: MotionEvent?) {
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                val pointerCount = e?.pointerCount ?: 1
                repeat(pointerCount) { pointer ->
                    val pid = e?.getPointerId(pointer) ?: 0
                    val px = e?.getX(pid) ?: 0f
                    val py = e?.getY(pid) ?: 0f
                    pointer(pid, px, py, TouchPointerState.UP)
                }

                return true
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return true
            }
        })

        view.setOnTouchListener { _, event ->
            gestureDetectorCompat.onTouchEvent(event)
            return@setOnTouchListener true
        }
    }

    override fun pointer(index: Int, x: Float, y: Float, state: TouchPointerState) {
        val pointer = pointers[index]!!
        pointer.oldX = pointer.x
        pointer.oldY = pointer.y
        pointer.x = x
        pointer.y = y
        pointer.state = state
    }

    override fun pointer(index: Int): Float2 {
        val pointer = pointers[index]!!
        val v = Pool.useFloat2()
        v.x = pointer.x
        v.y = pointer.y
        return v
    }

    override fun pointerVelocity(index: Int): Float2 {
        val pointer = pointers[index]!!
        return pointer.velocity
    }
}