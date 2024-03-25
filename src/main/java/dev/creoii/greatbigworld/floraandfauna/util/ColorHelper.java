package dev.creoii.greatbigworld.floraandfauna.util;

public final class ColorHelper {
    public static int add(int color, int r, int g, int b) {
        int red = red(color);
        int green = green(color);
        int blue = blue(color);

        red = Math.min(red + r, 255);
        green = Math.min(green + g, 255);
        blue = Math.min(blue + b, 255);

        return (red << 16) | (green << 8) | blue;
    }

    public static int interpolate(double delta, int color1, int color2) {
        delta = Math.max(Math.min(delta, 1f), 0f);

        int deltaRed = red(color2) - red(color1);
        int deltaGreen = green(color2) - green(color1);
        int deltaBlue = blue(color2) - blue(color1);

        int red = (int) (red(color1) + (deltaRed * delta));
        int green = (int) (green(color1) + (deltaGreen * delta));
        int blue = (int) (blue(color1) + (deltaBlue * delta));

        red = Math.max(Math.min(red, 255), 0);
        green = Math.max(Math.min(green, 255), 0);
        blue = Math.max(Math.min(blue, 255), 0);

        return red << 16 | green << 8 | blue;
    }

    public static int red(int color) {
        return color >> 16 & 0xff;
    }

    public static int green(int color) {
        return color >> 8 & 0xff;
    }

    public static int blue(int color) {
        return color & 0xff;
    }
}
