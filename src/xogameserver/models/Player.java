/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver.models;

/**
 *
 * @author Raiaan
 */
public class Player {
    public int id;
    public String name;
    public String email;
    public int status;
    public int points;
    static int length = 1;
    @Override
    public String toString() {
        return "Player{" +
                "id=" + "id_"+String.valueOf(length) +
                ", name='" + "name"+String.valueOf(length) + '\'' +
                ", email='" + "email"+String.valueOf(length) + '\'' +
                ", status=" + "status"+String.valueOf(length) +
                ", points=" + "points"+String.valueOf(length) +
                '}';
    }
}
