package com.github.pmcompany.pustomario.io;

/**
 * @author dector (dector9@gmail.com)
 */
public class PColor {
    public static final PColor RED = new PColor(1, 0, 0, 0);
    public static final PColor GREEN = new PColor(0, 1, 0, 0);
    public static final PColor BLUE = new PColor(0, 0, 1, 0);
    public static final PColor BLACK = new PColor(0, 0, 0, 0);
    public static final PColor WHITE = new PColor(1, 1, 1, 0);

    private float red;
    private float green;
    private float blue;
    private float alpha;

    public PColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }
}
