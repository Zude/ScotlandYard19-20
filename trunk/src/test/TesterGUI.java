/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 * This Class connects the GUI and the Logic of the Game
 */
package test;

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
public class TesterGUI implements GUIConnector {

    @Override
    public void openTicketChooseDialog(GameLogic gl, boolean taxi, boolean bus, boolean underground, boolean black, int taxiTicketNumber, int busTicketNumber, int ungergroundTicketNumber, int blackTicketNumber) {
        
    }

    @Override
    public void setTicketAt(int billboardNumber, Tickets ticketKind) {
        
    }

    @Override
    public void setTicketNumber(int ticketNumber, Tickets ticketKind) {
      
    }

    @Override
    public void setStatus(int roundNumber, int playerNumber) {
      
    }

    @Override
    public void setFigureAt(double xCord, double yCord, int playerNumber, int turn, boolean misterIsAi, boolean cheatActive, boolean playerIsAi) {
     
    }

    @Override
    public void initsFigures(int numberOfPlayers) {
       
    }

    @Override
    public void displayInfo(int type) {
        
    }

    @Override
    public void cleanBillboards() {
       
    }

    @Override
    public void setCircleAt(double xCord, double yCord) {
        
    }

    }