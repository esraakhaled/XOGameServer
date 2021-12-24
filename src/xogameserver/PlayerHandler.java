/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raiaan
 */
public class PlayerHandler extends Thread {
    DataInputStream dis;
    PrintStream ps;
    static public Vector<PlayerHandler> clientsVector = new Vector<PlayerHandler>();
    public PlayerHandler(Socket cs){
        try {
            dis = new DataInputStream(cs.getInputStream());
            ps = new PrintStream(cs.getOutputStream());
            PlayerHandler.clientsVector.add(this);
            start();
        } catch (IOException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void run(){
        while(true) {
            String str;
            try {
                str = dis.readLine();
                sendMessageToAll(str);
            } catch (IOException ex) {
                Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    void sendMessageToAll(String msg){
        for(PlayerHandler ch : clientsVector){
                ch.ps.println(msg);
        }
     }
}
