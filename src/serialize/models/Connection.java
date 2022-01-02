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
public class Connection implements Serializable{
     int signal ;
     int ack;

    public Connection(int signal, int ack) {
        this.signal = signal;
        this.ack = ack;
    }

   

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    
    
}
