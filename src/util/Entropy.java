package util;

import java.util.Random;

public class Entropy {

    static {
        random = new Random();
    }

    private static Random random;

    /**
     * @return an int in the range [min, max)
     */
    public static int nextInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    /**
     * @return a double in the range [min, max)
     */
    public static double next(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }
}
