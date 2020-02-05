/*
 * Scotland Yard Game
 * Programmierpraktikum
 * Created by Alexander Löffler
 *
 */
package logic;

/**
 * JSON Structre for the NET (for loading)
 *
 * @author Alexander Löffler
 */
class NetJSON {

    private final StationJSON[] stations;

    /**
     * JSON Strucure for NET
     *
     * @param station All Stations
     */
    NetJSON(StationJSON[] stations) {
        this.stations = stations;
    }

    /**
     * Getter for Stations
     *
     * @return get Stations
     */
    StationJSON[] getStations() {
        return stations;
    }

    /**
     * get Station with ID
     *
     * @param i ID
     * @return Station for ID
     */
    StationJSON getStation(int i) {
        return stations[i];
    }

}
