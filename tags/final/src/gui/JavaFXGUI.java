/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 * This Class connects the GUI and the Logic of the Game
 */
package gui;

import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import logic.GUIConnector;
import logic.GameLogic;
import logic.Tickets;

/**
 * This Class connects the GUI and the Logic of the Game
 *
 * @author Alexander Löffler
 */
public class JavaFXGUI implements GUIConnector {

    @FXML
    private Label label;

    @FXML
    ImageView map;

    @FXML
    public AnchorPane mapPane;

    @FXML
    Label undergroundTicketsLabel;

    @FXML
    Label busTicketsLabel;

    @FXML
    Label taxiTicketsLabel;

    @FXML
    Label blackTicketsLabel;

    @FXML
    Label turnLabel;

    @FXML
    Label playerLabel;

    ImageView ticketBillboards[];

    private final int figureX = 30;
    private final int figureY = 30;

    private int maxPossiblePlayers = 6;

    // Inits Tickets
    Image blackTicket = new Image("/gui/images/BlackTicket.jpg");
    Image busTicket = new Image("/gui/images/Bus.jpg");
    Image undergroundTicket = new Image("/gui/images/Underground.jpg");
    Image taxiTicket = new Image("/gui/images/Taxi.jpg");
    Image startTicket = new Image("/gui/images/BlackTicket.jpg");

    // Inits Player Icons
    Image player1 = new Image("/gui/images/newPlayer1.png");
    Image player2 = new Image("/gui/images/newPlayer2.png");
    Image player3 = new Image("/gui/images/newPlayer3.png");
    Image player4 = new Image("/gui/images/newPlayer4.png");
    Image player5 = new Image("/gui/images/newPlayer5.png");
    Image mister = new Image("/gui/images/mister.png");

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView0;

    StackPane playerStack = new StackPane();

    // Collects all possible imageViews inside an Array
    ImageView imageViews[] = new ImageView[this.maxPossiblePlayers];

    Group playerFiguresGroup = new Group();

    Group figuresGroup = new Group();

    Group circleGroup = new Group();

    boolean blocker = false;

    public JavaFXGUI(Label turnLabel, Label playerLabel, Label label, ImageView map, AnchorPane mapPane, ImageView ticketsBillBoards[], Label busTickets, Label undergroundTickets, Label taxiTickets, Label blackTickets) {

        this.turnLabel = turnLabel;
        this.label = label;
        this.map = map;
        this.mapPane = mapPane;
        this.ticketBillboards = ticketsBillBoards;
        this.undergroundTicketsLabel = undergroundTickets;
        this.busTicketsLabel = busTickets;
        this.taxiTicketsLabel = taxiTickets;
        this.blackTicketsLabel = blackTickets;

        this.playerLabel = playerLabel;

        // Make the Map resizable depending on Window Size
        this.map.setPreserveRatio(false);
        this.map.fitWidthProperty().bind(this.mapPane.widthProperty());
        this.map.fitHeightProperty().bind(this.mapPane.heightProperty());

    }

    @Override
    public void setTicketAt(int billboardNumber, Tickets ticketKind) {

        switch (ticketKind) {
            case Black: {
                this.ticketBillboards[billboardNumber - 1].setImage(this.blackTicket);
                break;
            }
            case Bus: {
                this.ticketBillboards[billboardNumber - 1].setImage(this.busTicket);
                break;
            }
            case Taxi: {
                this.ticketBillboards[billboardNumber - 1].setImage(this.taxiTicket);
                break;
            }
            case Underground: {
                this.ticketBillboards[billboardNumber - 1].setImage(this.undergroundTicket);
                break;
            }

        }
    }

    @Override
    public void openTicketChooseDialog(GameLogic gl, boolean taxi, boolean bus, boolean underground, boolean black, int taxiTicketNumber, int busTicketNumber, int ungergroundTicketNumber, int blackTicketNumber) {

        // Opens and Inits the TicketDialogController
        DialogTicketController controller2 = new DialogTicketController(gl, (JavaFXGUI) this, taxi, bus, underground, black, taxiTicketNumber, busTicketNumber, ungergroundTicketNumber, blackTicketNumber);
    }

    @Override
    public void setTicketNumber(int ticketNumber, Tickets ticketKind) {

        switch (ticketKind) {
            case Black: {
                this.blackTicketsLabel.setText(String.valueOf(ticketNumber));
                break;
            }
            case Bus: {
                this.busTicketsLabel.setText(String.valueOf(ticketNumber));
                break;
            }
            case Taxi: {
                this.taxiTicketsLabel.setText(String.valueOf(ticketNumber));
                break;
            }
            case Underground: {
                this.undergroundTicketsLabel.setText(String.valueOf(ticketNumber));
                break;
            }

        }
    }

    @Override
    public void setStatus(int roundNumber, int playerNumber) {

        String playerName = "";

        if (playerNumber == 0) {
            playerName = "MisterX";
        } else if (playerNumber > 0 && playerNumber <= 5) {
            playerName = "Detektiv" + String.valueOf(playerNumber);
        } else {
        }

        playerName = playerName + " ist am Zug";

        this.playerLabel.setText(playerName);
        this.turnLabel.setText("Runde: " + String.valueOf(roundNumber));

    }

    

    public KeyFrame setStatusKeyframe(int roundNumber, int playerNumber) {
        //alle gleichzeitig zu aktualisierenden Anzeigen werden hier gesetzt
        EventHandler<ActionEvent> toDo = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String playerName = "";

                setStatus(roundNumber, playerNumber);

            }
        };

        return new KeyFrame(Duration.millis((1) * 1000), toDo);
    }

    @Override
    public void setCircleAt(double xCord, double yCord) {

        circleGroup.getChildren().clear();

        Circle circle = new Circle(xCord, yCord, 12);

        circle.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle.setStroke(javafx.scene.paint.Color.BLACK);

        circle.centerXProperty().bind(this.mapPane.widthProperty().multiply(xCord));
        circle.centerYProperty().bind(this.mapPane.heightProperty().multiply(yCord));

        circleGroup.getChildren().add(circle);
    }
    
    public KeyFrame getKeyFrame(double xCord, double yCord, int playerNumber, int turn, boolean misterIsAi, boolean cheatActive, boolean playerIsAi) {
        //alle gleichzeitig zu aktualisierenden Anzeigen werden hier gesetzt
        EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                
                
                // Sets the Position with Animation and Show Mister if he is in the right round
                if ((playerNumber == 0 && (turn == 3 || turn == 8 || turn == 13 || turn == 18 || turn == 24) || !misterIsAi || cheatActive)
                        || (playerNumber != 0)) {

                    imageViews[playerNumber].setVisible(true);

                    imageViews[playerNumber].translateXProperty().bind(mapPane.widthProperty().multiply(xCord).subtract(figureX / 2));
                    imageViews[playerNumber].translateYProperty().bind(mapPane.heightProperty().multiply(yCord).subtract(figureY / 2));
                } else {
                    imageViews[playerNumber].setVisible(false);
                }

            }
        };
        return new KeyFrame(Duration.millis((1) * 1000), eh);
    }

    @Override
    public void setFigureAt(double xCord, double yCord, int playerNumber, int turn, boolean misterIsAi, boolean cheatActive, boolean playerIsAi) {

        
        
        if ((playerNumber == 0 && (turn == 3 || turn == 8 || turn == 13 || turn == 18 || turn == 24) || !misterIsAi || cheatActive)
                || (playerNumber != 0)) {
            this.imageViews[playerNumber].setVisible(true);
            this.imageViews[playerNumber].translateXProperty().bind(this.mapPane.widthProperty().multiply(xCord).subtract(this.figureX / 2));
            this.imageViews[playerNumber].translateYProperty().bind(this.mapPane.heightProperty().multiply(yCord).subtract(this.figureY / 2));
        } else {
            imageViews[playerNumber].setVisible(false);
        }

    }

    @Override
    public void initsFigures(int numberOfPlayers) {

        this.mapPane.getChildren().removeAll(figuresGroup);
        this.mapPane.getChildren().removeAll(circleGroup);
        this.mapPane.getChildren().add(circleGroup);

        this.figuresGroup = new Group();

        // Initialises all Images and ImageViews depending on the numberOfPlayers
        imageView0 = new ImageView(this.mister);
        imageView1 = new ImageView(this.player1);
        imageView2 = new ImageView(this.player2);
        imageView3 = new ImageView(this.player3);

        imageView0.setPreserveRatio(false);
        imageView0.setFitHeight(figureX);
        imageView0.setFitWidth(figureY);

        imageView1.setPreserveRatio(false);
        imageView1.setFitHeight(figureX);
        imageView1.setFitWidth(figureY);

        imageView2.setPreserveRatio(false);
        imageView2.setFitHeight(figureX);
        imageView2.setFitWidth(figureY);

        imageView3.setPreserveRatio(false);
        imageView3.setFitHeight(figureX);
        imageView3.setFitWidth(figureY);

        this.imageViews[0] = this.imageView0;
        this.imageViews[1] = this.imageView1;
        this.imageViews[2] = this.imageView2;
        this.imageViews[3] = this.imageView3;

        this.imageView0.setVisible(false);

        switch (numberOfPlayers) {
            case 5: {
                imageView4 = new ImageView(this.player4);
                imageView4.setPreserveRatio(false);
                imageView4.setFitHeight(figureX);
                imageView4.setFitWidth(figureY);

                this.imageViews[4] = this.imageView4;
                break;
            }
            case 6: {
                imageView4 = new ImageView(this.player4);
                imageView4.setPreserveRatio(false);
                imageView4.setFitHeight(figureX);
                imageView4.setFitWidth(figureY);

                imageView5 = new ImageView(this.player5);
                imageView5.setPreserveRatio(false);
                imageView5.setFitHeight(figureX);
                imageView5.setFitWidth(figureY);

                this.imageViews[4] = this.imageView4;
                this.imageViews[5] = this.imageView5;
                break;
            }
        }

        this.mapPane.getChildren().add(this.figuresGroup);

        // Sets starting Position for IMageViews
        for (int i = 0; i < numberOfPlayers; i++) {

            this.imageViews[i].translateXProperty().bind(this.mapPane.widthProperty().multiply(0.0).subtract(this.figureX / 2));
            this.imageViews[i].translateYProperty().bind(this.mapPane.heightProperty().multiply(0.0).subtract(this.figureY / 2));
            figuresGroup.getChildren().add(this.imageViews[i]);
        }

        // Hides MisterX
    }

    @Override
    public void displayInfo(int type) {

        Alert alert = new Alert(AlertType.INFORMATION);

        switch (type) {
            case 0: {
                alert.setTitle("Das Spiel ist zu Ende!");
                alert.setHeaderText("Mister X hat gewonnen");
                break;
            }
            case 1: {
                alert.setTitle("Das Spiel ist zu Ende!");
                alert.setHeaderText("Die Detektive haben gewonnen");
                break;
            }
            case 2: {
                alert.setTitle("Achtung");
                alert.setHeaderText("Der Spieler kann sich nicht bewegen. Der Nächste Spieler ist dran!");
                break;
            }
            case 3: {
                alert.setTitle("Achtung");
                alert.setHeaderText("Auf der Station ist ein anderer Spieler!");
                break;
            }
            case 4: {
                alert.setTitle("Achtung");
                alert.setHeaderText("Station nicht erreichbar");
                break;
            }
            case 5: {
                alert.setTitle("Achtung");
                alert.setHeaderText("Spielstand konnte nicht geladen werden!");
                break;
            }
            case 6: {
                alert.setTitle("Achtung");
                alert.setHeaderText("Spiel konnte nicht gespeichert werden!");
                break;
            }
        }

        alert.show();
    }

    @Override
    public void cleanBillboards() {

        for (int i = 1; i <= MAXBILLBOARDS; i++) {
            this.ticketBillboards[i - 1].setImage(null);
        }
    }
}
