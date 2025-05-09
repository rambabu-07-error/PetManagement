package com.pawkeeperdev.config

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView


class TouchEffectListener : View.OnTouchListener {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                v?.let {
                    when (it) {
                        is ImageView -> {
                            it.background?.colorFilter = BlendModeColorFilter(
                                0x77000000,
                                BlendMode.SRC_ATOP
                            )
                            it.drawable?.colorFilter = BlendModeColorFilter(
                                0x77000000,
                                BlendMode.SRC_ATOP
                            )
                        }

                        else -> {
                            it.background?.colorFilter = BlendModeColorFilter(
                                0x77000000,
                                BlendMode.SRC_ATOP
                            )
                        }
                    }
                    it.invalidate()
                }
                return false
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                v?.let {
                    when (it) {
                        is ImageView -> {
                            it.background?.clearColorFilter()
                            it.drawable?.clearColorFilter()
                        }

                        else -> {
                            it.background?.clearColorFilter()
                        }
                    }
                    it.invalidate()
                }
            }
        }
        return false
    }
}
