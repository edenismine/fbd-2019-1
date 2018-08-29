package mx.unam.ciencias.fbd.util;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUtils {
    /**
     * Method that asks the user for a valid string using a custom message and
     * only admits String's that match the provided regular expression. It then
     * returns such valid String.
     *
     * @param regex  Regular expression to be matched.
     * @param scan   The scanner that's been used to retrieve the user input.
     * @param prefix The prefix to be put on the input prompt.
     * @return the string provided by the user that matches {@code regex}.
     */
    public static String getValidString(String regex, Scanner scan, String prefix) {
        boolean seen = false;
        String str;
        do {
            if (seen) {
                error("Invalid input, try again.");
                input(prefix);
            } else {
                input(prefix);
            }
            while (!scan.hasNextLine()) {
                scan.next();
            }
            str = scan.nextLine();
            seen = true;
        } while (!str.matches(regex));
        return str;
    }

    /**
     * Method that extracts the capturing group with index {@code num} from a
     * given String, {@code searchSpace} using the provided regular expression
     * {@code regex}. Capturing groups are indexed from left to right, starting
     * at one. Group zero denotes the entire pattern.
     *
     * @param regex       Regular expression that will be use to match the provided
     *                    string.
     * @param searchSpace The string from which this method will attempt to extract a
     *                    matching group.
     * @param num         the index of the desired matching group.
     * @return the capturing group with index {@code num} or an empty String if
     * no such group exists.
     */
    public static String getGroup(String regex, String searchSpace, int num) {
        Matcher match = Pattern.compile(regex).matcher(searchSpace);
        if (match.find())
            try {
                return match.group(num);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        return "";
    }

    /**
     * This method takes a String and prints an adorned version using the prefix "ERROR: ". It can be used to signal
     * errors to the user.
     *
     * @param message the String to be decorated.
     */
    public static void error(String message) {
        System.out.println("ERROR: " + message);
    }

    public static void input() {
        input("");
    }

    public static void input(String prefix) {
        System.out.print(prefix + ">> ");
    }
}
