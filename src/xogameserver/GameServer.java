/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raiaan
 */
public class GameServer {

    public static ServerSocket serverSocket;

    public GameServer() {
        try {
            serverSocket = new ServerSocket(5555);
            while (true) {
                Socket s = serverSocket.accept();
                System.out.println(s);
                new Client(s);
            }

        } catch (SocketException e) {
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void close() {
        try {
            if (serverSocket != null) {
                //to handle database online user
                Client.clientsVector
                        .entrySet()
                        .stream()
                        .forEach(item -> item.getValue().closeConnection());
                serverSocket.close();
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
