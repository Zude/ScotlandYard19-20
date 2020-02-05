/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *

 */
package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import logic.GUIConnector;
import logic.GameLogic;
import java.io.IOException;

/**
 * This Class is the Main FXML Controller. It reacts to Actions inside the GUI
 *
 * @author Alexander Löffler
 */
public class FXMLDocumentController implements Initializable {

    //*************************************** FXML ***************************************//
    @FXML
    Label label;

    @FXML
    ImageView map;

    @FXML
    AnchorPane mapPane;

    @FXML
    ImageView gl1;

    @FXML
    ImageView gl2;

    @FXML
    ImageView gl3;

    @FXML
    ImageView gl4;

    @FXML
    ImageView gl5;

    @FXML
    ImageView gl6;

    @FXML
    ImageView gl7;

    @FXML
    ImageView gl8;

    @FXML
    ImageView gl9;

    @FXML
    ImageView gl10;

    @FXML
    ImageView gl11;

    @FXML
    ImageView gl12;

    @FXML
    ImageView gl13;

    @FXML
    ImageView gl14;

    @FXML
    ImageView gl15;

    @FXML
    ImageView gl16;

    @FXML
    ImageView gl17;

    @FXML
    ImageView gl18;

    @FXML
    ImageView gl19;

    @FXML
    ImageView gl20;

    @FXML
    ImageView gl21;

    @FXML
    ImageView gl22;

    @FXML
    ImageView gl23;

    @FXML
    ImageView gl24;

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

    ImageView billboardArray[];

    GUIConnector gc;
    GameLogic gl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Fills the Array with all Billboards for passing (gl represents the diffren billboard Elements)
        this.billboardArray = new ImageView[]{gl1, gl2, gl3, gl4, gl5, gl6, gl7, gl8, gl9, gl10, gl11, gl12, gl13, gl14, gl15, gl16, gl17, gl18, gl19, gl20, gl21, gl22, gl23, gl24
        };

        // Creates a new Instance of the GUI
        gc = new JavaFXGUI(turnLabel, this.playerLabel, label, map, mapPane, this.billboardArray, this.busTicketsLabel, this.undergroundTicketsLabel, this.taxiTicketsLabel, this.blackTicketsLabel);
        // Creates a new Instance of the gameLogic
        gl = new GameLogic(gc);

    }

    //*************************************** Class ***************************************//
    @FXML
    /**
     * Opens New Game Dialog
     *
     * @param event Action Event
     */
    private void newGameClicked(ActionEvent event) throws IOException {

        DialogController controller2 = new DialogController(this);
    }

    @FXML
    /**
     * Saves a Game
     *
     * @param event Action Event
     */
    private void saveGameClicked(ActionEvent event) throws IOException {

        // Trigger Savegame von der Logic
        this.gl.saveGame();
    }

    @FXML
    /**
     * Loads a new Game
     *
     * @param event Action Event
     */
    private void loadGameClicked(ActionEvent event) throws FileNotFoundException {

        File fileToLoad = this.fileChooser();

        this.gc.cleanBillboards();

        this.gl.loadGame(fileToLoad);
    }

    @FXML
    /**
     * Toogles Cheat Mode
     *
     * @param event Action Event
     */
    private void cheatModeClicked(ActionEvent event) {
        this.gl.toogleCheatMode();
    }

    @FXML
    /**
     * Passes a Click on the Map to Logic
     *
     * @param event Action Event
     */
    private void handleMouseClickPane(MouseEvent event) {

        // Only if the Player is not played by AI, Blocked or the Game is over
        if (!this.gl.isCurrentPlayerAi()) {

            double xNorm = event.getX() / this.mapPane.getWidth();
            double yNorm = event.getY() / this.mapPane.getHeight();

            if (!this.gl.blockerForAnimation() || this.gl.gameOver()) {
                this.gl.checkAndMoveToStation(xNorm, yNorm);
            }

        }
    }

    /**
     * File Chooser Dialog
     *
     * @param event Action Event
     */
    private File fileChooser() {

        File currDir = null;
        try {
            currDir = new File(FXMLDocumentController.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        } catch (URISyntaxException ex) {

        }

        FileChooser fileChooser = new FileChooser();
        if (currDir != null) {

            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.setTitle("Open JSON Graph-File");

        return fileChooser.showOpenDialog(this.mapPane.getScene().getWindow());
    }

    /**
     * Toogles the Start of a new Game
     *
     * @param detectiveAI Boolean if the Checkbox is Selected
     * @param misterAI Boolean if the Checkbox is Selected
     * @param detectiveCount Gets the number of selected detectives
     */
    public void startNewGame(boolean detectiveAI, boolean misterAI, int detectiveCount) {


        this.gc.cleanBillboards();

        // Inits a new Game with given properties and Turn 1
        this.gl.startNewGame(detectiveCount, detectiveAI, misterAI, 1);

    }
}
