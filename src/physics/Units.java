package physics;

public class Units {
    //TODO insert real values
    public static final double KG = 1;
    // TODO this is just preliminary
    public static final double PIXELS_PER_METER = 64;
    public static final double M = 1 / PIXELS_PER_METER;
    public static final double S = sherwood.gameScreen.GameScreen.DEFAULT_TICKSPERSEC;
    public static final double MPS = M / S;
    public static final double MPS2 = M / S / S;
    public static final double N = KG * MPS2;
}
