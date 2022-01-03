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
public class RequestTopPlayers implements Serializable{
    String playerUserName;
    Vector<Player> topPlayers;

    public RequestTopPlayers(String playerUserName, Vector<Player> topPlayers) {
        this.playerUserName = playerUserName;
        this.topPlayers = topPlayers;
    }

    public String getPlayer_id() {
        return playerUserName;
    }

    public void setPlayer_id(String player_id) {
        this.playerUserName = player_id;
    }

    public Vector<Player> getTopPlayers() {
        return topPlayers;
    }

    public void setTopPlayers(Vector<Player> topPlayers) {
        this.topPlayers = topPlayers;
    }
    
}
