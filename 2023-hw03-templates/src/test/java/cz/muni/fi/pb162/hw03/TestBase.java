package cz.muni.fi.pb162.hw03;

import cz.muni.fi.pb162.hw03.impl.Templates;
import cz.muni.fi.pb162.hw03.template.FSTemplateEngine;
import cz.muni.fi.pb162.hw03.template.model.TemplateModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;

public abstract class TestBase {
    protected FSTemplateEngine engine;
    protected TemplateModel model;

    @BeforeEach
    public void setup() {
        engine = Templates.engine();
        model = model();
    }

    protected TemplateModel model() {
        return Templates.modelOf(Map.of(
                "name", "Nibbles",
                "surname", "Disney",
                "cat", "Tom",
                "mouse", "Jerry",
                "yes", true,
                "no", false,
                "age", 42,
                "names", List.of("Butch", "Toodles", "Quacker")
        ));
    }

    protected void shouldEvaluate(String template, String in, String out) {
        // given
        engine.loadTemplate(template, in);
        // when / then
        shouldEvaluate(template, out);
    }

    protected void shouldEvaluate(String template,String out) {
        // when
        var actual = engine.evaluateTemplate(template, model);
        // then
        Assertions.assertThat(actual).isEqualToNormalizingWhitespace(out);
    }
}
