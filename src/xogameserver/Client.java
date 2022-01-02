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
import java.net.SocketException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import serialize.models.Connection;
import serialize.models.Login;
import serialize.models.Player;
import serialize.models.Register;

/**
 *
 * @author Raiaan
 */
public class Client extends Thread {

    //DataInputStream dis;
    //PrintStream ps;
    OutputStream os;
    InputStream is;
    Socket cs;
    ObjectOutputStream objectOutputStream;
    DataAccessLayer dataAccessLayer;
    public static Map<String, Client> clientsVector = new HashMap<String, Client>();
    public static Map<String, Client> attempsUser = new HashMap<String, Client>();

    public Client(Socket _cs) {
        try {
            //dis = new DataInputStream(cs.getInputStream());
            //ps = new PrintStream(cs.getOutputStream());
            os = _cs.getOutputStream();
            is = _cs.getInputStream();
            cs = _cs;
            dataAccessLayer = DataAccessLayer.openConnection();
            start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            //to convert evert client state to offline
            os.close();
            is.close();
            cs.close();
            dataAccessLayer.closeConnection();
        } catch (IOException ex) {

            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        while (true) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(is);
                Object obj = objectInputStream.readObject();
                if (obj instanceof Login) {
                    Login login = (Login) obj;
                    sendLoginMessage(login);
                } else if (obj instanceof Register) {
                    Register register = (Register) obj;
                    sendRegisterMessage(register);
                } else if (obj instanceof Connection) {
                    Connection connection = (Connection) obj;
                    //System.out.println(connection.getSignal());
                    sendSocketToIPScreen(connection);
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void sendLoginMessage(Login login) {
        Player playerDB = null;

        try {
//change
            Player playerLogin = new Player(login.getUserName(), login.getPassword());
            boolean status;
// check if it exist in DB
            if (dataAccessLayer.checkPlayer(playerLogin)) {
                status = true;
                playerDB = dataAccessLayer.getPlayer(playerLogin.getUserName());
            } else {
                status = false;
            }
// get Player

            /*
Client.clientsVector.entrySet().stream()
.filter(map -> (map.getKey()).equals(login.getUserName()))
.forEach(map -> {  });
             */
            try {
                objectOutputStream = new ObjectOutputStream(os);
                objectOutputStream.writeObject(playerDB);
                objectOutputStream.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void sendRegisterMessage(Register register) {
        Player p = new Player(register.getUserName(), register.getPassword());
        try {
            // check if it exist in DB or not
            dataAccessLayer.registerPlayer(p);

        } catch (SQLException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
            //must return messgae there is an error or not by serlization
        }

    }

    void sendSocketToIPScreen(Connection connection) {
        connection = new Connection(1, 1);
        // check if it exist in DB
        try {
            objectOutputStream = new ObjectOutputStream(os);
            objectOutputStream.writeObject(connection);

            System.out.println("pass");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
