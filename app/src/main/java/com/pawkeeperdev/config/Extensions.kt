package com.pawkeeperdev.config

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.pawkeeperdev.R

fun View.applyEdgeToEdgeInsets(
    typeMask: Int = WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.ime(),
    propagateInserts: Boolean = false,
    block: MarginLayoutParams.(InsertAccumulator) -> Unit
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(typeMask)

        val initialTop = if (view.getTag(R.id.initial_margin_top) != null) {
            view.getTag(R.id.initial_margin_top) as Int
        } else {
            view.setTag(R.id.initial_margin_top, view.marginTop)
            view.marginTop
        }
        val initialBottom = if (view.getTag(R.id.initial_margin_bottom) != null) {
            view.getTag(R.id.initial_margin_bottom) as Int
        } else {
            view.setTag(R.id.initial_margin_bottom, view.marginBottom)
            view.marginBottom
        }
        val initialLeft = if (view.getTag(R.id.initial_margin_left) != null) {
            view.getTag(R.id.initial_margin_left) as Int
        } else {
            view.setTag(R.id.initial_margin_left, view.marginLeft)
            view.marginLeft
        }
        val initialRight = if (view.getTag(R.id.initial_margin_right) != null) {
            view.getTag(R.id.initial_margin_right) as Int
        } else {
            view.setTag(R.id.initial_margin_right, view.marginRight)
            view.marginRight
        }

        val accumulate = InsertAccumulator(
            initialTop = initialTop,
            insertTop = insets.top,
            initialBottom = initialBottom,
            insertBottom = insets.bottom,
            initialLeft = initialLeft,
            insertLeft = insets.left,
            initialRight = initialRight,
            insertRight = insets.right,
        )

        view.updateLayoutParams<MarginLayoutParams> {
            block(accumulate)
        }
        if (propagateInserts) windowInsets else WindowInsetsCompat.CONSUMED
    }
}

fun View.applyTopAndSideInsets(
    typeMask: Int = WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.ime(),
    propagateInserts: Boolean = false,
) = applyEdgeToEdgeInsets { insets ->
    leftMargin = insets.left
    rightMargin = insets.right
    topMargin = insets.top
}

fun View.applySideInsets(
    typeMask: Int = WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.ime(),
    propagateInserts: Boolean = false,
) = applyEdgeToEdgeInsets { insets ->
    leftMargin = insets.left
    rightMargin = insets.right
}

fun View.applyBottomAndSideInsets(
    typeMask: Int = WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.ime(),
    propagateInserts: Boolean = false,
) = applyEdgeToEdgeInsets { insets ->
    leftMargin = insets.left
    rightMargin = insets.right
    bottomMargin = insets.bottom
}


data class InsertAccumulator(
    private val initialTop: Int,
    private val insertTop: Int,
    private val initialBottom: Int,
    private val insertBottom: Int,
    private val initialLeft: Int,
    private val insertLeft: Int,
    private val initialRight: Int,
    private val insertRight: Int,
) {
    val top: Int get() = initialTop + insertTop
    val bottom: Int get() = initialBottom + insertBottom
    val left: Int get() = initialLeft + insertLeft
    val right: Int get() = initialRight + insertRight
}