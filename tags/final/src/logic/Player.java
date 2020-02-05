/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

/**
 * Main Class for the Scotland Yard Players
 *
 * @author Alexander Löffler
 */
public class Player {

    //*************************************** attributes ***************************************//
    private Station currentStation;
    private Station nextStation;

    private boolean isAi;

    private int taxiTickets;
    private int busTickets;
    private int undergroundTickets;

    //*************************************** class ***************************************//
    /**
     * Player for Scotland Yard
     *
     * @param isAi Sets if the Player is controller by AI
     * @param taxiTickets Number of Taxi Tickets
     * @param busTickets Number of Bus Tickets
     * @param undergroundTickets Number of Underground Tickets
     * @param startStation Start Station
     */
    Player(boolean isAi, int taxiTickets, int busTickets, int undergroundTickets, Station startStation) {

        this.isAi = isAi;
        this.taxiTickets = taxiTickets;
        this.busTickets = busTickets;
        this.undergroundTickets = undergroundTickets;
        currentStation = startStation;
        this.nextStation = null;

        this.updateVisited();
    }

    /**
     *
     * @param currentStation
     */
    void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    /**
     * Getter for Players current Station
     *
     * @return Players current Station
     */
    Station getCurrentStation() {
        return currentStation;
    }

    boolean isAi() {
        return this.isAi;
    }

    /**
     * Getter for Taxi Tickets
     *
     * @return number of Taxi Tickets
     */
    int getTaxiTickets() {
        return taxiTickets;
    }

    /**
     * Getter for Bus Tickets
     *
     * @return number of Bus Tickets
     */
    int getBusTickets() {
        return busTickets;
    }

    /**
     * Getter for Black Tickets
     *
     * @return number of black Tickets
     */
    int getBlackTickets() {
        return 0;
    }

    /**
     * Getter for Underground Tickets
     *
     * @return number of Underground Tickets
     */
    int getUndergroundTickets() {
        return undergroundTickets;
    }

    /**
     * Gets the next Station
     *
     * @return next Station
     */
    public Station getNextStation() {
        return nextStation;
    }

    /**
     * Sets the next Station
     *
     * @param nextStation
     */
    public void setNextStation(Station nextStation) {
        this.nextStation = nextStation;
    }

    /**
     * Setter for is AI
     *
     * @param isPlayerAi boolean if the Player is AI
     */
    void setAi(boolean isPlayerAi) {
        this.isAi = isPlayerAi;
    }

    /**
     * Increases Taxi Tickets by 1
     */
    void reciveTaxiTicket() {
        this.taxiTickets++;
    }

    /**
     * Increases Bus Tickets by 1
     */
    void reciveBusTicket() {
        this.busTickets++;
    }

    /**
     * Increases Underground Tickets by 1
     */
    void reciveUndergroundTicket() {
        this.undergroundTickets++;
    }

    /**
     * Calculates the Minimum ammount of Tickets a Player has
     *
     * @return minimum Ammount of Tickets
     */
    int minTickets() {
        int result = undergroundTickets;

        if (undergroundTickets < result) {
            result = undergroundTickets;
        }
        if (busTickets < result) {
            result = busTickets;
        }
        if (taxiTickets < result) {
            result = taxiTickets;
        }
        if (getBlackTickets() != 0 && getBlackTickets() < result) {
            result = getBlackTickets();
        }

        return result;
    }

    /**
     * Calculates the Ticket Type Player has Most
     *
     * @return Ticket type
     */
    Tickets ticketPlayerHasMost(boolean bus, boolean taxi, boolean underground, boolean black) {

        Tickets returnTicket = Tickets.None;

        int maxTickets = 0;
        
        if (black && getBlackTickets() > maxTickets) {
            maxTickets = getBlackTickets();
            returnTicket = Tickets.Black;
            
        }
         if (underground && undergroundTickets > maxTickets) {
            maxTickets = undergroundTickets;
            returnTicket = Tickets.Underground;
            
        }
          if (bus && busTickets > maxTickets) {
            maxTickets = busTickets;
            returnTicket = Tickets.Bus;
            
        }
        if (taxi && taxiTickets > maxTickets) {
            maxTickets = taxiTickets;
            returnTicket = Tickets.Taxi;
            
        }
       
       
        
        

        return returnTicket;
    }

    /**
     * Moves Player from the curren Statoin to the Next Station
     */
    void movePlayerToNextStation() {

        if (this.nextStation != null) {
            this.currentStation.setIsVisited(false);
            this.currentStation = this.nextStation;
        }

        this.updateVisited();
    }

    /**
     * Sets the current Stataion as visited
     */
    void updateVisited() {
        currentStation.setIsVisited(true);
    }

    /**
     * Decreases Bus Tickets by 1
     */
    void useBusTicket() {
        this.busTickets--;
    }

    /**
     * Decreases Taxi Tickets by 1
     */
    void useTaxiTicket() {
        this.taxiTickets--;
    }

    /**
     * Decreases Underground Tickets by 1
     */
    void useUndergroundTicket() {
        this.undergroundTickets--;
    }

    /**
     * Decreases Black Tickets by 1
     */
    void useBlackTicket() {
    }

    /**
     * Checks if its still Possible to make a Move at all from the current
     * Station
     *
     * @return boolean that is true, if its still possible to move
     */
    boolean isMovePossible() {

        return (!this.currentStation.getBus().isEmpty() && this.busTickets > 0)
                || (!this.currentStation.getCab().isEmpty() && this.taxiTickets > 0)
                || (!this.currentStation.getTube().isEmpty() && this.undergroundTickets > 0);
    }

    /**
     * Checks if the Player can Travel to the Station next Station by
     * Underground
     *
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithUnderground(Station nextStation) {
        return this.currentStation.getTube().contains(nextStation) && this.undergroundTickets > 0;
    }

    /**
     * Checks if the Player can Travel to the Station next Station by Bus
     *
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithBus(Station nextStation) {
        return this.currentStation.getBus().contains(nextStation) && this.busTickets > 0;
    }

    /**
     * Checks if the Player can Travel to the Station next Station by Taxi
     *
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithTaxi(Station nextStation) {
        return this.currentStation.getCab().contains(nextStation) && this.taxiTickets > 0;
    }

    /**
     * Checks if the Player can Travel to the Station next Station by Boat
     * (Black ticket)
     *
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithboat(Station nextStation) {
        return false;
    }

    /**
     * Checks if the Player can Travel to the Station next Station by Black
     * Ticket
     *
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithBlack(Station nextStation) {
        return false;
    }

    /**
     * Checks if the Player can Travel to the Station next Station from
     * fromStation by Underground
     *
     * @param fromStation Station where we Travel from
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithUndergroundFrom(Station fromStation, Station nextStation) {
        return fromStation.getTube().contains(nextStation) && this.undergroundTickets > 0;
    }

    /**
     * Checks if the Player can Travel to the Station next Station from
     * fromStation by Bus
     *
     * @param fromStation Station where we Travel from
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithBusFrom(Station fromStation, Station nextStation) {
        return fromStation.getBus().contains(nextStation) && this.busTickets > 0;
    }

    /**
     * Checks if the Player can Travel to the Station next Station from
     * fromStation by Taxi
     *
     * @param fromStation Station where we Travel from
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithTaxiFrom(Station fromStation, Station nextStation) {
        return fromStation.getCab().contains(nextStation) && this.taxiTickets > 0;
    }

    /**
     * Checks if the Player can Travel to the Station next Station from
     * fromStation by Boat
     *
     * @param fromStation Station where we Travel from
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithboatFrom(Station fromStation, Station nextStation) {
        return false;
    }

    /**
     * Checks if the Player can Travel to the Station next Station from
     * fromStation by Black
     *
     * @param fromStation Station where we Travel from
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithBlackFrom(Station fromStation, Station nextStation) {
        return false;
    }

    /**
     * Checks if the Player can Travel to the Station next Station from
     * fromStation at all
     *
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelWithTicket(Station nextStation) {

        return this.canTravelWithBlack(nextStation) || this.canTravelWithBus(nextStation)
                || this.canTravelWithTaxi(nextStation) || this.canTravelWithUnderground(nextStation)
                || this.canTravelWithboat(nextStation);
    }

    /**
     * Checks if the Player can Travel to the Station next Station from
     * fromStation at all
     *
     * @param fromStation Station where we Travel from
     * @param nextStation the Station the Plyer want to travel to
     * @return boolean that is true, the player can travel
     */
    boolean canTravelFromWithTicket(Station fromStation, Station nextStation) {

        return this.canTravelWithBlackFrom(fromStation, nextStation) || this.canTravelWithBusFrom(fromStation, nextStation)
                || this.canTravelWithTaxiFrom(fromStation, nextStation) || this.canTravelWithUndergroundFrom(fromStation, nextStation)
                || this.canTravelWithboatFrom(fromStation, nextStation);
    }
}
