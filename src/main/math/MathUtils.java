package main.math;

import java.awt.Color;
import java.util.Random;

public class MathUtils {
    private static Random RANDOM = new Random();

    public static int randomInt(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static Color randomColor() {
        int hue = RANDOM.nextInt(360);
        int saturation = randomInt(160, 255);
        int brightness = randomInt(180, 255);

        return Color.getHSBColor(hue / 360.0f, saturation / 255.0f, brightness / 255.0f);
    }


}
