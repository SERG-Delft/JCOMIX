package nl.tudelft.util;

/**
 * This class contains some global constants.
 *
 * @author Dimitri Stallenberg
 */
public final class Constants {

    public static final double MILLIS_PER_MINUTE = 60d * 1000d;
    public static final double MILLIS_PER_SEC = 1000d;
    public static final double TO_PERCENT_SCALE = 100d;

    /**
     * As this class is final its constructor should be private.
     *
     * @throws InstantiationException instantiating this class throws th
     */
    private Constants() throws InstantiationException {
        throw new InstantiationException("This class cannot be instantiated!");
    }
}
