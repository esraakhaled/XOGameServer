/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialize.models;

import java.io.Serializable;

/**
 *
 * @author ahmed
 */
public class Player implements Serializable{

    private int id;
    private String userName;
    private String password;
    private int score;
    private int numOfGames;
    private boolean available;
    private boolean online;
    private int win;
    private int lose;
    private int draw;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
    public Player(String userName){
        this.userName=userName;
    }
     public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public Player(int id, String userName, String password, int score, int numOfGames) {
        this(userName,password);
        this.id = id;
        this.score = score;
        this.numOfGames = numOfGames;
    }
      public Player(int id, String userName, String password, int score, int numOfGames,boolean available,boolean online,int win,int lose,int draw) {
       this(id,userName,password,score,numOfGames);
       this.available=available;
       this.online=online;
       this.lose=lose;
       this.win=win;
       this.draw=draw;
    }
    
    public void setId(int id){
        this.id=id;
    }
    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public int getNumOfGames() {
        return numOfGames;
    }

    public int getId() {
        return id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNumOfGames(int numOfGames) {
        this.numOfGames = numOfGames;
    }

    public String getPassword(){
        return password;
    }
   
}
