/*
 * Scotland Yard Game
 * Programmierpraktikum
 * Created by Alexander Löffler
 *
 */
package logic;

/**
 * Save Class Strucure for Detectives JSON Save Format
 *
 * @author Alexander Löffler
 */
 class DetectiveSave {

    private StationJSON currentStation;
    private int busTickets;
    private int taxiTickets;
    private int undergroundTickets;

    /**
     * JSON Strucure for Savegame (detectives block)
     *
     * @param currentStation for the Detective
     * @param busTickets Nubmer of Bus tickets
     * @param taxiTickets Number of Taxi Tickets
     * @param undergroundTickets Number of Underground Tickets
     */
    DetectiveSave(StationJSON currentStation, int busTickets, int taxiTickets, int undergroundTickets) {
        this.currentStation = currentStation;
        this.busTickets = busTickets;
        this.taxiTickets = taxiTickets;
        this.undergroundTickets = undergroundTickets;
    }

    /**
     * Getter for current Station
     *
     * @return current Station
     */
    StationJSON getCurrentStation() {
        return currentStation;
    }

    /**
     * Getter for Bus Tickets
     *
     * @return Bus Tickets
     */
    int getBusTickets() {
        return busTickets;
    }

    /**
     * Getter for taxi Tickets
     *
     * @return Taxi Tickets
     */
    int getTaxiTickets() {
        return taxiTickets;
    }

    /**
     * Getter for Underground Tickets
     *
     * @return Underground Tickets
     */
    int getUndergroundTickets() {
        return undergroundTickets;
    }

}
