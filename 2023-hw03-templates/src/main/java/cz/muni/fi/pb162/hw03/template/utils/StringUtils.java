package cz.muni.fi.pb162.hw03.template.utils;

import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Provides useful string operations
 */
public final class StringUtils {

    private StringUtils() {
        // intentionally private
    }

    /**
     * Prefix based on predicate applied to each character
     *
     * @param input input string
     * @param predicate character predicate
     * @param beginIndex starting index
     * @return prefix where character match given predicate
     */
    public static String takeWhile(String input, int beginIndex, Predicate<Character> predicate) {
        return input
                .codePoints()
                .skip(beginIndex)
                .takeWhile(c -> predicate.test((char) c))
                .mapToObj(Character::toString)
                .collect(Collectors.joining());
    }

    /**
     * Same as {@link #takeWhile(String, int, Predicate)} but starts from the beginning
     *
     * @param input input string
     * @param predicate character predicate
     * @return prefix where character match given predicate
     */
    public static String takeWhile(String input, Predicate<Character> predicate) {
        return takeWhile(input, 0, predicate);
    }

    /**
     * Prefix up to specified substring
     *
     * @param input input string
     * @param limit substring marking the end of the prefix
     * @return prefix ending with the start of {@code limit}
     */
    public static String takeUntil(String input, String limit) {
        var index = input.indexOf(limit);
        return input.substring(0, (index != -1) ? index : input.length());
    }

    /**
     * Normalizes newline characters in text to {@code "\n"}
     *
     * @param input text
     * @return text with normalized newlines
     */
    public static String normalizeNewLines(String input) {
        return input.replace(System.lineSeparator(), "\n");
    }

    /**
     * Skips up to {@code n} leading whitespace characters.
     * Up to {@code n+1} characters can be removed when the
     * last two removed characters are {@code \r\n}
     *
     * @param input text
     * @param n max number of skipped whitespace characters
     * @return text with {@code n} leading whitespace characters stripped
     */
    public static String stripLeadingWhitespace(String input, int n) {
        var prefix = takeWhile(input, Character::isWhitespace);
        var from = Math.min(n, prefix.length());

        // No whitespaces at the beginning
        if (from == 0) {
            return input;
        }

        // Only whitespaces
        if (from == input.length()) {
            return "";
        }

        // If last character is \r skip the following \n
        var lst = input.charAt(from -1);
        var fst = input.charAt(from);
        if (lst == '\r' && fst == '\n') {
            from += 1;
        }

        // Return from first non-whitespace character
        return input.substring(from);
    }
}