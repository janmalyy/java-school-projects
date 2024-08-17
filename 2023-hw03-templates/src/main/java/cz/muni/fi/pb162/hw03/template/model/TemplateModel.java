package cz.muni.fi.pb162.hw03.template.model;

import cz.muni.fi.pb162.hw03.template.TemplateEngine;

import java.util.Objects;

/**
 * Data model for {@link TemplateEngine}
 */
public interface TemplateModel {
    /**
     * Add new value to this model
     *
     * @param key   lookup key
     * @param value associated value
     * @return this model
     */
    TemplateModel put(String key, Object value);

    /**
     * Creates shallow copy of this model
     *
     * @return copy of this model
     */
    TemplateModel copy();

    /**
     * Creates copy of this model with additional entry
     *
     * @param key   new key
     * @param value new value
     * @return extended copy of this model
     */
    TemplateModel extended(String key, Object value);

    /**
     * Lookup value by key in this model
     *
     * @param key lookup key (supports nesting)
     * @return value associated with given key
     * @throws ModelException if key is not present
     */
    Object getAsObject(String key);

    /**
     * Returns String representation of the value
     * associated with given key using {@link Objects#toString(Object)}
     *
     * @param key lookup key (supports nesting)
     * @return value associated with given key as String
     * @throws ModelException if key is not present
     */
    String getAsString(String key);

    /**
     * Returns {@code Truthy} representation of the value
     * associated with given key.
     * <p>
     * Based on data type a Truthy value is
     * <p>
     * - For Boolean: actual ture/false value
     * - For String: true if not empty, false otherwise
     * - For Collection: true if not empty, false otherwise
     * - For Number: true if not zero, false otherwise
     * - For Character: true if not zero value character, false otherwise
     * - For Object: true if not null, false otherwise
     * <p>
     * Note: the rule for Object is applicable only when no other rule matched
     *
     * @param key lookup key (supports nesting)
     * @return value associated with given key as boolean
     * @throws ModelException if key is not present
     */
    boolean getAsBoolean(String key);

    /**
     * Returns instance of {@link Iterable} associated with given key using
     *
     * @param key lookup key (supports nesting)
     * @return value associated with given key as {@code Iterable<Object>}
     * @throws ModelException if key is not present or value is not {@link Iterable}
     */
    Iterable<Object> getAsIterable(String key);
}
