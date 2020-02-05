/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

import java.util.HashSet;
import java.util.Set;

/**
 * Save Class Strucure for Mister JSON Save Format
 *
 * @author Alexander Löffler
 */
class MisterSave {

    private StationJSON misterCurrentStation;
    private StationJSON possiblePositions[][] = new StationJSON[24][];

    private int lastMisterPosition;
    private int blackTickets;
    private int busTickets;
    private int taxiTickets;
    private int undergroundTickets;
    private int billboardValues[] = new int[24];

    /**
     * JSON Strucure for Savegame (Mister block)
     *
     * @param misterCurrentStation Current Station
     * @param lastMisterPosition Last mister show Pos
     * @param blackTickets Number of Black Tickets
     * @param busTickets Number of bus Tickets
     * @param taxiTickets Number of taxi Tickets
     * @param undergroundTickets Number of Underground Tickets
     * @param billboardValues Billboard Values for GUI
     * @param possiblePositions Possible Possitions Array for Misters positions
     * @param map Map reference
     * @param turn Current Turn
     */
    MisterSave(StationJSON misterCurrentStation, int lastMisterPosition, int blackTickets, int busTickets, int taxiTickets, int undergroundTickets, int billboardValues[], Set<Station> possiblePositions[], NetJSON map, int turn) {
        this.misterCurrentStation = misterCurrentStation;
        this.lastMisterPosition = lastMisterPosition;
        this.blackTickets = blackTickets;
        this.busTickets = busTickets;
        this.taxiTickets = taxiTickets;
        this.undergroundTickets = undergroundTickets;
        this.billboardValues = billboardValues;

        // Fills the Possible Stations from Set to Array
        for (int i = 0; i < turn; i++) {
            this.possiblePositions[i] = new StationJSON[possiblePositions[i].size()];
            int x = 0;
            for (Station stations : possiblePositions[i]) {
                this.possiblePositions[i][x] = map.getStation(stations.getIdentifier() - 1);
                x++;
            }
        }
    }

    // Getter for Possible Positions
    StationJSON[][] getPossiblePositions() {
        return possiblePositions;
    }

    /**
     * Getter for Mister Curren Station
     *
     */
    StationJSON getMisterCurrentStation() {
        return misterCurrentStation;
    }

    /**
     * Getter for last Mister Position
     *
     */
    int getLastMisterPosition() {
        return lastMisterPosition;
    }

    /**
     * Getter for Black Tickets
     *
     */
    int getBlackTickets() {
        return blackTickets;
    }

    /**
     * Getter for Bus Tickets
     *
     */
    int getBusTickets() {
        return busTickets;
    }

    /**
     * Getter for Taxi Tickets
     *
     */
    int getTaxiTickets() {
        return taxiTickets;
    }

    /**
     * Getter for Underground Tickets
     *
     */
    int getUndergroundTickets() {
        return undergroundTickets;
    }

    /**
     * Getter for Billboard Values
     *
     */
    int[] getBillboardValues() {
        return billboardValues;
    }

}
