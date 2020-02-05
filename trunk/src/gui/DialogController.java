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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * Controller Class for new Game Dialog
 *
 * @author Alexander Löffler
 */
 class DialogController implements Initializable {

    //*************************************** FXML ***************************************//
    @FXML
    CheckBox checkBoxMister;

    @FXML
    CheckBox checkBoxDetektive;

    @FXML
    ComboBox countCB;

    //*************************************** attributes ***************************************//
    // Holds this controller's Stage
    private Stage thisStage;

    // Will hold a reference to the first controller, allowing us to access the methods found there.
    private final FXMLDocumentController mainController;

    /**
     * Controller Class for new Game Dialog
     *
     * @param controller Main Controller Reference
     */
     DialogController(FXMLDocumentController controller) {

        this.mainController = controller;

        thisStage = new Stage();

        // Load the FXML file
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Dialog.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Neues Spiel erstellen");
        } catch (IOException e) {
            e.printStackTrace();
        }

        thisStage.showAndWait();
    }

    //*************************************** class ***************************************//
    @Override
     public void initialize(URL url, ResourceBundle rb) {

        // Init Values for ComboBox
        countCB.getItems().addAll(
                3,
                4,
                5
        );
    }

    @FXML
    /**
     * Start Game Clicked
     *
     * @param event Action Event
     */
    private void startClicked(ActionEvent event) {
        if (countCB.getValue() != null) {
            //Starts a new Game with given Parameters
            this.mainController.startNewGame(this.checkBoxDetektive.isSelected(), this.checkBoxMister.isSelected(), (int) countCB.getValue());
            this.thisStage.close();
        }
    }

    @FXML
    /**
     * Cancel Clicked
     *
     * @param event Action Event
     */
    private void cancelClicked(ActionEvent event) {

        this.thisStage.close();
    }

}
