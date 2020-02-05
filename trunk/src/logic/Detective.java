/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

/**
 * Main Class for the Scotland Yard Players subclass Detectives
 *
 * @author Alexander Löffler
 */
public class Detective extends Player {

    //*************************************** attributes ***************************************//
    private Mister mister;

    //*************************************** class ***************************************//
    /**
     * Detecite Class for Scotland Yard
     *
     * @param mister Mister X reference
     * @param isAi Sets if the Player is controller by AI
     * @param taxiTickets Number of Taxi Tickets
     * @param busTickets Number of Bus Tickets
     * @param undergroundTickets Number of Underground Tickets
     * @param startStation Start Station
     */
    Detective(Mister mister, boolean isAi, int taxiTickets, int busTickets, int undergroundTickets, Station startStation) {

        super(isAi, taxiTickets, busTickets, undergroundTickets, startStation);
        this.mister = mister;
    }

    /**
     * Uses the Bus Ticket and give it to Mister
     *
     */
    void useBusTicket() {

        super.useBusTicket();
        mister.reciveBusTicket();
    }

    /**
     * Uses the Taxi Ticket and give it to Mister
     *
     */
    void useTaxiTicket() {

        super.useTaxiTicket();
        mister.reciveTaxiTicket();
    }

    /**
     * Uses the Bus Underground and give it to Mister
     *
     */
    void useUndergroundTicket() {

        super.useUndergroundTicket();
        mister.reciveUndergroundTicket();
    }

}
