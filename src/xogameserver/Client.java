/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import serialize.models.Login;
import serialize.models.Player;

/**
 *
 * @author Raiaan
 */
public class Client extends Thread{
    //DataInputStream dis;
    //PrintStream ps;
    OutputStream os;
    InputStream is;
    Socket cs;
    ObjectOutputStream objectOutputStream;
    public static Map<String, Client> clientsVector = new HashMap<String, Client>();
    public Client(Socket _cs){
        try {
            //dis = new DataInputStream(cs.getInputStream());
            //ps = new PrintStream(cs.getOutputStream());
            os = _cs.getOutputStream();
            is = _cs.getInputStream();
            cs = _cs;
            start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void closeConnection(){
        try {
            //to convert evert client state to offline
            os.close();
            is.close();
            cs.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void run(){
        while(true) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(is);
                Object obj = objectInputStream.readObject();
                if(obj instanceof Login){
                    Login login = (Login)obj;
                    sendLoginMessage(login);
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
//            String str;
//            try {
//                str = dis.readUTF();
//                String []data = str.split(";");
//                if(Integer.parseInt(data[0]) == RoutingBase.login){
//                    if(true){
//                        Client.clientsVector.put(data[0], this);
//                        sendLoginMessage(data[0],data[1],data[2]);
//                    }
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
        }
    }
    void sendLoginMessage(Login login){
        try {
            Player p = new Player(login.getUserName(),"1");
            objectOutputStream = new ObjectOutputStream(os);
            Client.clientsVector.entrySet().stream() 
                    .filter(map->(map.getKey()).equals(login.getUserName()))
                    .forEach(map->{
                try {
                    objectOutputStream.writeObject(p);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    void sendMessageToAll(String key,String msg){
//        Client.clientsVector.entrySet().stream()
//                .filter(map -> (map.getKey()).equals(key))
//                .forEach(map ->ps.println(msg) ); 
     }
}
