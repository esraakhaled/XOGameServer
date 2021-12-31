/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialize.models;

import java.io.Serializable;

/**
 *
 * @author Raiaan
 */
public class Player implements Serializable{
    public String userName;
    public int score;

    public Player(String user_name, int score) {
        this.userName = user_name;
        this.score = score;
    }

    public String getUser_name() {
        return userName;
    }

    public void setUser_name(String user_name) {
        this.userName = user_name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
}
