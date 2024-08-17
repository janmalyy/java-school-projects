package cz.muni.fi.pb162.hw03.template;

/**
 * Exception representing problems with template
 */
public class TemplateException extends RuntimeException {

    /**
     * Constructs a new template exception with the specified detail message
     *
     * @param message the detail message
     */
    public TemplateException(String message) {
        super(message);
    }

    /**
     * Constructs a new template exception with the specified detail message
     * and cause
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }

}
