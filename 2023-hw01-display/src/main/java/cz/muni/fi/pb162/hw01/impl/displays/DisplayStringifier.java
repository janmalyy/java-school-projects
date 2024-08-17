package cz.muni.fi.pb162.hw01.impl.displays;


/**
 * Converts the contents of given display to {@link String}
 */
public interface DisplayStringifier {

    /**
     * Determines if given display can be stringified
     *
     * @param display display to check
     * @return true if this display can be stringified
     */
    boolean canStringify(Display display);

    /**
     * Stringify the contents of given display as lines
     *
     * @param display display to stringify
     * @return array of lines for given display or null if not possible
     */
    String[] asLines(Display display);

    /**
     * Stringify the contents of given display
     * calls asLines method and then concatenate its result into one string
     * @param display display to stringify
     * @return stringified content of given display or null if not possible
     */

    default String asString(Display display) {
        StringBuilder outputString = new StringBuilder();
        String[] lines = asLines(display);
        for (int i = 0; i < lines.length; i++) {
            outputString.append(lines[i]);
            // after the last line, no lineSeparator is put
            if (i != lines.length - 1) {
                outputString.append(System.lineSeparator());
            }
        }
        return outputString.toString();
    }
}
