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
 * Main Class for the Game Net
 *
 * @author Alexander Löffler
 */
class Net {

    private final Station[] stations;

    private final int numberOfStations;

    private final double maxDistanceBetween = 0.1;

    /**
     * Main Class for the Game Net
     *
     * @param netJson Net in JSON Format
     */
    Net(NetJSON netJson) {

        this.numberOfStations = netJson.getStations().length;

        // Init the size of the array
        this.stations = new Station[this.numberOfStations];

        // Init all Stations 
        for (int i = 0; i < this.numberOfStations; i++) {
            this.stations[i] = new Station(netJson.getStation(i));
        }

        // Fill all Neighboar References
        for (int i = 0; i < this.numberOfStations; i++) {
            this.stations[i].fillAllStations(this.stations, netJson.getStations());
        }
    }

    /**
     * Getter for all Stations
     *
     * @return Stations
     */
    Station[] getStations() {
        return stations;
    }

    /**
     * Getter for Statin with ID
     *
     * @param id The ID for the Station
     * @return Station with ID
     */
    Station getStationWithId(int id) {
        assert (id > 0);
        assert (id <= this.numberOfStations);

        return this.stations[id - 1];
    }

    /**
     * Getter for Number of Stations
     *
     * @return the number of Stations inside the net
     */
    int getNumberOfStations() {
        return numberOfStations;
    }

    /**
     * Calc the shortest Path between 2 Stations for current Player
     *
     * @param a Station a
     * @param b Station b
     * @param player current Player
     * @return next Station for the shortest Path
     */
    Station shortestPath(Station a, Station b, Player player) {

        if (a == null || b == null) {
            return null;
        }

        Set<Station> visitied = new HashSet();
        int i = 0;

        // Clear Values
        for (Station station : stations) {
            station.setPathValue(-1);
            station.setLastStation(null);
        }

        // Adds current Station
        visitied.add(a);
        a.setPathValue(i);
        i++;
        int counter = 0;

        // While we havnt reach b Check each Neighboar and sets Identifier and Last Station
        while (!visitied.contains(b)) {

            Set<Station> fillerSet = new HashSet();

            for (Station s : visitied) {

                Set<Station> set = new HashSet();

                if (player.getBusTickets() > 0) {
                    set.addAll(s.getBus());
                }
                if (player.getTaxiTickets() > 0) {
                    set.addAll(s.getCab());
                }
                if (player.getUndergroundTickets() > 0) {
                    set.addAll(s.getTube());
                }
                if (player.getBlackTickets() > 0) {
                    set.addAll(s.getBoat());
                }

                // Only add Stations that are not Visited nor the algorith already has reached
                for (Station s2 : set) {
                    if (!visitied.contains(s2) && !s2.getIsVisited()) {

                        s2.setLastStation(s);
                        if (s2.getIdentifier() == b.getIdentifier()) {
                        }
                        if (s2.getPathValue() == -1) {
                            s.setPathValue(i);
                            b.setPathValue(i);
                        }
                    }
                }

                fillerSet.addAll(getAllNeighboars(s));
            }
            i++;
            counter++;

            visitied.addAll(fillerSet);
        }

        Station checkStation = b;
        Station returnStation = null;

        // Find the Station by calulating backwards the Laststations
        if (b != null && checkStation.getLastStation() != null) {

            while (checkStation != a && checkStation.getLastStation() != null) {

                returnStation = checkStation;
                checkStation = checkStation.getLastStation();
            }
        }

        return returnStation;

    }

    /**
     * Gets the closest Station to gives Coordinates
     *
     * @param x X Coordinate
     * @param y Y Coorediante
     * @return Station that is closest to Coordinates
     */
    Station getClosestStationTo(double x, double y) {
        Station nextStation = null;
        double smallestDistance = this.maxDistanceBetween;
        double distance;

        // Takes the Station with the shortest Distance 
        for (int i = 0; i < this.numberOfStations; i++) {
            distance = calcDistanceStations(x, y, this.stations[i].getPosition().getX(), this.stations[i].getPosition().getY());
            if (distance < smallestDistance) {
                smallestDistance = distance;
                nextStation = this.stations[i];
            }
        }

        return nextStation;
    }

    /**
     * Calc the Distance between 2 Stations
     *
     * @param s1x X Coordinate Station 1
     * @param s1y Y Coorediante Station 1
     * @param s2x X Coordinate Station 2
     * @param s2y Y Coorediante Station 2
     * @return Distance
     */
    double calcDistanceStations(double s1x, double s1y, double s2x, double s2y) {
        double result;

        result = Math.sqrt(Math.pow((s2x - s1x), 2.0) + Math.pow((s2y - s1y), 2.0));

        return result;
    }

    /**
     * gets all Neighboars for a Station as Set
     *
     * @param station Station
     * @return All neighboars
     */
    Set getAllNeighboars(Station station) {

        Set returnSet = new HashSet();

        returnSet.addAll(station.getBoat());
        returnSet.addAll(station.getCab());
        returnSet.addAll(station.getBus());
        returnSet.addAll(station.getTube());

        return returnSet;
    }

}
