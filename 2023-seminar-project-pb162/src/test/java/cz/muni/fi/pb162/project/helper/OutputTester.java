package cz.muni.fi.pb162.project.helper;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Radek Oslejsek, Marek Sabo
 */
public class OutputTester {

    private final OutputStream os;
    private final PrintStream tempOutput;
    private PrintStream origOutput;

    /**
     * Constructor
     */
    public OutputTester() {
        os = new ByteArrayOutputStream();
        tempOutput = new PrintStream(os);
    }

    /**
     * Capture stdout and temporarily replace it with controlled output stream
     */
    public void captureOutput() {
        origOutput = System.out;
        System.setOut(tempOutput);
    }

    /**
     * Release the captured stdout
     */
    public void releaseOutput() {
        System.setOut(origOutput);
    }

    /**
     * Returns output.
     *
     * @return output string
     */
    public String getOutput() {
        return os.toString();
    }

}
