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
import logic.Position;

/**
 * Class for a Station
 *
 * @author Alexander Löffler
 */
class Station {

    private final int identifier;
    private final Position position;

    private final Set tube;
    private final Set bus;
    private final Set cab;
    private final Set boat;

    private boolean isVisited = false;
    private int pathValue = -1;
    private Station lastStation = null;

    /**
     * Class for a Station
     *
     * @param Statioan in JSON Format
     */
    Station(StationJSON station) {
        this.identifier = station.getIdentifier();
        this.position = station.getPosition();
        this.tube = new HashSet();
        this.bus = new HashSet();
        this.cab = new HashSet();
        this.boat = new HashSet();
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
     * Getter for Identifier
     *
     * @return identifier
     */
    int getIdentifier() {
        return identifier;
    }

    /**
     * Getter for Tube
     *
     * @return Tubes
     */
    Set getTube() {
        return tube;
    }

    /**
     * Getter for Bus
     *
     * @return Bus
     */
    Set getBus() {
        return bus;
    }

    /**
     * Getter for Taxi
     *
     * @return Taxi
     */
    Set getCab() {
        return cab;
    }

    /**
     * Getter for Boat
     *
     * @return boat
     */
    Set getBoat() {
        return boat;
    }

    /**
     * Getter for PathValue
     *
     * @return Path value
     */
    int getPathValue() {
        return pathValue;
    }

    /**
     * Setter for path Value
     *
     * @param i Path Value
     */
    void setPathValue(int i) {
        this.pathValue = i;
    }

    /**
     * Getter for Last Station
     *
     * @return Last Station
     */
    Station getLastStation() {
        return lastStation;
    }

    /**
     * Setter for last Station
     *
     * @param lastStation Station to set Last Station to
     */
    void setLastStation(Station lastStation) {
        this.lastStation = lastStation;
    }

    /**
     * Getter for Is Visited
     *
     * @return Is Visited
     */
    boolean getIsVisited() {
        return isVisited;
    }

    /**
     * Setter for is Visited
     *
     * @param isVisited value if a Station is Visited
     */
    void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    /**
     * Checks if current Station is an Underground Station
     *
     * @return is Underground Station
     */
    boolean isUndergroundStation() {

        return !getTube().isEmpty();
    }

    /**
     * Get all Neighboars for the Station
     *
     * @return All Neighboars
     */
    Set<Station> getAllNeighboars() {
        Set<Station> returnSet = new HashSet<>();

        returnSet.addAll(cab);
        returnSet.addAll(bus);
        returnSet.addAll(boat);
        returnSet.addAll(tube);

        return returnSet;
    }

    /**
     * Fill all Stations with References from JSON Station IDs
     *
     * @param stations All Station from the Net
     * @pararm jsonStation All Stations in JSOn Format
     */
    void fillAllStations(Station[] stations, StationJSON[] jsonStations) {

        // Fill Tubes Refereces
        for (int i = 0; i < jsonStations[identifier - 1].getTube().length; i++) {
            //this.tube[i] = stations[jsonStations[identifier - 1].getTubeAt(i) - 1];
            this.tube.add(stations[jsonStations[identifier - 1].getTubeAt(i) - 1]);
        }

        // Fill Cab Refereces
        for (int i = 0; i < jsonStations[identifier - 1].getCab().length; i++) {
            //this.cab[i] = stations[jsonStations[identifier - 1].getCabAt(i) - 1];
            this.cab.add(stations[jsonStations[identifier - 1].getCabAt(i) - 1]);
        }

        // Fill Bus Refereces
        for (int i = 0; i < jsonStations[identifier - 1].getBus().length; i++) {
            // this.bus[i] = stations[jsonStations[identifier - 1].getBusAt(i) - 1];
            this.bus.add(stations[jsonStations[identifier - 1].getBusAt(i) - 1]);
        }

        // Fill Boat Refereces
        for (int i = 0; i < jsonStations[identifier - 1].getBoat().length; i++) {
            //this.boat[i] = stations[jsonStations[identifier - 1].getBoatAt(i) - 1];
            this.boat.add(stations[jsonStations[identifier - 1].getBoatAt(i) - 1]);
        }
    }

}
