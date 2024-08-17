package cz.muni.fi.pb162.project;

/**
 * representing the player of the chess game
 * @param name the name of the player
 * @param color black/white
 * @author Jan Maly
 */
public record Player(String name, Color color) {
    @Override
    public String toString() {
        return String.format("%s-%s", name, color);
    }
}
