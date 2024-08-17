package cz.muni.fi.pb162.hw03.template.model;

import cz.muni.fi.pb162.hw03.template.TemplateException;


/**
 * Exception representing problems with Model
 */
public class ModelException extends TemplateException {
    /**
     * Constructs a new model exception with the specified detail message
     * @param message the detail message
     */
    public ModelException(String message) {
        super(message);
    }

    /**
     * Constructs a new model exception with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
