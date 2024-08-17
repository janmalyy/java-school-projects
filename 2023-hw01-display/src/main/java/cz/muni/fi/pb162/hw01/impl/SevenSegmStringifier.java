package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.impl.displays.Display;
import cz.muni.fi.pb162.hw01.impl.displays.DisplayStringifier;
import cz.muni.fi.pb162.hw01.impl.utils.SevenSegmStringNumbers;

import java.util.Arrays;

/**
 * converts the input text processed by DisplayImplementation into output string
 *      which will be printed to the screen
 * @author Jan Maly
 */
public class SevenSegmStringifier implements DisplayStringifier {
    @Override
    public boolean canStringify(Display display) {
        return display != null;
    }

    @Override
    public String[] asLines(Display display) {
        if (canStringify(display)) {
            String[] lines = new String[SevenSegmStringNumbers.getSymbolHeight()];
            //done in order to not have null values in the array
            Arrays.fill(lines, "");
            SevenSegmStringNumbers.createStringNumbers();

            for (int i = 0; i < SevenSegmStringNumbers.getSymbolHeight(); i++) {
                for (int j = 0; j < display.getTextToBeStringified().length(); j++) {
                    //symbol is the char on the specified position in textToBeStringified
                    String symbol = String.valueOf(display.getTextToBeStringified().charAt(j));
                    if (SevenSegmStringNumbers.getStringNumbers().containsKey(symbol)) {
                        // to the i-th string in lines the i-th part of the string representation of the symbol is added
                        lines[i] = lines[i].concat(SevenSegmStringNumbers.getStringNumbers()
                                .get(symbol)
                                .get(i));
                    } else {
                        //if this symbol not found, adds the error symbol string representation
                        lines[i] = lines[i].concat(SevenSegmStringNumbers.getStringNumbers()
                                .get("E")
                                .get(i));
                    }
                }
            }
            return lines;
        }
        return null;
    }
}
