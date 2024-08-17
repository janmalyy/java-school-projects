package cz.muni.fi.pb162.hw03.template;

import cz.muni.fi.pb162.hw03.template.model.TemplateModel;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Extension of {@link TemplateEngine} which supports loading templates
 * from files and directories
 */
public interface FSTemplateEngine extends TemplateEngine {

    /**
     * Load template from given file -- using filename without {@code ext}
     * as template name
     *
     * Given {code "tpl"} as value of parameter {@code ext}
     * and file with absolute path /main/templates/resume.html.tpl
     * Then the name of the stored raw template will be "resume.html"
     *
     * @param file input file
     * @param cs file charset encoding
     * @param ext extension stripped from the filename
     */
    void loadTemplate(Path file, Charset cs, String ext);

    /**
     * Load templates from files in given directory.
     * The method is not recursive.
     *
     * @param inDir input directory
     * @param cs file charset encoding
     * @param ext suffix stripped from filename
     */
    void loadTemplateDir(Path inDir, Charset cs, String ext);

    /**
     * Evaluates template with given model and writes it into files
     *
     * @param name template name
     * @param model model used for evaluation
     * @param file output file
     * @param cs file charset encoding
     * @throws TemplateException if there is no loaded template with given name
     */
    void writeTemplate(String name, TemplateModel model, Path file, Charset cs);

    /**
     * Evaluates all loaded templates with given model and writes
     * them into output directory (name of the template is
     * used as filename)
     *
     * @param model model used for evaluation
     * @param outDir output directory
     * @param cs file charset encoding
     */
    void writeTemplates(TemplateModel model, Path outDir, Charset cs);
}
