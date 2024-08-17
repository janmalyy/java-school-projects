package cz.muni.fi.pb162.hw01.impl;

/**
 * Expected output constants
 */
public final class Outputs {
    public static final String EMPTY_LINE = "   ";

    public static final String TXT_ = """
            \s\s\s
            \s\s\s
            \s\s\s
            """ ;

    public static final String TXT_0123456789E = """
                 _     _  _     _     _  _  _  _\s
                | |  | _| _||_||_ |_   ||_||_||_\s
                |_|  ||_  _|  | _||_|  ||_|  ||_\s
                """;

    public static final String TXT_0123 = """
                 _     _  _\s
                | |  | _| _|
                |_|  ||_  _|
                """;

    public static final String TXT_01E2 = """ 
                 _     _  _\s
                | |  ||_  _|
                |_|  ||_ |_\s
                """;

    public static final String TXT_8012__ = """
                 _  _     _      \s
                |_|| |  | _|     \s
                |_||_|  ||_      \s
                """;

    public static final String TXT__E_ = """
                \s   _   \s
                \s  |_   \s
                \s  |_   \s
                """;

    public static final String TXT_01233210__ = """
                 _     _  _  _  _     _      \s
                | |  | _| _| _| _|  || |     \s
                |_|  ||_  _| _||_   ||_|     \s
                """;

    public static final String TXT_012_456_89E = """
                 _     _        _        _  _  _\s
                | |  | _|   |_||_ |_    |_||_||_\s
                |_|  ||_      | _||_|   |_|  ||_\s
                """;
    private Outputs() {
        // intentionally private
    }

    public static String removeSuffixNewLine(String string) {
        if (string.endsWith(System.lineSeparator())) {
            return string.substring(0, string.length() - System.lineSeparator().length());
        }
        return string;
    }

    public static String ensurePlatformLines(String string) {
        return string.replace("\n", System.lineSeparator());
    }
}
