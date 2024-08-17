package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.impl.model.Evaluator;
import cz.muni.fi.pb162.hw03.template.FSTemplateEngine;
import cz.muni.fi.pb162.hw03.template.TemplateException;
import cz.muni.fi.pb162.hw03.template.model.TemplateModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * @author Jan Maly
 * class for handling the inputs (templates), loads them, evalutes them, writes the evaluated inputs into files
 */
public class MyFSTemplateEngine implements FSTemplateEngine {

    private final HashMap<String, String> templatesDataset = new HashMap<>();

    /**
     * @return collection of stored templates
     */
    @Override
    public Collection<String> getTemplateNames() {
        return new HashSet<>(templatesDataset.keySet());
    }

    /**
     * Loads template
     *
     * @param name name of the template
     * @param text text representation of raw template
     * @throws TemplateException if template is malformed
     */
    @Override
    public void loadTemplate(String name, String text) {
        List<Object[]> tokenizedInput = Evaluator.getTokenizedInput(text, new ArrayList<>());
        if (Evaluator.isCorrectSyntax(tokenizedInput)) {
            templatesDataset.put(name, text);
        } else {
            throw new TemplateException("Something wrong in syntax when trying to evaluate the given template:"
                    + System.lineSeparator() + text);
        }
    }

    /**
     * Load template from given file -- using filename without {@code ext}
     * as template name
     * <p>
     * Given {code "tpl"} as value of parameter {@code ext}
     * and file with absolute path /main/templates/resume.html.tpl
     * Then the name of the stored raw template will be "resume.html"
     *
     * @param file input file
     * @param cs   file charset encoding
     * @param ext  extension stripped from the filename
     */
    @Override
    public void loadTemplate(Path file, Charset cs, String ext) {
        Path toBeName = file.getFileName();
        String name = toBeName.toString();
        name = name.replace(ext, "").substring(0, name.length()-ext.length()-1);
        try {
            String content = Files.readString(file, cs);
            loadTemplate(name, content);
        } catch (IOException e) {
          throw new RuntimeException("A problem occurred when reading a content from given file " + file, e);
        }

    }

    /**
     * Load templates from files in given directory.
     * The method is not recursive.
     *
     * @param inDir input directory
     * @param cs    file charset encoding
     * @param ext   suffix stripped from filename
     */
    @Override
    public void loadTemplateDir(Path inDir, Charset cs, String ext) {
        File[] files = inDir.toFile().listFiles();
        if (files != null) {
            for (File file : files) {
                loadTemplate(file.toPath(), cs, ext);
            }
        }
    }

    /**
     * Evaluates template with given model
     *
     * @param name  template name
     * @param model model used for evaluation
     * @return evaluated text
     * @throws TemplateException if there is no loaded template with given name
     */
    @Override
    public String evaluateTemplate(String name, TemplateModel model) {
        Evaluator evaluator = new Evaluator(templatesDataset.get(name), model);
        return evaluator.evaluate();

    }

    /**
     * Evaluates template with given model and writes it into files
     *
     * @param name  template name
     * @param model model used for evaluation
     * @param file  output file
     * @param cs    file charset encoding
     * @throws TemplateException if there is no loaded template with given name
     */
    @Override
    public void writeTemplate(String name, TemplateModel model, Path file, Charset cs) throws TemplateException {
        if (!templatesDataset.containsKey(name)) {
            throw new TemplateException("There is no loaded template with given name " + name + ". " +
                    "All loaded template names are" + templatesDataset.keySet());
        }
        String evaluated = evaluateTemplate(name, model);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), cs));
            writer.write(evaluated);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("A problem occurred when trying to write into given file " + file, e);
        }
    }

    /**
     * Evaluates all loaded templates with given model and writes
     * them into output directory (name of the template is
     * used as filename)
     *
     * @param model  model used for evaluation
     * @param outDir output directory
     * @param cs     file charset encoding
     */
    @Override
    public void writeTemplates(TemplateModel model, Path outDir, Charset cs) {
        for (String templateName : templatesDataset.keySet()) {
            File file = new File(outDir.toFile(), templateName);
            writeTemplate(templateName, model, file.toPath(), cs);
        }
    }
}
