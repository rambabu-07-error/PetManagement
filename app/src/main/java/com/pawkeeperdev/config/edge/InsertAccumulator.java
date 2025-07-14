package com.pawkeeperdev.config.edge;

public class InsertAccumulator {
    private final int initialTop;
    private final int insertTop;
    private final int initialBottom;
    private final int insertBottom;
    private final int initialLeft;
    private final int insertLeft;
    private final int initialRight;
    private final int insertRight;

    public InsertAccumulator(int initialTop, int insertTop, int initialBottom, int insertBottom,
                             int initialLeft, int insertLeft, int initialRight, int insertRight) {
        this.initialTop = initialTop;
        this.insertTop = insertTop;
        this.initialBottom = initialBottom;
        this.insertBottom = insertBottom;
        this.initialLeft = initialLeft;
        this.insertLeft = insertLeft;
        this.initialRight = initialRight;
        this.insertRight = insertRight;
    }

    public int getTop() {
        return initialTop + insertTop;
    }

    public int getBottom() {
        return initialBottom + insertBottom;
    }

    public int getLeft() {
        return initialLeft + insertLeft;
    }

    public int getRight() {
        return initialRight + insertRight;
    }
}
