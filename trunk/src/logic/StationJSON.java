/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

import logic.Position;

/**
 * JSON Structre for the Station (for loading)
 *
 * @author Alexander Löffler
 */
class StationJSON {

    private final int identifier;
    private final Position position;
    private final int[] tube;
    private final int[] bus;
    private final int[] cab;
    private final int[] boat;

    /**
     * JSON Strucure for NET
     *
     * @param identifier ID for Station
     * @param position Position for Station
     * @param tube Underground Neighboars
     * @param bus Bus Neighboars
     * @param cab Cab neighboars
     * @param boat Boat neighboars
     */
    StationJSON(int identifier, Position position, int[] tube, int[] bus, int[] cab, int[] boat) {
        this.identifier = identifier;
        this.position = position;
        this.tube = tube;
        this.bus = bus;
        this.cab = cab;
        this.boat = boat;
    }

    /**
     * Getter for ID
     *
     * @return ID
     */
    int getIdentifier() {
        return identifier;
    }

    /**
     * Getter for Position
     *
     * @return Position
     */
    Position getPosition() {
        return position;
    }

    /**
     * Getter for Tube
     *
     * @return Tube
     */
    int[] getTube() {
        return tube;
    }

    /**
     * Getter for Bus
     *
     * @return Bus
     */
    int[] getBus() {
        return bus;
    }

    /**
     * Getter for Cab
     *
     * @return Cab
     */
    int[] getCab() {
        return cab;
    }

    /**
     * Getter for Boat
     *
     * @return Boat
     */
    int[] getBoat() {
        return boat;
    }

    /**
     * Getter Underground Station by i
     *
     * @param i Position in Array
     * @return Station
     */
    int getTubeAt(int i) {
        return this.tube[i];
    }

    /**
     * Getter Bus Station by i
     *
     * @param i Position in Array
     * @return Station
     */
    int getBusAt(int i) {
        return this.bus[i];
    }

    /**
     * Getter Cab Station by i
     *
     * @param i Position in Array
     * @return Station
     */
    int getCabAt(int i) {
        return this.cab[i];
    }

    /**
     * Getter Boat Station by i
     *
     * @param i Position in Array
     * @return Station
     */
    int getBoatAt(int i) {
        return this.boat[i];
    }
}
