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
 * Main Class for the Scotland Yard Players subclass Mister
 *
 * @author Alexander Löffler
 */
public class Mister extends Player {

    //*************************************** attributes ***************************************//
    private Station turn3Station;
    private Station turn8Station;
    private Station turn13Station;
    private Station turn18Station;
    private Station turn24Station;

    private int blackTickets;
    private int lastShowPosition = 0;
    private int billboardValues[] = new int[24];
    private int lastSeenRound = 0;

    //*************************************** class ***************************************//
    /**
     * Mister Class for Scotland Yard
     *
     * @param isAi Sets if the Player is controller by AI
     * @param taxiTickets Number of Taxi Tickets
     * @param busTickets Number of Bus Tickets
     * @param undergroundTickets Number of Underground Tickets
     * @param black Ticket Number of Black Tickets
     * @param startStation Start Station
     */
    Mister(boolean isAi, int taxiTickets, int busTickets, int undergroundTickets, int blackTickets, Station startStation) {
        super(isAi, taxiTickets, busTickets, undergroundTickets, startStation);
        this.blackTickets = blackTickets;
    }

    /**
     * Getter for Last Show Position
     *
     * @return last Show Position
     */
    int getLastShowPosition() {
        return lastShowPosition;
    }

    /**
     * Getter for Last Turn 3 Station
     *
     * @return Turn 3 Station
     */
    Station getTurn3Station() {
        return turn3Station;
    }

    /**
     * Getter for Last Turn 8 Station
     *
     * @return Turn 8 Station
     */
    Station getTurn8Station() {
        return turn8Station;
    }

    /**
     * Getter for Last Turn 13 Station
     *
     * @return Turn 13 Station
     */
    Station getTurn13Station() {
        return turn13Station;
    }

    /**
     * Getter for Last Turn 18 Station
     *
     * @return Turn 18 Station
     */
    Station getTurn18Station() {
        return turn18Station;
    }

    /**
     * Getter for Last Turn 24 Station
     *
     * @return Turn 24 Station
     */
    Station getTurn24Station() {
        return turn24Station;
    }

    /**
     * Gets the last Round Mister has been seen
     *
     * @return last Show round
     */
    int getLastSeenRound() {
        return lastSeenRound;
    }

    @Override
    int getBlackTickets() {
        return blackTickets;
    }

    /**
     * Gets the Array of Billboard Values
     *
     * @return Values for the GUI Billboard
     */
    int[] getBillboardValues() {
        return billboardValues;
    }

    /**
     * Gets the Array of Billboard Values
     *
     * @param billboardValues Sets the Array with Billboar values
     */
    void setBillboardValues(int[] billboardValues) {
        this.billboardValues = billboardValues;
    }

    @Override
    void movePlayerToNextStation() {

        this.setCurrentStation(this.getNextStation());
    }

    @Override
    void useBlackTicket() {
        this.blackTickets--;
    }

    @Override
    boolean isMovePossible() {

        return super.isMovePossible()
                || (this.blackTickets > 0);
    }

    @Override
    boolean canTravelWithBlack(Station nextStation) {
        return ((getCurrentStation().getTube().contains(nextStation)
                || getCurrentStation().getBus().contains(nextStation)
                || getCurrentStation().getCab().contains(nextStation))
                && (this.blackTickets > 0));
    }

    @Override
    boolean canTravelWithboat(Station nextStation) {
        return (getCurrentStation().getBoat().contains(nextStation) && this.blackTickets > 0);
    }

    @Override
    public void updateVisited() {

    }

    /**
     * Updates Mister Visibility and Billboard Values for current Turn
     *
     * @param turn current Game turn
     * @param ticket Ticket for Billboard values
     * @return bool if he is visibil right now
     */
    boolean updateMisterVisibility(int turn, Tickets ticket) {

        boolean isVisible = false;

        // Sets the Bisibility and Last Show Position
        switch (turn) {
            case 3: {

                turn3Station = getCurrentStation();
                isVisible = true;
                lastShowPosition = getCurrentStation().getIdentifier();
                lastSeenRound = 3;
                break;
            }
            case 8: {

                turn8Station = getCurrentStation();
                isVisible = true;
                lastShowPosition = getCurrentStation().getIdentifier();
                lastSeenRound = 8;
                break;
            }
            case 13: {

                turn13Station = getCurrentStation();
                isVisible = true;
                lastShowPosition = getCurrentStation().getIdentifier();
                lastSeenRound = 13;
                break;
            }
            case 18: {

                turn18Station = getCurrentStation();
                isVisible = true;
                lastShowPosition = getCurrentStation().getIdentifier();
                lastSeenRound = 18;
                break;
            }
            case 24: {

                turn24Station = getCurrentStation();
                isVisible = true;
                lastShowPosition = getCurrentStation().getIdentifier();
                lastSeenRound = 24;
                break;
            }
        }

        // Updates Billoard values 
        switch (ticket) {
            case Underground: {
                this.billboardValues[turn - 1] = 0;
                break;
            }
            case Bus: {
                this.billboardValues[turn - 1] = 1;
                break;
            }
            case Taxi: {
                this.billboardValues[turn - 1] = 2;
                break;
            }
            case Black: {
                this.billboardValues[turn - 1] = 3;
                break;
            }
        }

        return isVisible;
    }

    /**
     * gets if Mister is visibil for turn
     *
     * @param turn current Game turn
     * @return bool if he is visibil right now
     */
    boolean isMisterVisible(int turn) {

        boolean isVisible = false;

        switch (turn) {
            case 3: {

                isVisible = true;

                break;
            }
            case 8: {

                isVisible = true;

                break;
            }
            case 13: {

                isVisible = true;

                break;
            }
            case 18: {

                isVisible = true;

                break;
            }
            case 24: {

                isVisible = true;

                break;
            }
        }
        return isVisible;
    }

}
