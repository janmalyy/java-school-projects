package cz.muni.fi.pb162.hw03.template;

import cz.muni.fi.pb162.hw03.template.model.TemplateModel;

import java.util.Collection;

/**
 * Representation of template engine.
 *
 * Template engine transforms template documents by interpolating
 * values from given model.
 */
public interface TemplateEngine {

    /**
     * Loads template
     *
     * @param name name of the template
     * @param text text representation of raw template
     * @throws TemplateException if template is malformed
     */
    void loadTemplate(String name, String text);

    /**
     * @return collection of stored templates
     */
    Collection<String> getTemplateNames();

    /**
     * Evaluates template with given model
     *
     * @param name template name
     * @param model model used for evaluation
     * @return evaluated text
     * @throws TemplateException if there is no loaded template with given name
     */
    String evaluateTemplate(String name, TemplateModel model);
}
