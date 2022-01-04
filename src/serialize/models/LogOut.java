/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialize.models;

import java.io.Serializable;

/**
 *
 * @author abdelrhman sayed
 */
public class LogOut implements Serializable{
    private String userName;
    private int ack;

    public LogOut(String userName, int ack) {
        this.userName = userName;
        this.ack = ack;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }
    
}
