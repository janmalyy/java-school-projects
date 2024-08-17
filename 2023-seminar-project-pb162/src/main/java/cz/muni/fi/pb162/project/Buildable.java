package cz.muni.fi.pb162.project;


/**
 * Functional interface serve as a template for implementing a creational design pattern
 * that lets you construct complex objects' step by step.
 * The pattern allows you to produce different types and representations
 * of an object using the same construction code.
 * When the object is ready to be built, {@link #build()} method is called.
 * Every implementation of this interface should contain at least one method to fill up the builder with the data.
 *
 * @param <T> what type we want to build
 * @author Alzbeta Strompova
 */
public interface Buildable<T> {

    /**
     * Method is called when the builder object is filled with data.
     *
     * @return new built object
     */
    T build();

}
