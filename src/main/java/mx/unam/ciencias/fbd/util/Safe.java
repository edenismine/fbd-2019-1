package mx.unam.ciencias.fbd.util;

/**
 * Utility class, provides safe version of several common methods.
 */
public class Safe {
    /**
     * @param o an object, possibly null.
     * @return the object's string representation via {@link Object#toString()} or an empty string if the object is
     * null.
     */
    public static String safeToString(Object o) {
        return o == null ? "" : o.toString();
    }
}
