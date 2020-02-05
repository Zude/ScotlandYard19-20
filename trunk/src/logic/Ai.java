/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * AI Class for the Scotland Yard Game
 *
 * @author Alexander Löffler
 */
public class Ai {

    //*************************************** attributes ***************************************//
    private Net net;
    private Player players[];
    private int playerCount;
    private Mister mister;
    private Log logger;

    // Impossible Ticket Score for calculating min Tickets
    private final int minTicketStartScore = 100;

    //*************************************** class ***************************************//
    /**
     * Main AI Class for AI Calculations
     *
     * @param net reference to the net
     * @param players reference to all Players
     * @param playerCount the nuber of PLayers
     * @param mister reference to mister
     * @param log reference for log
     */
    Ai(Net net, Player[] players, int playerCount, Mister mister, Log log) {
        this.net = net;
        this.players = players;
        this.playerCount = playerCount;
        this.mister = mister;
        this.logger = log;
    }

    //*************************************** main ***************************************//
    /**
     * Methos for calculating the next Station for Detecte AI PLayer
     *
     * @param possibleStations Set of all Stations where Mister can actually be
     * right now
     * @param closestAverageStation Closest Station to the Average Possibles
     * Stations where mister can be
     * @param turn number stop
     * @param player current player reference
     * @param currentPlayer number of current player
     * @return the calculated Station
     */
    Station detectiveAi(Set<Station> possibleStations, Station closestAverageStation, int turn, Player player, int currentPlayer) {

        Station returnStation = null;
        int lastMisterPosId = mister.getLastShowPosition();

        // All Stations from the last Showposition Neighboars that can be reached by the current Detectives
        Set<Station> accessableStationsForPlayers = stationsPlayersHaveAsNeighboar(possibleStations, players);
        Set<Station> accessableStationsForPlayer = stationsPlayerHasAsNeighboar(possibleStations, player);

        // -----------------Perform Tactic 1 and evaluate----------------- //
        Station resultStation1 = tactic1(lastMisterPosId, accessableStationsForPlayer);
        double scoreTactic1 = evaluateDetTactics(resultStation1, accessableStationsForPlayers.size(), possibleStations.size(), player, closestAverageStation);

        // -----------------Perform Tactic 2 and evaluate----------------- //
        Station resultStation2 = tactic2(player);
        double scoreTactic2 = evaluateDetTactics(resultStation2, accessableStationsForPlayers.size(), possibleStations.size(), player, closestAverageStation);

        // -----------------Perform Tactic 3 and evaluate----------------- //
        Station resultStation3 = tactic3(player, lastMisterPosId);
        double scoreTactic3 = evaluateDetTactics(resultStation3, accessableStationsForPlayers.size(), possibleStations.size(), player, closestAverageStation);

        // -----------------Perform Tactic 4 and evaluate----------------- //
        Station resultStation4 = tactic4(player);
        double scoreTactic4 = evaluateDetTactics(resultStation4, accessableStationsForPlayers.size(), possibleStations.size(), player, closestAverageStation);

        returnStation = calculateWinnerDet(resultStation1, resultStation2, resultStation3, resultStation4, scoreTactic1, scoreTactic2, scoreTactic3, scoreTactic4, player, currentPlayer);

      
        return returnStation;
    }

    /**
     * Methos for calculating the next Station for Mister AI PLayer
     *
     * @return the calculated Station
     */
    Station misterAi() {

        // return station
        Station resultStation = null;

        Set<Station> misterNeighboars = mister.getCurrentStation().getAllNeighboars();

        // Scores for the diffrent Tactics
        double tactic1Scores[] = new double[misterNeighboars.size()];

        // Resultstations for the diffrent Tactics
        Station tactic1Station[] = new Station[misterNeighboars.size()];

        int neighboarNumber = 0;

        // For all Possible Neighboars try tactic 1-3
        for (Station neighboar : misterNeighboars) {

            Set<Station> neighboars = neighboar.getAllNeighboars();
            double possibleDets = 0;
            double possibleTravelStations = 0;

            tactic1Scores[neighboarNumber] = 0;

            if (!neighboar.getIsVisited()) {

                // Get All neighboars from every neighboar (wich is not visited nor can be visited by dets)
                for (Station neighboarNeighboar : neighboars) {
                    if (neighboarNeighboar.getIsVisited()) {
                        for (int i = 0; i < players.length - 1; i++) {
                            if (players[i].getCurrentStation() == neighboarNeighboar
                                    && players[i].canTravelWithTicket(neighboar)) {
                                possibleDets++;
                            }
                        }
                    } else if (mister.canTravelWithTicket(neighboarNeighboar)) {
                        possibleTravelStations++;
                    }
                }

                // Tactic 1 Results
                tactic1Scores[neighboarNumber] = (Double.valueOf(playerCount - 1.0) - Double.valueOf(possibleDets)) * 10.0;
                tactic1Station[neighboarNumber] = neighboar;

                // Tactic 2 Results
                tactic1Scores[neighboarNumber] += possibleTravelStations / 13 * 4;

                int scoreResult = 0;

                // Get the smallest Ticket ammount for a Neighboar
                if (mister.canTravelWithBlack(neighboar)) {
                    if (mister.getBlackTickets() > 2) {
                        scoreResult = 3;
                    } else {
                        if (mister.getBlackTickets() > scoreResult) {
                            scoreResult = mister.getBlackTickets();
                        }
                    }
                }

                if (mister.canTravelWithBus(neighboar)) {

                    if (mister.getBusTickets() > 2) {
                        scoreResult = 3;
                    } else {
                        if (mister.getBusTickets() > scoreResult) {
                            scoreResult = mister.getBusTickets();
                        }
                    }
                }

                if (mister.canTravelWithTaxi(neighboar)) {

                    if (mister.getTaxiTickets() > 2) {
                        scoreResult = 3;
                    } else {
                        if (mister.getTaxiTickets() > scoreResult) {
                            scoreResult = mister.getTaxiTickets();
                        }
                    }
                }

                if (mister.canTravelWithUnderground(neighboar)) {
                    if (mister.getUndergroundTickets() > 2) {
                        scoreResult = 3;
                    } else {
                        if (mister.getUndergroundTickets() > scoreResult) {
                            scoreResult = mister.getUndergroundTickets();
                        }
                    }
                }

                tactic1Scores[neighboarNumber] += scoreResult;
            }
            neighboarNumber++;

        }

        // Calculates the winner
        resultStation = calculateWinnerMister(tactic1Scores,
                tactic1Station, neighboarNumber);

        if (resultStation == null || resultStation.getIsVisited()) {
            neighboarNumber++;
        }

        return resultStation;
    }

    //*************************************** helper ***************************************//
    /**
     * Calculates the winner for diffrent tactics and scores (Mister)
     *
     * @param tactic1Scores holds all scores for the tactics
     * @param tactic1Station holds all stations for the tactics
     * @param numberNeighboars Number of Neighboars around the station
     * @return Station that won
     */
    private Station calculateWinnerMister(double tactic1Scores[], Station tactic1Station[], int numberNeighboars) {

        Station resultStation = null;
        double resultValue = 0;

        // Takes the highest score and return the Station corresponding to it
        for (int i = 0; i < numberNeighboars; i++) {

            if (tactic1Scores[i] > resultValue) {
                resultValue = tactic1Scores[i];
                resultStation = tactic1Station[i];
            }
        }

        return resultStation;
    }

    /**
     * Calculates the winner for diffrent tactics and scores (Detectives)
     *
     * @param resultStation1 Result Station from Tactic 1
     * @param resultStation2 Result Station from Tactic 2
     * @param resultStation3 Result Station from Tactic 3
     * @param resultStation4 Result Station from Tactic 4
     * @param scoreTactic1 Result Score from Tactic 1
     * @param scoreTactic2 Result Score from Tactic 2
     * @param scoreTactic3 Result Score from Tactic 3
     * @param scoreTactic4 Result Score from Tactic 4
     * @param currentPlayer current Player
     * @param player number for the current Player
     * @return Station that won (highest score)
     */
    private Station calculateWinnerDet(Station resultStation1, Station resultStation2, Station resultStation3,
            Station resultStation4, double scoreTactic1, double scoreTactic2, double scoreTactic3, double scoreTactic4, Player currentPlayer, int player) {

        Station returnStation = null;
        Set<Station> returnSet = new HashSet<>();

        int logTactic = 0;
        Double logScore = 0.0;

        // Evaluates what Score is the highest and sets the winning Station and Score
        if (scoreTactic1 >= scoreTactic2 && scoreTactic1 >= scoreTactic3 && scoreTactic1 >= scoreTactic4) {
            returnSet.add(resultStation1);
            logTactic = 1;
            logScore = scoreTactic1;
        }
        if (scoreTactic2 >= scoreTactic1 && scoreTactic2 >= scoreTactic3 && scoreTactic2 >= scoreTactic4) {
            returnSet.add(resultStation2);
            logTactic = 2;
            logScore = scoreTactic2;
        }
        if (scoreTactic3 >= scoreTactic1 && scoreTactic3 >= scoreTactic2 && scoreTactic3 >= scoreTactic4) {
            returnSet.add(resultStation3);
            logTactic = 3;
            logScore = scoreTactic3;
        }
        if (scoreTactic4 >= scoreTactic1 && scoreTactic4 >= scoreTactic2 && scoreTactic4 >= scoreTactic3) {
            returnSet.add(resultStation4);
            logTactic = 4;
            logScore = scoreTactic4;
        }

        if (returnSet.size() > 1) {
            returnStation = getStationWithSmallestId(returnSet);
        } else {
            returnStation = returnSet.iterator().next();
        }

        // Round for 1 Decimal for logging
        logScore = Math.round(logScore * 10) / 10.0;

        // Write Log
        try {
            if (returnStation == null) {
                this.logger.writeLogForTurn(player, currentPlayer.getCurrentStation().getIdentifier(), 0,
                        currentPlayer.getUndergroundTickets(), currentPlayer.getBusTickets(), currentPlayer.getTaxiTickets(), currentPlayer.getBlackTickets(), logTactic, logScore);
            } else {
                this.logger.writeLogForTurn(player, currentPlayer.getCurrentStation().getIdentifier(), returnStation.getIdentifier(),
                        currentPlayer.getUndergroundTickets(), currentPlayer.getBusTickets(), currentPlayer.getTaxiTickets(), currentPlayer.getBlackTickets(), logTactic, logScore);
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return returnStation;

    }

    /**
     * Evaluate the Tactics for Detectives and gives back the total Score
     *
     * @param resultStation Result Station
     * @param numberOfAccesableStations Stations that can be accessed by
     * detectives
     * @param numberOfPossibleStations Total possible Stations
     * @param player current player reference
     * @param closestAverageStation Closest Station to the Average Possibles
     * Stations where mister can be
     * @return Total score for current tactics
     */
    private double evaluateDetTactics(Station resultStation, int numberOfAccesableStations, int numberOfPossibleStations, Player player, Station closestAverageStation) {

        double score1 = evaluateTactic1(numberOfAccesableStations, resultStation, numberOfPossibleStations);
        double score2 = evaluateTactic2(resultStation, closestAverageStation, player);
        double score3 = evaluateTactic3(resultStation, player);
        double score4 = evaluateTactic4(player, resultStation);

        return score1 + score2 + score3 + score4;
    }

    /**
     * Tactic 1 for detectives Step on the next possible Position Station
     *
     * @param lastMisterPosId ID for the last shown Mister Station
     * @param reachableStations Stations that can be accessed by detectives
     * @return Result Station arcording to tactic
     */
    private Station tactic1(int lastMisterPosId, Set<Station> reachableStations) {

        Station nextStation = null;

        if (lastMisterPosId > 0) {

            nextStation = getStationWithSmallestId(reachableStations);
        }

        return nextStation;
    }

    /**
     * Tactic 2 for detectives Step on the next Underground Statin
     *
     * @param player current Player
     * @return Result Station arcording to tactic (null if non found)
     */
    private Station tactic2(Player player) {
        assert player != null;

        Set<Station> neighboarUndergroundStations = new HashSet<>();

        Set<Station> tactic2Set = new HashSet<>();

        neighboarUndergroundStations = player.getCurrentStation().getAllNeighboars();

        // Gets all neighboar Undergournd Stations that are not visited
        if (player.getUndergroundTickets() == 0) {
            return null;
        } else {

            for (Station station : neighboarUndergroundStations) {
                if (station.isUndergroundStation() && !station.getIsVisited()) {
                    tactic2Set.add(station);
                }
            }

            return getStationWithSmallestId(tactic2Set);
        }
    }

    /**
     * Tactic 3 for detectives Step in the direction of last show Position
     *
     * @param player current Player
     * @param lastMisterPosId last Mister Show Position
     * @return Result Station arcording to tactic (null if non found)
     */
    private Station tactic3(Player player, int lastMisterPosId) {
        assert player != null;

        Station nextStation = null;

        if (lastMisterPosId != 0) {

            nextStation = net.shortestPath(player.getCurrentStation(), net.getStationWithId(lastMisterPosId), player);
        }

        return nextStation;
    }

    /**
     * Tactic 4 for detectives Step to neighboar Station with smallest ID
     *
     * @param player current Player
     * @return Result Station arcording to tactic (null if non found)
     */
    private Station tactic4(Player player) {
        assert player != null;

        Set<Station> set = player.getCurrentStation().getAllNeighboars();
        Set<Station> resultSet = new HashSet<>();

        for (Station object : set) {
            if (player.canTravelWithTicket(object) && !object.getIsVisited()) {
                resultSet.add(object);
            }
        }

        return getStationWithSmallestId(resultSet);
    }

    /**
     * Evaluates Detectives Tactic 1 and sets its score
     *
     * @param numberOfStationsDetectivesCanReach The actuall number of Possible
     * Station detedctives could reach
     * @param tactic1Result The result Station from Tactics 1
     * @param numberOfNeighboarsLastShowPosition The number of neighboars from
     * Misters last show position
     * @return Total Score for Tactics 1
     */
    private double evaluateTactic1(int numberOfStationsDetectivesCanReach, Station tactic1Result, int numberOfNeighboarsLastShowPosition) {

        if (tactic1Result != null) {
            numberOfStationsDetectivesCanReach--;
            numberOfNeighboarsLastShowPosition--;

            if (mister.getLastSeenRound() != 0 && numberOfNeighboarsLastShowPosition != 0) {
                return Double.valueOf(numberOfStationsDetectivesCanReach) / Double.valueOf(numberOfNeighboarsLastShowPosition) * 10;
            }
        }

        return 0;

    }

    /**
     * Evaluates Detectives Tactic 2 and sets its score
     *
     * @param tactics2Result Result Station from Tactics 2
     * @param avgStation Average Possible Positions Station
     * @param player Current Player
     * @return Total Score for Tactics 2
     */
    private double evaluateTactic2(Station tactics2Result, Station avgStation, Player player) {
        assert player != null;

        int numbersToAverage = 0;
        net.shortestPath(tactics2Result, avgStation, player);

        if (avgStation != null) {
            numbersToAverage = avgStation.getPathValue();
        }

        if (tactics2Result == null) {
            return 0.0;
        }

        if (numbersToAverage >= 10 || numbersToAverage <= 0) {
            return 0.0;
        } else {
            return 10 - Double.valueOf(numbersToAverage);
        }
    }

    /**
     * Evaluates Detectives Tactic 3 and sets its score
     *
     * @param tactics3Result Result Station from Tactics 3
     * @param player Current Player
     * @return Total Score for Tactics 3
     */
    private double evaluateTactic3(Station tactics3Result, Player player) {
        assert player != null;

        Set<Station> neighboar = new HashSet<>();

        if (tactics3Result != null) {
            neighboar = tactics3Result.getAllNeighboars();
        }

        int numbersOfNeighboars = 0;

        for (Station neighbboar : neighboar) {
            if (player.canTravelFromWithTicket(tactics3Result, neighbboar)) {
                numbersOfNeighboars++;
            }
        }

        return Double.valueOf(numbersOfNeighboars) / 13.0 * 4.0;
    }

    /**
     * Evaluates Detectives Tactic 4 and sets its score
     *
     * @param player Current Player
     * @param tactics3Result Result Station from Tactics 4
     * @return Total Score for Tactics 4
     */
    private int evaluateTactic4(Player player, Station resultStation) {
        assert player != null;

        int minTickets = minTicketStartScore;

        // Gets the least tickets for a vehicle
        if (player.canTravelWithBus(resultStation)) {
            if (player.getBusTickets() < minTickets) {
                minTickets = player.getBusTickets();
            }
        }
        if (player.canTravelWithTaxi(resultStation)) {
            if (player.getTaxiTickets() < minTickets) {
                minTickets = player.getTaxiTickets();
            }
        }
        if (player.canTravelWithUnderground(resultStation)) {
            if (player.getUndergroundTickets() < minTickets) {
                minTickets = player.getUndergroundTickets();
            }
        }

        if (minTickets == 100) {
            return 0;
        } else if (minTickets > 2) {
            return 3;
        } else {
            return minTickets;
        }
    }

    /**
     * Gets all Station a Player has as neighboar from a Set of Stations
     *
     * @param stations Set of Stations to check
     * @param player Current Player
     * @return Total Score for Tactics 4
     */
    private Set stationsPlayerHasAsNeighboar(Set<Station> stations, Player player) {
        assert player != null;

        Set<Station> reachableStations = new HashSet();

        // Gets the Station if he can Travel to it and its not visited
        for (Station s : stations) {

            if (s != null && player.canTravelWithTicket(s) && !s.getIsVisited()) {
                reachableStations.add(s);
            }
        }

        return reachableStations;
    }

    /**
     * Gets all Station all Players (detectives) has as neighboar from a Set of
     * Stations
     *
     * @param stations Set of Stations to check
     * @param players Array for all Players (dets)
     * @return Total Score for Tactics 4
     */
    private Set stationsPlayersHaveAsNeighboar(Set<Station> stations, Player players[]) {

        Set<Station> result = new HashSet<>();

        for (Player player : players) {

            result.addAll(stationsPlayerHasAsNeighboar(stations, player));
        }

        return result;
    }

    /**
     * Gets the Station with the smallest ID from a Set
     *
     * @param stations Set of Stations to check
     * @return Station with smallest ID
     */
    private Station getStationWithSmallestId(Set<Station> stations) {

        Station nextStation = null;

        int smallestId = this.net.getNumberOfStations();

        for (Station s : stations) {
            if (s != null && s.getIdentifier() < smallestId) {
                smallestId = s.getIdentifier();
                nextStation = s;
            }
        }

        return nextStation;
    }

}
