package cz.muni.fi.pb162.hw03.impl.model;

import cz.muni.fi.pb162.hw03.template.TemplateException;
import cz.muni.fi.pb162.hw03.template.model.ModelException;
import cz.muni.fi.pb162.hw03.template.model.TemplateModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Template model.
 * <p>
 * This class is a wrapper around {@code Map<String, Object>}
 * which provides additional functionality.
 *
 * <ul>
 *     <li>Typed access for String / Boolean / Iterable values</li>
 *     <li>Nested key reference, e.g. {@code customer.address.city}</li>
 * </ul>
 */
public class MapModel implements TemplateModel, Serializable {

    private final Map<String, Object> model = new HashMap<>();


    /**
     * Creates new model from given map
     *
     * @param model initial model entries
     */
    public MapModel(Map<String, Object> model) {
        this.model.putAll(model);
    }

    @Override
    public TemplateModel put(String key, Object value) {
        model.put(key, value);
        return this;
    }

    @Override
    public TemplateModel copy() {
        return new MapModel(model);
    }

    @Override
    public TemplateModel extended(String key, Object value) {
        return copy().put(key, value);
    }

    @Override
    public Object getAsObject(String key) {
        return getAsObject(key, model);
    }

    @Override
    public String getAsString(String key) {
        var value = getAsObject(key);
        return Objects.toString(value);
    }

    @Override
    public boolean getAsBoolean(String key) {
        var value = getAsObject(key);

        // No switch here until Java 21 :(
        if (value instanceof Boolean b) {
            return b;
        }
        if (value instanceof String s) {
            return !s.isEmpty();
        }
        if (value instanceof Collection<?> c) {
            return !c.isEmpty();
        }
        if (value instanceof Number n) {
            return n.longValue() != 0;
        }
        if (value instanceof Character c) {
            return c != 0;
        }

        return value != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<Object> getAsIterable(String key) {
        var value = getAsObject(key);
        if (!(value instanceof Iterable<?>)) {
            throw new TemplateException("Key not iterable: " + key);
        }
        return (Iterable<Object>) value;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getAsNested(String key, Map<String, Object> model) {
        var value = getAsShallow(key, model);

        if (!(value instanceof Map<?, ?>)) {
            throw new TemplateException("Key is not a nested model: " + key);
        }

        return (Map<String, Object>) value;
    }

    private Object getAsShallow(String key, Map<String, Object> model) {
        if (!model.containsKey(key)) {
            throw new ModelException("Key not found: " + key);
        }
        return model.get(key);
    }

    private Object getAsObject(String key, Map<String, Object> model) {
        var segments = key.split("\\.", 2);
        var immediate = segments[0];

        if (segments.length == 1) {
            return getAsShallow(immediate, model);
        }

        var nestedKey = segments[1];
        var nestedModel = getAsNested(immediate, model);

        return getAsObject(nestedKey, nestedModel);
    }
}
