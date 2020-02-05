/*
 * Scotland Yard Game
 * Programmierpraktikum
 * FH-Wedel 2019-2020
 * Created by Alexander Löffler
 *
 */
package logic;

/**
 * Main Save Class Strucure
 *
 * @author Alexander Löffler
 */
public class SaveGame {

    private final MisterSave mister;
    private final DetectiveSave detectives[];

    private final int turn;
    private final boolean isWon;

    private final boolean isDetectiveAi;
    private final boolean isMisterAi;

    private final int detectiveCount;
    private final int whosTurn;

    /**
     * Save Class Strucure for Detectives JSON Save Format
     *
     * @param mister Mister reference
     * @param detectives Detectives Reference
     * @param turn Current Turn
     * @param isWon Bool if won
     * @param isDetectiveAi Bool if Det is AI
     * @param isMisterAi Bool if Mister is AI
     * @param detectiveCount Number of Detectives
     * @param whosTurn current Player number
     */
    SaveGame(MisterSave mister, DetectiveSave[] detectives, int turn, boolean isWon, boolean isDetectiveAi, boolean isMisterAi, int detectiveCount, int whosTurn) {
        this.mister = mister;
        this.detectives = detectives;
        this.turn = turn;
        this.isWon = isWon;
        this.isDetectiveAi = isDetectiveAi;
        this.isMisterAi = isMisterAi;
        this.detectiveCount = detectiveCount;
        this.whosTurn = whosTurn;
    }

    /**
     * Getter for Mister
     *
     * @return mister
     */
    MisterSave getMister() {
        return mister;
    }

    /**
     * Getter for Detectives
     *
     * @return detectives
     */
    DetectiveSave[] getDetectives() {
        return detectives;
    }

    /**
     * Getter for Turn
     *
     * @return turn
     */
    int getTurn() {
        return turn;
    }

    /**
     * getter for is Won
     *
     * @return isWon
     */
    boolean isIsWon() {
        return isWon;
    }

    /**
     * Getter for is Detective an AI
     *
     * @return bool Detectives are AI
     */
    boolean isIsDetectiveAi() {
        return isDetectiveAi;
    }

    /**
     * Getter for Is mister AI
     *
     * @return Bool Mister is AI
     */
    boolean isIsMisterAi() {
        return isMisterAi;
    }

    /**
     * Getter for Number of Detectives
     *
     * @return number of Detectives
     */
    int getDetectiveCount() {
        return detectiveCount;
    }

    /**
     * Getter for Whos turn
     *
     * @return number for current player
     */
    int getWhosTurn() {
        return whosTurn;
    }

}
