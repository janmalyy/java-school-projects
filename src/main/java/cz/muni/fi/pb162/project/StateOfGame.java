package cz.muni.fi.pb162.project;

/**
 * @author Jan Maly
 */
public enum StateOfGame {
    WHITE_PLAYER_WIN, BLACK_PLAYER_WIN, PAT, PLAYING;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
