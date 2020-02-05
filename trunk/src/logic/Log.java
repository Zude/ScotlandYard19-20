/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Logging class for Game Logging
 *
 * @author Alexander Löffler
 */
class Log {

    private FileWriter writer;
    private final String fileName = "gamelog.log";

    /**
     * A Logger Class for Game Logs
     */
     Log() {

        String filename = fileName;

        try {
            //write converted json data to a file
            writer = new FileWriter(filename);
            writer.write("");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the first Line of the log
     *
     * @param detevtiveCount count of Detectives
     * @param isMisterAi Bool if Mister is AI
     * @param isDetectivesAi Bool if Detective is AI
     * @param misterStartPos Start Position for Mister
     * @param detectivesStartPos Start Position array for Detectives
     * @throws IOException
     */
    void writeLogBeginning(int detevtiveCount, boolean isMisterAi, boolean isDetectivesAi, int misterStartPos, int detectivesStartPos[]) throws IOException {

        writer.write(String.valueOf(detevtiveCount) + ",");
        writer.write(String.valueOf(isMisterAi) + ",");
        writer.write(String.valueOf(isDetectivesAi) + ",");
        writer.write(String.valueOf(misterStartPos) + ",");
        for (int i = 0; i < detevtiveCount; i++) {
            writer.write(String.valueOf(detectivesStartPos[i]));
            if (i < detevtiveCount - 1) {
                writer.write(",");
            }
        }
        writer.write("\n");
        writer.flush();

    }

    /**
     * Writes the lines of the Log for each Player move
     *
     * @param activePlayer Number for the active Player
     * @param currentStation Current Station of active Player
     * @param nextStation next Station of active player
     * @param undergroundTickets Number of Underground Tickets
     * @param busTickets Number of Bus Tickets
     * @param taxiTickets Number of Taxi Tickets
     * @param blackTickets Number of Black Tickets
     * @param tactics Number of last used Tactic
     * @param tacticScore Score for the last used Tactic
     * @throws IOException Exceptio if cant write log
     */
    void writeLogForTurn(int activePlayer, int currentStation, int nextStation, int undergroundTickets, int busTickets, int taxiTickets, int blackTickets, int tactics, double tacticScore) throws IOException {

        double roundedScore = Math.round(tacticScore * 10) / 10.0;

        writer.write(String.valueOf(activePlayer) + ",");
        writer.write(String.valueOf(currentStation) + ",");
        writer.write(String.valueOf(nextStation) + ",");
        writer.write(String.valueOf(undergroundTickets) + ",");
        writer.write(String.valueOf(busTickets) + ",");
        writer.write(String.valueOf(taxiTickets) + ",");
        writer.write(String.valueOf(blackTickets) + ",");
        writer.write(String.valueOf(tactics) + ",");
        writer.write(String.valueOf(roundedScore));
        writer.write("\n");
        writer.flush();
    }

    /**
     * Writes the last Line of the log
     *
     * @param detectivesWon bool if detectives have won
     * @throws IOException Error if cant write file
     */
    void writeLogForGameEnd(boolean detectivesWon) throws IOException {
        if (detectivesWon) {
            writer.write(String.valueOf(1));
        } else {
            writer.write(String.valueOf(0));
        }

        writer.close();
    }

}
