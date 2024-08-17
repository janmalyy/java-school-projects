package cz.muni.fi.pb162.hw03;

import cz.muni.fi.pb162.hw03.template.TemplateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FileTemplateTest extends TestBase {

    public static final String EXAMPLES_SOURCE_PATH = System.getProperty("test.examples");
    public static final String EXTRA_SOURCE_PATH = System.getProperty("test.examples.extra");

    public static final Path EXAMPLES = Path.of(EXAMPLES_SOURCE_PATH);
    public static final Path EXTRA = Path.of(EXTRA_SOURCE_PATH);
    public static final String EXT = "tpl";

    public static final Charset ISO_8859_2 = Charset.forName("iso-8859-2");
    @TempDir
    private Path testDir;
    private Path testExamples;
    private Path testExtra;
    private Path testOutput;


    @BeforeEach
    void setUp() throws IOException {
        testExamples = testDir.resolve(EXAMPLES.getFileName());
        testExtra = testDir.resolve(EXTRA.getFileName());
        testOutput = testDir.resolve("output");

        copyDirectory(EXAMPLES, testExamples);
        copyDirectory(EXTRA, testExtra);
        Files.createDirectories(testOutput);
    }

    @Test
    public void shouldListTemplatesLoadedFromFile() throws IOException {
        // given
        var files = listFiles(testExamples);
        var names = fileNames(files);
        // when
        files.forEach(file -> {
            engine.loadTemplate(file, StandardCharsets.UTF_8, EXT);
        });
        // then
        Assertions.assertThat(engine.getTemplateNames()).containsExactlyInAnyOrderElementsOf(names);
    }

    @Test
    public void shouldListTemplatesLoadedFromDirectory() throws IOException {
        // given
        var files = listFiles(testExamples);
        var names = fileNames(files);
        // when
        engine.loadTemplateDir(testExamples, StandardCharsets.UTF_8, EXT);
        // then
        Assertions.assertThat(engine.getTemplateNames()).containsExactlyInAnyOrderElementsOf(names);
    }

    @Test
    public void shouldEvaluateTemplatesFromFile() {
        // given
        var expected = Map.of(
                "print.txt", Outputs.PRINT,
                "if.txt", Outputs.IF,
                "for.txt", Outputs.FOR,
                "complex.txt", Outputs.COMPLEX,
                "if_nested_in_else.txt", Outputs.IF_NESTED_IN_ELSE
        );
        // when
        engine.loadTemplateDir(testExamples, StandardCharsets.UTF_8, EXT);
        // then
        expected.forEach(this::shouldEvaluate);
    }

    @Test
    public void shouldEvaluateIso88592() {
        // given
        var name = "print_iso88592.txt";
        var file = testExtra.resolve(name + "." + EXT);
        // when
        engine.loadTemplate(file, ISO_8859_2, EXT);
        // then
        shouldEvaluate(name, Outputs.PRINT_CS);
    }

    @Test
    public void shouldFailToLoadMalformedTemplate() {
        //given
        var name = "malformed.txt";
        var file = testExtra.resolve(name + "." + EXT);
        // then
        Assertions.assertThatExceptionOfType(TemplateException.class)
                .isThrownBy(() ->  engine.loadTemplate(file, StandardCharsets.UTF_8, EXT));
    }

    @Test
    public void shouldFailToWriteMissingTemplate() {
        // given
        var name = "totallyMissingTemplate.txt";
        var output = testOutput.resolve(name);
        // then
        Assertions.assertThatExceptionOfType(TemplateException.class)
                .isThrownBy(() ->  engine.writeTemplate(name, model, output, StandardCharsets.UTF_8));
        Assertions.assertThat(output).doesNotExist();
    }

    @Test
    public void shouldWriteTemplateToFile() {
        shouldWrite("kun", Inputs.PRINT_CS, Outputs.PRINT_CS, "kun.txt", StandardCharsets.UTF_8);
    }

    @Test
    public void shouldWriteTemplateToFileIso88592() {
        shouldWrite("kun", Inputs.PRINT_CS, Outputs.PRINT_CS, "88592.txt", ISO_8859_2);
    }

    @Test
    public void shouldWriteTemplatesToDirAsISO88592() {
        // given
        var expected = Map.of(
                "print.txt", Outputs.PRINT,
                "if.txt", Outputs.IF,
                "for.txt", Outputs.FOR,
                "complex.txt", Outputs.COMPLEX,
                "if_nested_in_else.txt", Outputs.IF_NESTED_IN_ELSE
        );
        // when
        engine.loadTemplateDir(testExamples, StandardCharsets.UTF_8, EXT);
        engine.writeTemplates(model, testOutput, ISO_8859_2);
        // then
        expected.forEach((name, content) -> {
            shouldHaveContentEqualTo(testOutput.resolve(name), ISO_8859_2, content);
        });
    }

    protected void shouldWrite(String template, String in, String out, String fileName, Charset cs) {
        // given
        engine.loadTemplate(template, in);
        // when / then
        shouldWrite(template, out, testOutput.resolve(fileName), cs);
    }

    protected void shouldWrite(String template,String out, Path file, Charset cs) {
        // when
        engine.writeTemplate(template, model, file, cs);
        // then
        shouldHaveContentEqualTo(file, cs, out);
    }

    protected void shouldHaveContentEqualTo(Path file, Charset cs, String content) {
        Assertions.assertThat(file)
                .exists()
                .content(cs).isEqualToNormalizingWhitespace(content);
    }

    protected static void copyDirectory(Path sourceDir, Path targetDir) throws IOException {
        try (var walk = Files.walk(sourceDir)) {
            walk.forEach(source -> {
                try {
                    var relativeSource = sourceDir.relativize(source);
                    var target = targetDir.resolve(relativeSource);
                    Files.copy(source, target);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    protected static List<Path> listFiles(Path sourceDir) throws IOException {
        try (var stream = Files.list(sourceDir)) {
            return stream.toList();
        }
    }

    protected static List<String> fileNames(List<Path> files) {
        return files.stream()
                .map(FileTemplateTest::fileName)
                .toList();
    }

    protected static String fileName(Path file) {
        var name = file.getFileName().toString();
        return name.substring(0, name.lastIndexOf("."));
    }
}
