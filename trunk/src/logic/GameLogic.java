/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * This Class Implements the Game Logic of the Base Game
 *
 * @author Alexander Löffler
 */
public class GameLogic {

    //*************************************** attributes ***************************************//
    private final int animationSpeed = 14;
    private final int lastRound = 24;
    private final double smallestDist = 10;

    private Set<Station> possiblePositions[] = new HashSet[25];
    private GUIConnector gui;
    private Player[] players;
    private Mister mister;
    private NetJSON mapJson;
    private Net net;
    private Log logger;
    private Ai ai = null;

    private boolean detectiveWon = false;
    private boolean blocker = false;
    private boolean isWon = false;
    private boolean cheatActive = false;

    private int turn = 1;
    private int playerCount;
    private int currentPlayer;

    //*************************************** class ***************************************//
    /**
     * Constructor for GameLogic
     *
     * @param gui Refrence to the GUI
     */
    public GameLogic(GUIConnector gui) {

        this.gui = gui;

        // Loads the Map
        try {
            this.loadMap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        logger = new Log();
    }

    /**
     * Simple getter to get the Status for running Animations
     *
     * @return true if Animations are Running, flase if not
     */
    public boolean blockerForAnimation() {
        return blocker;
    }

    //*************************************** main ***************************************//
    /**
     * Start a new Game of Scotland Yard and initializes
     *
     * @param detectivesNumber Is the nummber of detectives Playing the Game
     * @param detectiveAi Boolean if the Detectives are Played by AI (true if
     * they are)
     * @param misterAi Boolean if Mister X is Player by AI (true if he is)
     * @param turn The current turn for the Game (1 in most cases)
     */
    public void startNewGame(int detectivesNumber, boolean detectiveAi, boolean misterAi, int turn) {

        initMap();

        logger = new Log();

        // Playercount is Detective Number + 1 for Mister
        this.playerCount = detectivesNumber + 1;

        this.players = new Player[this.playerCount];

        // Availiable Stations for possible Start Position
        int startPositions[] = new int[]{13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 132, 138, 141, 155, 174, 197, 198};

        // Shuffles the Array for random Positions
        shuffleArray(startPositions);

        // Inits MisterX
        this.mister = new Mister(misterAi, 4, 3, 3, playerCount - 1, net.getStations()[startPositions[0]]);
        this.players[0] = this.mister;
        this.possiblePositions[0] = new HashSet<>();
        this.possiblePositions[0].add(this.mister.getCurrentStation());

        Player dets[] = new Player[this.playerCount - 1];

        // Initalises Detectives
        for (int i = 1; i <= detectivesNumber; i++) {
            this.players[i] = new Detective(this.mister, detectiveAi, 10, 8, 4, net.getStations()[startPositions[i]]);
            this.players[i].setAi(detectiveAi);
            dets[i - 1] = players[i];
        }

        this.turn = turn;
        this.isWon = false;

        // Sets the current Player (0 is always Mister)
        this.currentPlayer = 0;

        this.gui.initsFigures(this.playerCount);

        // Inits Game Ai
        ai = new Ai(net, dets, playerCount, mister, logger);

        // Writes the Beginngin Line for the Log
        try {
            int detectivesStartPos[] = new int[detectivesNumber];

            for (int i = 0; i < detectivesNumber; i++) {
                detectivesStartPos[i] = players[i + 1].getCurrentStation().getIdentifier();
            }
            logger.writeLogBeginning(detectivesNumber, misterAi, detectiveAi, mister.getCurrentStation().getIdentifier(), detectivesStartPos);
        } catch (Exception e) {
            System.err.print(e);
        }

        this.updateAllFigures();
        this.loadPlayerToGuiAndMakeTurn();
    }

    /**
     * Loads a Game of Scotland Yard from data and Initializes
     *
     * @param data Game Files to load
     */
    public void loadNewGame(SaveGame data) {

        if (data != null) {

            try {
                
           
            initMap();

            logger = new Log();

            this.playerCount = data.getDetectiveCount() + 1;

            this.players = new Player[this.playerCount];

            // Sets the current Turn
            this.turn = data.getTurn();
            this.isWon = data.isIsWon();
            // Sets the current Player
            this.currentPlayer = data.getWhosTurn();

            // Inits Mister
            this.mister = new Mister(data.isIsMisterAi(), data.getMister().getTaxiTickets(), data.getMister().getBusTickets(), data.getMister().getUndergroundTickets(), data.getMister().getBlackTickets(), this.net.getStations()[data.getMister().getMisterCurrentStation().getIdentifier() - 1]);
            this.mister.setBillboardValues(data.getMister().getBillboardValues());
            this.players[0] = this.mister;

            Player dets[] = new Player[this.playerCount - 1];

            // Inits Detectives
            for (int i = 1; i < playerCount; i++) {
                this.players[i] = new Detective(this.mister, data.isIsDetectiveAi(), data.getDetectives()[i - 1].getTaxiTickets(), data.getDetectives()[i - 1].getBusTickets(), data.getDetectives()[i - 1].getUndergroundTickets(), this.net.getStations()[data.getDetectives()[i - 1].getCurrentStation().getIdentifier() - 1]);
                dets[i - 1] = players[i];
            }

            ai = new Ai(net, dets, playerCount, mister, logger);

            this.gui.initsFigures(this.playerCount);

            // Loads all Possible Positions for Mister into the Game
            for (int i = 0; i < turn; i++) {

                possiblePositions[i] = new HashSet<>();

                for (int j = 0; j < data.getMister().getPossiblePositions()[i].length; j++) {
                    if (data.getMister().getPossiblePositions()[i][j] != null) {
                        this.possiblePositions[i].add(this.net.getStations()[data.getMister().getPossiblePositions()[i][j].getIdentifier() - 1]);
                    }
                }
            }

            //this.mister.get
            this.updateAllFigures();

            // Inits the GUI Billboard for the Tickets and Loads the correct ones for every turn
            for (int i = 1; i <= this.turn; i++) {

                switch (data.getMister().getBillboardValues()[i - 1]) {
                    case 0: {
                        this.gui.setTicketAt(i, Tickets.Underground);
                        break;
                    }
                    case 1: {
                        this.gui.setTicketAt(i, Tickets.Bus);
                        break;
                    }
                    case 2: {
                        this.gui.setTicketAt(i, Tickets.Taxi);
                        break;
                    }
                    case 3: {
                        this.gui.setTicketAt(i, Tickets.Black);
                        break;
                    }
                }
            }

            this.loadPlayerToGuiAndMakeTurn();

             } catch (Exception e) {
                 gui.displayInfo(5);
            }
            
            
        }
            
            
    }

    /**
     * Checks if it is Possible for the current Player to travel to the Station
     * nearest to the Coordinates
     *
     * @param x Coordinate for the Station
     * @param y Coordinate for the Station
     */
    public void checkAndMoveToStation(double x, double y) {

        // Checks if the Game if over
        if (!isWon) {

            // Get the closest Station to given Coordinates
            Station nextStation = this.net.getClosestStationTo(x, y);

            // The Ticket the player should use to travel
            Tickets ticketToUse = Tickets.None;

            // If the current Player cant move anymore, the Station is null ir visited display Alert to GUI
            if (!this.getCurrentPlayer().isMovePossible() || nextStation == null || nextStation.getIsVisited()) {

                if (nextStation.getIsVisited()) {
                    this.gui.displayInfo(3);
                }

                if (!this.getCurrentPlayer().isMovePossible()) {
                    this.gui.displayInfo(2);
                    nextTurn();
                }

                if (getCurrentPlayer().isAi()) {
                    nextTurn();
                }
            } else if (nextStation != null && nextStation != this.getCurrentPlayer().getCurrentStation()) {

                // If the Player is not an AI mark the found Station inside the GUI
                if (!isCurrentPlayerAi()) {
                    this.gui.setCircleAt(nextStation.getPosition().getX(), nextStation.getPosition().getY());
                }

                // Number of vehicles to use
                int availableVehicles = 0;

                // Checks if the Station is accessable for the diffrent Tickets and vehicles 
                boolean undergroundAvailable = this.getCurrentPlayer().canTravelWithUnderground(nextStation);
                if (undergroundAvailable) {

                    availableVehicles++;
                    ticketToUse = Tickets.Underground;
                }

                boolean busAvailable = this.getCurrentPlayer().canTravelWithBus(nextStation);
                if (busAvailable) {

                    availableVehicles++;
                    ticketToUse = Tickets.Bus;
                }

                boolean taxiAvailable = this.getCurrentPlayer().canTravelWithTaxi(nextStation);
                if (taxiAvailable) {

                    availableVehicles++;
                    ticketToUse = Tickets.Taxi;
                }

                boolean boatAvailable = this.getCurrentPlayer().canTravelWithboat(nextStation);
                if (boatAvailable) {

                    availableVehicles++;
                    ticketToUse = Tickets.Black;
                }

                boolean blackAvailable = this.getCurrentPlayer().canTravelWithBlack(nextStation);
                if (blackAvailable) {

                    availableVehicles++;
                    ticketToUse = Tickets.Black;
                }

                // Depending on the Tickets we are Allowed to Use we perform the Move and open a Ticket Dialog Option if nessesary
                if (availableVehicles == 1) {
                    this.getCurrentPlayer().setNextStation(nextStation);
                    this.moveToStation(ticketToUse);
                } // If we have more than one possible Option opens the Ticket Choose Dialog in GUI (not if player is AI)
                else if (availableVehicles > 1) {

                    if (getCurrentPlayer().isAi()) {
                        this.getCurrentPlayer().setNextStation(nextStation);
                        this.moveToStation(getCurrentPlayer().ticketPlayerHasMost(busAvailable, taxiAvailable, undergroundAvailable, blackAvailable));
                    } else {
                        this.getCurrentPlayer().setNextStation(nextStation);
                        this.gui.openTicketChooseDialog(this, taxiAvailable, busAvailable, undergroundAvailable, blackAvailable,
                                this.getCurrentPlayer().getTaxiTickets(), this.getCurrentPlayer().getBusTickets(),
                                this.getCurrentPlayer().getUndergroundTickets(), this.getCurrentPlayer().getBlackTickets());
                    }
                } else {
                    if (getCurrentPlayer().isAi()) {
                        nextTurn();
                    } else {
                        this.gui.displayInfo(4);
                    }
                }
            }
        }
    }

    /**
     * Check and Move to Station for a Station
     *
     * @param station Station player should travel to
     */
    private void checkAndMoveToStation(Station station) {

        if (!isWon) {
            if (station != null) {
                // Take Coordinates and open main Method
                checkAndMoveToStation(station.getPosition().getX(), station.getPosition().getY());
            }

        }
    }

    /**
     * The main Method to perform the actual Move to a Station for the Current
     * Player
     *
     * @param ticket The ticket the player should use to travel
     */
    public void moveToStation(Tickets ticket) {

        if (!isWon) {
            if (!getCurrentPlayer().isAi()) {

                // Writes Log Line for curent Turn
                try {
                    this.logger.writeLogForTurn(currentPlayer, getCurrentPlayer().getCurrentStation().getIdentifier(), getCurrentPlayer().getNextStation().getIdentifier(),
                            getCurrentPlayer().getUndergroundTickets(), getCurrentPlayer().getBusTickets(), getCurrentPlayer().getTaxiTickets(), getCurrentPlayer().getBlackTickets(), 0, 0.0);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

            // Sets next Station as current Station 
            this.getCurrentPlayer().movePlayerToNextStation();
            getCurrentPlayer().updateVisited();

            // Choose the correct Ticket
            switch (ticket) {
                case Underground: {

                    this.getCurrentPlayer().useUndergroundTicket();
                    break;
                }
                case Taxi: {

                    this.getCurrentPlayer().useTaxiTicket();;
                    break;
                }
                case Bus: {

                    this.getCurrentPlayer().useBusTicket();
                    break;
                }
                case Black: {
                    this.getCurrentPlayer().useBlackTicket();
                    break;
                }
            }

            // Updates Mister attributes in GUI and Current Stations
            if (currentPlayerIsMister()) {
                
                mister.updateMisterVisibility(turn, ticket);
                this.gui.setTicketAt(this.turn, ticket);
                this.calcPossiblePositions();
            }

            this.updateFiguresAndNext();
        }
    }

    /**
     * Performs the main Routine for AI Players
     *
     */
    private void performAi() {

        if (!isWon) {

            // Result Station
            Station nextStation = null;

            // Depending of the Player perform AI Methods
            if (currentPlayer == 0) {
                // Mister AI
                nextStation = ai.misterAi();
            } else {
                // Detective AI
                nextStation = ai.detectiveAi(possiblePositions[turn], closestStationToAveragePossibleStation(), turn, getCurrentPlayer(), currentPlayer);
            }

            // When we have a Valid Station, perform move to Station. When not, next Players turn
            if (nextStation == null) {
                nextTurn();
            } else {
                checkAndMoveToStation(nextStation);
            }
        }
    }

    /**
     * Sets the next Turn for the current Game
     *
     */
    private void nextTurn() {

        if (!gameOver()) {

            this.calcPossiblePositions();

            // Sets the current Player
            this.currentPlayer++;

            // If all Detectives finished their turn, MisterX turn starts again
            if (this.currentPlayer >= this.playerCount) {

                this.turn++;
                this.currentPlayer = 0;

                // Checks if the Game is over
                if (this.turn > this.lastRound || detectiveWon() || misterWon()) {
                    this.gameEnd();
                }

                this.loadPlayerToGuiAndMakeTurn();

            } else {

                // Checks if the Game is over
                if (detectiveWon()) {
                    this.gameEnd();
                }

                this.loadPlayerToGuiAndMakeTurn();

            }
        }
    }

    /**
     * Returns the current State of the Game
     *
     * @return true if the Game is over and not running anymore
     */
    public boolean gameOver() {
        return this.isWon;
    }

    /**
     * Toogles the Cheatmode for displaying Mister inside the GUI
     *
     */
    public void toogleCheatMode() {
        cheatActive = !cheatActive;
        updateAllFigures();
    }

    /**
     * Method for loading a File for a load Game
     *
     * @param fileToLoad the File with the Game Data
     * @throws FileNotFoundException Error if the File Cant be found
     */
    public void loadGame(File fileToLoad) throws FileNotFoundException {

        // Create Gson
        Gson gson = new Gson();

        // File into JSON
        JsonReader reader = new JsonReader(new FileReader(fileToLoad));

        SaveGame data = null;
        
        // Create new SaveGame out of JSON
        try {
             data = gson.fromJson(reader, SaveGame.class);
        } catch (Exception e) {
            this.gui.displayInfo(0);
        }
       

        // Loads a new game with data
        loadNewGame(data);
    }

    /**
     * Saves the current Game into File
     *
     * @throws IOException
     */
    public void saveGame() throws IOException {

        // Create Gson
        Gson gson = new Gson();

        // Create Save Game Structures
        MisterSave misterSave = new MisterSave(this.mapJson.getStation(mister.getCurrentStation().getIdentifier() - 1), mister.getLastShowPosition(), mister.getBlackTickets(), mister.getBusTickets(), mister.getTaxiTickets(), mister.getUndergroundTickets(), mister.getBillboardValues(), possiblePositions, this.mapJson, turn);
        DetectiveSave detectives[] = new DetectiveSave[this.playerCount - 1];

        for (int i = 0; i < this.playerCount - 1; i++) {
            detectives[i] = new DetectiveSave(this.mapJson.getStation(players[i + 1].getCurrentStation().getIdentifier() - 1), players[i + 1].getBusTickets(), players[i + 1].getTaxiTickets(), players[i + 1].getUndergroundTickets());
        }
        SaveGame sg = new SaveGame(misterSave, detectives, turn, isWon, players[1].isAi(), mister.isAi(), playerCount - 1, currentPlayer);

        // Name fot the Savegame
        String filename = "savegame.txt";

        // Convert Object to Json
        String json = gson.toJson(sg);

        // Write into File
        try {
            //write converted json data to a file
            FileWriter writer = new FileWriter(filename);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
             this.gui.displayInfo(6);
        }
    }

    /**
     * Checks if the current Player is controller by AI
     *
     * @return true if he is
     */
    public boolean isCurrentPlayerAi() {

        return getCurrentPlayer().isAi();
    }

    //*************************************** helper ***************************************//
    /**
     * Calculates the possible Positions for Mister (for every turn)
     *
     * @throws IOException
     */
    private void calcPossiblePositions() {

        // Inits array for current Turn
        possiblePositions[turn] = new HashSet<>();

        // Only after turn 3
        if (mister.getLastSeenRound() != 0) {

            // If Mister is visible right now we dont need to calculate
            if (mister.isMisterVisible(turn)) {
                possiblePositions[turn].add(mister.getCurrentStation());
            } else {

                // Adds all Possible Stations depending on the used Ticket
                for (Station station : possiblePositions[turn - 1]) {

                    switch (mister.getBillboardValues()[turn - 1]) {

                        case 0: {
                            possiblePositions[turn].addAll(station.getTube());
                            break;
                        }
                        case 1: {
                            possiblePositions[turn].addAll(station.getBus());
                            break;
                        }
                        case 2: {
                            possiblePositions[turn].addAll(station.getCab());
                            break;
                        }
                        case 3: {
                            possiblePositions[turn].addAll(station.getBoat());
                            break;
                        }
                    }
                }

                // Removes all Stations where Detectives currently are
                for (int i = 1; i < playerCount; i++) {
                    possiblePositions[turn].remove(players[i].getCurrentStation());
                }
            }
        }
    }

    /**
     * Calculates the Average X Coordinate for the Average Station
     *
     * @return mean Y coordinate
     */
    private double calcAveragePossibleStationX() {

        int numberOfPossibleStations = possiblePositions[turn].size();
        double x = 0;

        // Calculates the mean
        for (Station station : possiblePositions[turn]) {
            x += station.getPosition().getX();
        }

        if (numberOfPossibleStations == 0) {
            return 0;
        } else {

            x = x / Double.valueOf(numberOfPossibleStations);
            return x;
        }
    }

    /**
     * Calculates the Average Y Coordinate for the Average Station
     *
     * @return mean Y coordinate
     */
    private double calcAveragePossibleStationY() {

        int numberOfPossibleStations = possiblePositions[turn].size();
        double y = 0;

        // Calculates the mean
        for (Station station : possiblePositions[turn]) {
            y += station.getPosition().getY();
        }

        if (numberOfPossibleStations == 0) {
            return 0;
        } else {

            y = y / Double.valueOf(numberOfPossibleStations);

            return y;
        }
    }

    /**
     * Calculates the closest Station the the Average Possible Position
     *
     * @return Station closest to AveragePossiblePosition
     */
    private Station closestStationToAveragePossibleStation() {

        Station nextStation = null;

        // Only neccesary if its turn 3+
        if (mister.getLastSeenRound() != 0) {

            double x = calcAveragePossibleStationX();
            double y = calcAveragePossibleStationY();

            double smallestDistance = smallestDist;
            double distance;

            // Gets the closest Station to the avg Possible Position
            for (Station station : possiblePositions[turn]) {

                distance = net.calcDistanceStations(x, y, station.getPosition().getX(), station.getPosition().getY());
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    nextStation = station;
                }
            }
        }

        return nextStation;
    }

    /**
     * Inits the Map
     */
    private void initMap() {

        // Inits the GameMap with the JSON parsed Map
        this.net = new Net(this.mapJson);
    }

    /**
     * Loads the map from file netz.json
     *
     */
    private void loadMap() throws FileNotFoundException {

        Gson gson = new Gson();
        InputStream myInputStream = this.getClass().getResourceAsStream("data/netz.json");
        InputStreamReader reader = new InputStreamReader(myInputStream);
        NetJSON newNet = gson.fromJson(reader, NetJSON.class);
        this.mapJson = newNet;
    }

    /**
     * Shulles the Array using Fisher Yates Shuffle
     *
     * @param array the Array to shuffle
     */
    private static void shuffleArray(int[] array) {
        int index;
        Random random = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }

    /**
     * Checks if detectives have won
     *
     * @return true if detectives have won
     */
    private boolean detectiveWon() {
        if (mister.getCurrentStation().getIsVisited()) {
            this.detectiveWon = true;
            return true;
        } else {
            return false;
        }
    }
    
     /**
     * Checks if Mister has won
     *
     * @return true if detectives have won
     */
    private boolean misterWon() {
        
        int detsThatCantMove = 0;
        
        detectiveWon = false;
        
        for (Player player : players) {
            
            if (!player.isMovePossible()) {
                detsThatCantMove++;
            }
        }
        
        if (detsThatCantMove == playerCount-1 || (turn == 24 && currentPlayer > 0)) {
            return true;
        }
        else {
            return false;
        }
     
    }

    /**
     * Checks if the current Player is Mister X
     *
     * @return true if the is
     */
    private boolean currentPlayerIsMister() {

        return (this.currentPlayer == 0);
    }

    //*************************************** GUI ***************************************//
    /**
     * Update all Figures inside GUI Instantly
     */
    private void updateAllFigures() {

        for (int i = 0; i < playerCount; i++) {
            this.gui.setFigureAt(this.players[i].getCurrentStation().getPosition().getX(), this.players[i].getCurrentStation().getPosition().getY(), i, turn, mister.isAi(), cheatActive, getCurrentPlayer().isAi());
        }
    }
    
      public KeyFrame getKeyFrame(double xCord, double yCord, int playerNumber, int turn, boolean misterIsAi, boolean cheatActive, boolean playerIsAi) {
        //alle gleichzeitig zu aktualisierenden Anzeigen werden hier gesetzt
        EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                gui.setFigureAt(xCord, yCord, playerNumber, turn, misterIsAi, cheatActive, playerIsAi);

            }
        };
        return new KeyFrame(Duration.millis((1) * 1000), eh);
    }

    /**
     * Update Figures inside the GUI and inits the next Turn
     *
     */
    private void updateFiguresAndNext() {

        if (isCurrentPlayerAi()) {

            // Creates a Timeline for GUI Animations
            Timeline timeline = new Timeline(this.getKeyFrame(this.players[currentPlayer].getCurrentStation().getPosition().getX(), this.players[currentPlayer].getCurrentStation().getPosition().getY(), currentPlayer, turn, mister.isAi(), cheatActive, getCurrentPlayer().isAi()),
                    finishFrame(animationSpeed));
            blocker = true;
            timeline.play();
        } else {
            this.gui.setFigureAt(this.players[currentPlayer].getCurrentStation().getPosition().getX(), this.players[currentPlayer].getCurrentStation().getPosition().getY(), currentPlayer, turn, mister.isAi(), cheatActive, getCurrentPlayer().isAi());
            nextTurn();
        }
    }
    
    public KeyFrame setStatusKeyframe(int roundNumber, int playerNumber) {
        //alle gleichzeitig zu aktualisierenden Anzeigen werden hier gesetzt
        EventHandler<ActionEvent> toDo = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String playerName = "";

                gui.setStatus(roundNumber, playerNumber);

            }
        };

        return new KeyFrame(Duration.millis((1) * 1000), toDo);
    }

    private void loadPlayerToGuiAndMakeTurn() {

        // Updates GUI and start animation for AI players. No Animation if its human player
        if (isCurrentPlayerAi()) {

            Timeline timeline = new Timeline(this.setStatusKeyframe(this.turn, this.currentPlayer),
                    finishFrameAi(animationSpeed));
            this.gui.setStatus(this.turn, this.currentPlayer);
            this.gui.setTicketNumber(this.getCurrentPlayer().getTaxiTickets(), Tickets.Taxi);
            this.gui.setTicketNumber(this.getCurrentPlayer().getBusTickets(), Tickets.Bus);
            this.gui.setTicketNumber(this.getCurrentPlayer().getUndergroundTickets(), Tickets.Underground);
            this.gui.setTicketNumber(this.getCurrentPlayer().getBlackTickets(), Tickets.Black);
            this.blocker = true;
            timeline.play();

        } else {

            this.gui.setStatus(this.turn, this.currentPlayer);
            this.gui.setTicketNumber(this.getCurrentPlayer().getTaxiTickets(), Tickets.Taxi);
            this.gui.setTicketNumber(this.getCurrentPlayer().getBusTickets(), Tickets.Bus);
            this.gui.setTicketNumber(this.getCurrentPlayer().getUndergroundTickets(), Tickets.Underground);
            this.gui.setTicketNumber(this.getCurrentPlayer().getBlackTickets(), Tickets.Black);

        }
    }

    /**
     * Gets the current Player
     *
     * @return the current Player
     */
    private Player getCurrentPlayer() {

        return this.players[this.currentPlayer];
    }

    /**
     * Keyframe for the end of the information and unlock and gets the next turn
     *
     * @param noOfPauses count ouf pauses
     * @return KeyFrame with animation end
     */
    private KeyFrame finishFrame(int noOfPauses) {

        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                blocker = false;
                nextTurn();
            }
        };
        return new KeyFrame(Duration.millis((noOfPauses) * 100), onFinished);
    }

    /**
     * Keyframe for the end of the information and unlock and gets the next turn
     *
     * @param noOfPauses count ouf pauses
     * @return KeyFrame with animation end
     */
    private KeyFrame finishFrameAi(int noOfPauses) {

        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                blocker = false;
                performAi();
            }
        };
        return new KeyFrame(Duration.millis((noOfPauses) * 100), onFinished);
    }

    /**
     * Sets the game End and Display info in GUI
     */
    private void gameEnd() {
        this.isWon = true;
        if (!detectiveWon) {
            this.gui.displayInfo(0);
        } else {
            this.gui.displayInfo(1);
        }
    }

}
