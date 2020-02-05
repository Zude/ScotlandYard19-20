/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

import javafx.animation.KeyFrame;
import javafx.scene.layout.AnchorPane;

/**
 * This Class connects the GUI and the Logic of the Game
 *
 * @author Alexander Löffler
 */
public interface GUIConnector {

    /**
     *  Maximum Number of Billboards
     */
    final int MAXBILLBOARDS = 24;

    /**
     *  Minimun Number of Billboards
     */
    final int MINBILLBOARDS = 1;
    
    /**
     * Opens the Dialog for the User to Choose witch Ticket to use for the next Station
     * 
     * @param gl The GameLogic
     * @param taxi Bool if Taxi Ticket is Available
     * @param bus Bool if Bus Ticket is Available
     * @param underground Bool if Underground Ticket is Available
     * @param black Bool if Black Ticket is Available
     * @param taxiTicketNumber Number of Taxi Tickets
     * @param busTicketNumber Number of Bus Tickets
     * @param ungergroundTicketNumber Number of Underground Tickets
     * @param blackTicketNumber Number of Black Tickets
     */
    public void openTicketChooseDialog(GameLogic gl, boolean taxi, boolean bus, boolean underground, boolean black, int taxiTicketNumber, int busTicketNumber, int ungergroundTicketNumber, int blackTicketNumber);
    
    /**
     * Changes the Image inside the Ticket Billboard to a Specific Ticket
     *
     * @param billboardNumber the Number of the Billboard (corresposing to the
     * Turn Number 1-24)
     * @param ticketKind Sets what ticket should be blaced on the Billboard
     */
    public void setTicketAt(int billboardNumber, Tickets ticketKind);

    /**
     * Changes the Number of Tickets for a special Ticket
     *
     * @param ticketNumber Number of Tickets
     * @param ticketKind Sets what kind of Ticket we got
     */
    public void setTicketNumber(int ticketNumber, Tickets ticketKind);
    
    /**
     * Changes the current Status inside the Status Label
     *
     * @param roundNumber Current Turn Number
     * @param playerNumber The Number of the Player (1-3: Detective, 4: Mr. X)
     */
    public void setStatus (int roundNumber, int playerNumber);
    
    /**
     * Changes the current Status inside the Status Label
     *
     * @param xCord x Coordinate
     * @param yCord y Coordinate
     * @param playerNumber The Number of the Player (1-3: Detective, 4: Mr. X)
     * @param turn current Turn
     * @param misterIsAi bool if mister is AI controller
     * @param cheatActive bool if Cheat mode is Active
     * @param playerIsAi bool if Player is controller by AI
     */
    public void setFigureAt (double xCord, double yCord, int playerNumber, int turn, boolean misterIsAi, boolean cheatActive, boolean playerIsAi);
    
    /**
     * Inits Figures inside the Map 
     * 
     * @param numberOfPlayers Number of Players
     */
    public void initsFigures (int numberOfPlayers);
    
    /**
     *  Displays User Informatin for Diffrent Scenarios 
     * 
     * @param type represents the Type of Message the User will see 
     */
    public void displayInfo(int type);
    
    /**
     * Flushes the Billboard
     */
    public void cleanBillboards();
    
    /**
     *  Sets a Small Circle at given Position to Highlight a Station
     * 
     * @param xCord x Coordinate
     * @param yCord y Coordinate
     */
    public void setCircleAt(double xCord, double yCord);
    

   
}
