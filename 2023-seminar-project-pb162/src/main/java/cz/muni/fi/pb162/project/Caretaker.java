package cz.muni.fi.pb162.project;

/**
 * Interface to help implement behavioral design pattern Memento that lets you save and restore
 * the previous state of an object without revealing the details of its implementation.<p>
 * This design patter consist of
 * - The {@code Originator} class can produce snapshots of its own state,
 * as well as restore its state from snapshots when needed.
 * - The Memento is a value object that acts as a snapshot of the originator’s state.
 * It’s a common practice to make the memento immutable and pass it the data only once, via the constructor.
 * - The {@code Caretaker} knows not only “when” and “why” to capture the originator’s state,
 * but also when the state should be restored.
 *
 * @author Alzbeta Strompova
 */
public interface Caretaker {

    /**
     * Method is called when we want save Originator.
     */
    void hitSave();

    /**
     * Method is called when we want to take a step to back.
     */
    void hitUndo();

}
