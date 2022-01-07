/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialize.models;

import javafx.util.Pair;
import xogameserver.Client;

/**
 *
 * @author ahmed
 */
public class RoomPlayer {

    private Pair<String, Client> playerA;
    private Pair<String, Client> playerB;

    public RoomPlayer() {
    }

    public RoomPlayer(Pair<String, Client> playerA) {
        this.playerA=playerA;
    }
     public RoomPlayer(Pair playerA,Pair playerB) {
        this.playerA=playerA;
        this.playerB=playerB;
    }

    public Pair<String, Client> getPlayerA() {
        return playerA;
    }

    public void setPlayerA(Pair<String, Client> playerA) {
        this.playerA = playerA;
    }

    public Pair<String, Client> getPlayerB() {
        return playerB;
    }

    public void setPlayerB(Pair<String, Client> playerB) {
        this.playerB = playerB;
    }

}
