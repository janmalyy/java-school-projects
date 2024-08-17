package cz.muni.fi.pb162.project;

/**
 * @author Jan Maly
 */
public enum Color {
    WHITE, BLACK;

    /**
     * name says everything
     * @return enum type Color, the opposite one
     */
    public Color getOppositeColor() {
        // condition ? what to do if true : what to do if else
        return this.equals(WHITE) ? BLACK : WHITE;
    }
}
