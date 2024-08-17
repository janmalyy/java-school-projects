package cz.muni.fi.pb162.hw01.impl;

import cz.muni.fi.pb162.hw01.impl.displays.Display;
import cz.muni.fi.pb162.hw01.impl.displays.DisplayStringifier;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static cz.muni.fi.pb162.hw01.impl.Outputs.ensurePlatformLines;
import static cz.muni.fi.pb162.hw01.impl.Outputs.removeSuffixNewLine;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class DisplayTest {

    private static Factory factory;
    private static DisplayStringifier stringifier;


    @BeforeAll
    public static void beforeAll() {
        factory = new Factory();
        stringifier = factory.stringifier();
    }

    @Test
    public void shouldCreateEmptyDisplay() {
        var display = factory.display(1);

        assertDisplay(display, Outputs.TXT_);
    }


    @Test
    public void shouldContainMultipleSymbols() {
        var display = factory.display(11);

        display.set("0123456789E");

        assertDisplay(display, Outputs.TXT_0123456789E);
    }

    @Test
    public void shouldHandleLongerInput() {
        var display = factory.display(4);

        display.set("0123456789E");

        assertDisplay(display, Outputs.TXT_0123);
    }

    @Test
    public void shouldHandleUnknownSymbol() {
        var display = factory.display(4);

        display.set("01k2");

        assertDisplay(display, Outputs.TXT_01E2);
    }

    @Test
    public void shouldHandleShorterInput() {
        var display = factory.display(6);

        display.set("8012");

        assertDisplay(display, Outputs.TXT_8012__);
    }

    @Test
    public void shouldHandleIndexedPlacement() {
        var display = factory.display(3);

        display.set(1,"E");

        assertDisplay(display, Outputs.TXT__E_);
    }

    @Test
    public void shouldKeepPrefixAndClearSuffixOnIndexedTextPlacement() {
        var display = factory.display(10);

        display.set("0123456789E");
        display.set(4,"3210");

        assertDisplay(display, Outputs.TXT_01233210__);
    }

    @Test
    public void shouldClearDisplay() {
        var display = factory.display(11);

        display.set("0123456789E");
        display.clear();

        var expectedLine = Outputs.EMPTY_LINE.repeat(11);

        assertDisplay(display, expectedLine, expectedLine, expectedLine);
    }

    @Test
    public void shouldClearSpecifiedPositions() {
        var display = factory.display(11);

        display.set("0123456789E");
        display.clear(3);
        display.clear(7);

        assertDisplay(display, Outputs.TXT_012_456_89E);
    }


    private void assertDisplay(Display display, String expected) {
        var platformExpected = ensurePlatformLines(expected);
        assertSoftly(softly -> {
            assertDisplayString(softly, display, platformExpected);
            assertDisplayLines(softly, display, platformExpected);
        });
    }

    private void assertDisplay(Display display, String... lines) {
        assertSoftly(softly -> {
            assertDisplayString(softly, display, lines);
            assertDisplayLines(softly, display, lines);
        });
    }

    private void assertDisplayLines(SoftAssertions assertions, Display display, String expected) {
        var lines = expected.split(System.lineSeparator());
        assertDisplayLines(assertions, display, lines);
    }

    private void assertDisplayLines(SoftAssertions assertions, Display display, String... lines) {
        var actual = stringifier.asLines(display);

        assertions
                .assertThat(actual)
                .describedAs("String lines of display")
                .containsExactly(lines);
    }

    private void assertDisplayString(SoftAssertions assertions, Display display, String... lines) {
        var expected = Arrays.stream(lines).collect(joining(System.lineSeparator()));
        assertDisplayString(assertions, display, expected);
    }

    private void assertDisplayString(SoftAssertions assertions, Display display, String expected) {
        var actual = removeSuffixNewLine(stringifier.asString(display));
        var description = "String representation of" + (expected.isBlank() ? "empty" : "") + " display";

        assertions
                .assertThat(actual)
                .describedAs(description)
                .isEqualTo(removeSuffixNewLine(expected));
    }


}
