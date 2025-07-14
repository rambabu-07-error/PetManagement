package com.pawkeeperdev.config.edge;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.pawkeeperdev.R;

public class EdgeToEdgeExtensions {

    // Define an interface for the block lambda
    public interface ApplyInsetsBlock {
        void apply(MarginLayoutParams params, InsertAccumulator accumulator);
    }

    public static void applyEdgeToEdgeInsets(
            View view,
            int typeMask,
            boolean propagateInsets,
            ApplyInsetsBlock block
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(typeMask);

            Integer initialTopTag = (Integer) v.getTag(R.id.initial_margin_top);
            int initialTop;
            if (initialTopTag != null) {
                initialTop = initialTopTag;
            } else {
                initialTop = ((MarginLayoutParams) v.getLayoutParams()).topMargin;
                v.setTag(R.id.initial_margin_top, initialTop);
            }

            Integer initialBottomTag = (Integer) v.getTag(R.id.initial_margin_bottom);
            int initialBottom;
            if (initialBottomTag != null) {
                initialBottom = initialBottomTag;
            } else {
                initialBottom = ((MarginLayoutParams) v.getLayoutParams()).bottomMargin;
                v.setTag(R.id.initial_margin_bottom, initialBottom);
            }

            Integer initialLeftTag = (Integer) v.getTag(R.id.initial_margin_left);
            int initialLeft;
            if (initialLeftTag != null) {
                initialLeft = initialLeftTag;
            } else {
                initialLeft = ((MarginLayoutParams) v.getLayoutParams()).leftMargin;
                v.setTag(R.id.initial_margin_left, initialLeft);
            }

            Integer initialRightTag = (Integer) v.getTag(R.id.initial_margin_right);
            int initialRight;
            if (initialRightTag != null) {
                initialRight = initialRightTag;
            } else {
                initialRight = ((MarginLayoutParams) v.getLayoutParams()).rightMargin;
                v.setTag(R.id.initial_margin_right, initialRight);
            }

            InsertAccumulator accumulate = new InsertAccumulator(
                    initialTop,
                    insets.top,
                    initialBottom,
                    insets.bottom,
                    initialLeft,
                    insets.left,
                    initialRight,
                    insets.right
            );

            MarginLayoutParams params = (MarginLayoutParams) v.getLayoutParams();
            block.apply(params, accumulate);
            v.setLayoutParams(params);

            return propagateInsets ? windowInsets : WindowInsetsCompat.CONSUMED;
        });
    }

    // Overloaded method with default values for typeMask and propagateInsets
    public static void applyEdgeToEdgeInsets(View view, ApplyInsetsBlock block) {
        applyEdgeToEdgeInsets(
                view,
                WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars() | WindowInsetsCompat.Type.ime(),
                false,
                block
        );
    }


    public static void applyTopAndSideInsets(View view, int typeMask, boolean propagateInsets) {
        applyEdgeToEdgeInsets(view, typeMask, propagateInsets, (params, insets) -> {
            params.leftMargin = insets.getLeft();
            params.rightMargin = insets.getRight();
            params.topMargin = insets.getTop();
        });
    }

    // Overloaded method with default values
    public static void applyTopAndSideInsets(View view) {
        applyTopAndSideInsets(
                view,
                WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars() | WindowInsetsCompat.Type.ime(),
                false
        );
    }

    public static void applySideInsets(View view, int typeMask, boolean propagateInsets) {
        applyEdgeToEdgeInsets(view, typeMask, propagateInsets, (params, insets) -> {
            params.leftMargin = insets.getLeft();
            params.rightMargin = insets.getRight();
        });
    }

    // Overloaded method with default values
    public static void applySideInsets(View view) {
        applySideInsets(
                view,
                WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars() | WindowInsetsCompat.Type.ime(),
                false
        );
    }


    public static void applyBottomAndSideInsets(View view, int typeMask, boolean propagateInsets) {
        applyEdgeToEdgeInsets(view, typeMask, propagateInsets, (params, insets) -> {
            params.leftMargin = insets.getLeft();
            params.rightMargin = insets.getRight();
            params.bottomMargin = insets.getBottom();
        });
    }

    // Overloaded method with default values
    public static void applyBottomAndSideInsets(View view) {
        applyBottomAndSideInsets(
                view,
                WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars() | WindowInsetsCompat.Type.ime(),
                false
        );
    }
}
