package mx.unam.ciencias.fbd.util;

public class Validate {
    /**
     * Checks that all provided objects are not null.
     *
     * @param objects checked objects.
     * @throws IllegalArgumentException if a null value is provided.
     */
    public static void notNull(Object... objects) throws IllegalArgumentException {
        for (Object o : objects)
            if (o == null)
                throw new IllegalArgumentException("NotNull value is null");
    }

    /**
     * Checks that all provided strings are not empty.
     *
     * @param strings checked objects.
     * @throws IllegalArgumentException if a null value or an empty string is provided.
     */
    public static void notEmpty(String... strings) throws IllegalArgumentException {
        notNull((Object[]) strings);
        for (String s : strings)
            if (s.isEmpty())
                throw new IllegalArgumentException("NotEmpty value is empty");
    }
}
