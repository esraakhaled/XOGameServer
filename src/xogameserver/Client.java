/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import xogameserver.models.Player;

/**
 *
 * @author Raiaan
 */
public class Client extends Thread{
    DataInputStream dis;
    PrintStream ps;
    static Map<String, Client> clientsVector = new HashMap<String, Client>();
    public Client(Socket cs){
        try {
            dis = new DataInputStream(cs.getInputStream());
            ps = new PrintStream(cs.getOutputStream());
            start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void run(){
        while(true) {
            String str;
            try {
                str = dis.readUTF();
                String []data = str.split(";");
                if(Integer.parseInt(data[0]) == RoutingBase.login){
                    if(true){
                        Client.clientsVector.put(data[0], this);
                        sendLoginMessage(data[0],data[1],data[2]);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    void sendLoginMessage(String key , String mail , String password){
        Client.clientsVector.entrySet().stream()
                .filter(map -> (map.getKey()).equals(key))
                .forEach(map ->ps.println("your email is"+mail+"password is"+ password) ); 
    }
    void sendMessageToAll(String key,String msg){
        Client.clientsVector.entrySet().stream()
                .filter(map -> (map.getKey()).equals(key))
                .forEach(map ->ps.println(msg) ); 
     }
}
