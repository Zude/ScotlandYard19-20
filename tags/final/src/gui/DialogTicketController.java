/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.GameLogic;
import logic.Tickets;

/**
 * Controller for the Ticket Dialog
 *
 * @author Alexander Löffler
 */
public class DialogTicketController implements Initializable {

    //*************************************** FXML ***************************************//
    @FXML
    Label blackLabel;

    @FXML
    Label taxiLabel;

    @FXML
    Label busLabel;

    @FXML
    Label undergroundLabel;

    @FXML
    Button busButton;

    @FXML
    Button taxiButton;

    @FXML
    Button undergroundButton;

    @FXML
    Button blackButton;

    //*************************************** attributes ***************************************//
    // Holds this controller's Stage
    private Stage thisStage;

    // Holds the current Player on Ticket chooser Dialog
    private GameLogic gl;

    /**
     * Controller for the Ticket Dialog
     *
     * @param gl Gamelogic
     * @param gui GUI
     * @param taxi bool for Taxi
     * @param bus bool for Bus
     * @param underground bool for Underground
     * @param black bool for Black
     * @param taxiTicketNumber number of Tickets
     * @param busTicketNumber number of Tickets
     * @param undergroundTicketNumber number of Tickets
     * @param blackTicketNumber number of Tickets
     */
    DialogTicketController(GameLogic gl, JavaFXGUI gui, boolean taxi, boolean bus, boolean underground, boolean black, int taxiTicketNumber, int busTicketNumber, int undergroundTicketNumber, int blackTicketNumber) {

        this.gl = gl;
        thisStage = new Stage();

        // Load the FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DialogTicket.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Ticket für die nächste Station wählen");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Checks What Button/Label to show and what to hide (depending on current Ticket Numbers)
        if (bus) {
            this.busLabel.setText(String.valueOf(busTicketNumber) + "x");
        } else {
            this.busLabel.setVisible(false);
            this.busButton.setVisible(false);
        }

        if (taxi) {
            this.taxiLabel.setText(String.valueOf(taxiTicketNumber) + "x");
        } else {
            this.taxiLabel.setVisible(false);
            this.taxiButton.setVisible(false);
        }

        if (underground) {
            this.undergroundLabel.setText(String.valueOf(undergroundTicketNumber) + "x");
        } else {
            this.undergroundLabel.setVisible(false);
            this.undergroundButton.setVisible(false);
        }

        if (black) {
            this.blackLabel.setText(String.valueOf(blackTicketNumber) + "x");
        } else {
            this.blackLabel.setVisible(false);
            this.blackButton.setVisible(false);
        }

        thisStage.show();
    }
    
    //*************************************** class ***************************************//

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    /**
     * Gives the Choosen Ticket back to Logic
     *
     * @param event Action Event
     */
    private void undergroundTicketClicked(ActionEvent event) {

        this.thisStage.close();
        this.gl.moveToStation(Tickets.Underground);

    }

    @FXML
    /**
     * Gives the Choosen Ticket back to Logic
     *
     * @param event Action Event
     */
    private void busTicketClicked(ActionEvent event) {

        this.thisStage.close();
        this.gl.moveToStation(Tickets.Bus);
    }

    @FXML
    /**
     * Gives the Choosen Ticket back to Logic
     *
     * @param event Action Event
     */
    private void taxiTicketClicked(ActionEvent event) {
        this.thisStage.close();
        this.gl.moveToStation(Tickets.Taxi);
    }

    @FXML
    /**
     * Gives the Choosen Ticket back to Logic
     *
     * @param event Action Event
     */
    private void blackTicketClicked(ActionEvent event) {
        this.thisStage.close();
        this.gl.moveToStation(Tickets.Black);

    }

}
