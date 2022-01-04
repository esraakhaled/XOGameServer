/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialize.models;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author Raiaan
 */
public class RequestProfileBase implements Serializable{
    String playerUserName;
    Vector<Player> topPlayers;
    Vector<Player> onlinePlayer;

    public Vector<Player> getOnlinePlayer() {
        return onlinePlayer;
    }

    public void setOnlinePlayer(Vector<Player> onlinePlayer) {
        this.onlinePlayer = onlinePlayer;
    }
    
    public RequestProfileBase(String playerUserName, Vector<Player> topPlayers,Vector<Player> onlinePlayer){
        this.playerUserName = playerUserName;
        this.topPlayers = topPlayers;
        this.onlinePlayer = onlinePlayer;
    }

    public String getPlayerUserName() {
        return playerUserName;
    }

    public void setPlayerUserName(String playerUserName) {
        this.playerUserName = playerUserName;
    }

    public Vector<Player> getTopPlayers() {
        return topPlayers;
    }

    public void setTopPlayers(Vector<Player> topPlayers) {
        this.topPlayers = topPlayers;
    }
    
}
