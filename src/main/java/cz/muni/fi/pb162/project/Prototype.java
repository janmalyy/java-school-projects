package cz.muni.fi.pb162.project;

/**
 * Functional interface serves as a template for implement creational design pattern Prototype
 * that lets you copy existing objects without making your code dependent on their classes.
 * @param <T> class type with which this interface is used
 * @author Alzbeta Strompova
 */
public interface Prototype<T> {

    /**
     * Method creates copy.
     * @return copy of object.
     */
    T makeClone();

}
