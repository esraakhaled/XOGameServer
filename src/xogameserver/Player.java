/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

/**
 *
 * @author ahmed
 */
public class Player {

    private int id;
    private String userName;
    private String password;
    private int score;
    private int numOfGames;

   
     public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public Player(int id, String userName, String password, int score, int numOfGames) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.score = score;
        this.numOfGames = numOfGames;
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
