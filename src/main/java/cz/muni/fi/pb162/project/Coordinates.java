package cz.muni.fi.pb162.project;

import cz.muni.fi.pb162.project.utils.BoardNotation;

/**
 * represents coordinates on the 2D board
 * @author Jan Maly
 * @param letterNumber first part of coordinates (a number 1-8 representing a letter a-h)
 * @param number second part of coordinates (a number 0-7 representing a number 1-8)
 */
public record Coordinates (int letterNumber, int number) implements Comparable<Coordinates> {

        /**
     * method for counting the average of two parts of given coordinates; converts them into
     * doubles in order to give the correct result with an odd sum
     * @return the average of two parts of given coordinates
     */
    public double averageOfCoordinates() {
        return ((double) letterNumber + (double) number)/2;
    }

    /**
     * takes other coordinates as an input parameter
     * and calculates the new coordinates by summing the two
     * @return new Coordinates object
     */
    public Coordinates add(Coordinates other) {
        return new Coordinates(
                this.letterNumber + other.letterNumber(),
                this.number + other.number());
    }

    @Override
    public String toString() {
        return BoardNotation.getNotationOfCoordinates(letterNumber, number);
    }

    @Override
    public int compareTo(Coordinates c) {
        if (this.letterNumber < c.letterNumber) {
                return -1;
            }
        if (this.letterNumber == c.letterNumber) {
            return Integer.compare(this.number, c.number);
        }
        return 1;
        // dá se zkrátit pomocí return s ? a :
    }
}
