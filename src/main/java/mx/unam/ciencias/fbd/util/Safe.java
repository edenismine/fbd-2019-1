package mx.unam.ciencias.fbd.util;

public class Safe {
    public static String safeToString(Object o) {
        return o == null ? "" : o.toString();
    }
}
