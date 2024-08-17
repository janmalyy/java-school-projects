package cz.muni.fi.pb162.hw01.impl.app;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import cz.muni.fi.pb162.hw01.impl.Outputs;
import org.junit.jupiter.api.Test;

import static cz.muni.fi.pb162.hw01.impl.Outputs.ensurePlatformLines;
import static cz.muni.fi.pb162.hw01.impl.Outputs.removeSuffixNewLine;
import static org.assertj.core.api.Assertions.assertThat;

public class DisplayAppTest {
    
    private static DisplayApp app = new DisplayApp();

    @Test
    public void shouldPrintEmptyDisplay() throws Exception {
        assertApplicationRun(1, "", Outputs.TXT_);
    }

    @Test
    public void shouldContainMultipleSymbols() throws Exception {
        assertApplicationRun(11, "0123456789E", Outputs.TXT_0123456789E);
    }

    @Test
    public void shouldHandleLongerInput() throws Exception {
        assertApplicationRun(4, "0123456789E", Outputs.TXT_0123);
    }

    @Test
    public void shouldHandleUnknownSymbol() throws Exception {
        assertApplicationRun(4, "01k2", Outputs.TXT_01E2);
    }

    @Test
    public void shouldHandleShorterInput() throws Exception {
        assertApplicationRun(6, "8012", Outputs.TXT_8012__);
    }

    private static String run(DisplayAppOptions options) throws Exception {
        return SystemLambda.tapSystemOut(() -> app.run(options));
    }

    private void assertApplicationRun(int size, String text, String expected) throws Exception {
        var actual = run(new DisplayAppOptions(size, text));
        var platformExpected = ensurePlatformLines(expected);
        assertThat(removeSuffixNewLine(actual)).isEqualTo(removeSuffixNewLine(platformExpected));
    }
}
