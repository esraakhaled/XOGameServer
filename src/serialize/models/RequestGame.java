package serialize.models;

import java.io.Serializable;

/**
 *
 * @author Raiaan
 */
public class RequestGame implements Serializable{
    public static final int requestGame = 0;
    public static final int acceptChallenge = 1;
    public static final int refuseChallenge = 2;
    String player1 , player2;
    short gameResponse;

    public RequestGame(String player1, String player2, short gameResponse) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameResponse = gameResponse;
    }
    
    public RequestGame() {
    }

    public short getGameResponse() {
        return gameResponse;
    }

    public void setGameResponse(short gameResponse) {
        this.gameResponse = gameResponse;
    }

    public String getRequstedUserName() {
        return player1;
    }

    public void setRequstedUserName(String requstedUserName) {
        this.player1 = requstedUserName;
    }

    public String getChoosePlayerUserName() {
        return player2;
    }

    public void setChoosePlayerUserName(String choosePlayerUserName) {
        this.player2 = choosePlayerUserName;
    }
}
