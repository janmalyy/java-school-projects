package cz.muni.fi.pb162.hw03;

import cz.muni.fi.pb162.hw03.template.TemplateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StringTemplateTest extends TestBase {

    @Test
    public void shouldEvaluatePrint() {
        shouldEvaluate("print", Inputs.PRINT, Outputs.PRINT);
    }

    @Test
    public void shouldEvaluateIf() {
        shouldEvaluate("if", Inputs.IF, Outputs.IF);
    }

    @Test
    public void shouldEvaluateFor() {
        shouldEvaluate("for", Inputs.FOR, Outputs.FOR);
    }

    @Test
    public void shouldEvaluateNestedCondition() {
        shouldEvaluate("nestedCondition", Inputs.IF_NESTED_IN_ELSE, Outputs.IF_NESTED_IN_ELSE);
    }

    @Test
    public void shouldEvaluatedComplexTemplate() {
        shouldEvaluate("complex", Inputs.COMPLEX, Outputs.COMPLEX);
    }

    @Test
    public void shouldFailToEvaluateMissingTemplate() {
        Assertions.assertThatExceptionOfType(TemplateException.class)
                .isThrownBy(() ->  engine.evaluateTemplate("totallyMissingTemplate", model));
    }

    @Test
    public void shouldFailToLoadMalformedTemplate() {
        Assertions.assertThatExceptionOfType(TemplateException.class)
                .isThrownBy(() ->  engine.loadTemplate("ifWithMissingDone", Inputs.IF_ELSE_MISSING_DONE));
    }

    @Test
    public void shouldListTemplatesLoadedFromString() {
        // given
        var expected = List.of("A", "B", "C");
        // when
        engine.loadTemplate(expected.get(0), Inputs.PRINT);
        engine.loadTemplate(expected.get(1), Inputs.PRINT);
        engine.loadTemplate(expected.get(2), Inputs.PRINT);
        // then
        Assertions.assertThat(engine.getTemplateNames()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void shouldOverrideTemplate() {
        // given
        var expected = List.of("A", "B");
        // when
        engine.loadTemplate(expected.get(0), Inputs.IF);
        engine.loadTemplate(expected.get(1), Inputs.IF);
        engine.loadTemplate(expected.get(1), Inputs.PRINT);
        // then
        Assertions.assertThat(engine.getTemplateNames()).containsExactlyInAnyOrderElementsOf(expected);
        shouldEvaluate(expected.get(1), Outputs.PRINT);
    }
}
