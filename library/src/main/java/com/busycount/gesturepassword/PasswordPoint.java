package com.busycount.gesturepassword;

import java.util.Objects;

/**
 * PasswordPoint
 * <p>
 * 2018/11/30 | Count.C | Created
 */
public class PasswordPoint {
    int x;
    int y;
    int coordinateX;
    int coordinateY;

    PasswordPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int num(int factor) {
        return y * factor + x;
    }

    boolean isInclude(float x1, float y1, int radius) {
        int dx = (int) Math.abs(x1 - coordinateX);
        int dy = (int) Math.abs(y1 - coordinateY);
        return dx <= radius && dy <= radius && ((dx * dx + dy * dy) <= radius * radius);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordPoint point = (PasswordPoint) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + 37 * y;
    }
}
