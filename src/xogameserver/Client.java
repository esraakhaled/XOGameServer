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

    OutputStream os;
    InputStream is;
    Socket cs;
    ObjectOutputStream objectOutputStream;
    DataAccessLayer dataAccessLayer;
    public static Map<String, Client> clientsVector = new HashMap<String, Client>();
    // public static Map<String, Client> attempsUser = new HashMap<String, Client>();
    Player playerDB = null;

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
                    clientsVector.put(login.getUserName(), this);
                    sendLoginMessage(login);
                } else if (obj instanceof Register) {
                    Register register = (Register) obj;
                    clientsVector.put(register.getUserName(), this);
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
        boolean status = false;
        playerDB = null;
        try {
//change
            Player playerLogin = new Player(login.getUserName(), login.getPassword());

// check if it exist in DB
            if (dataAccessLayer.checkPlayerForLogin(playerLogin)) {
                status = true;
                playerDB = dataAccessLayer.getPlayer(playerLogin.getUserName());
                dataAccessLayer.updatePlayerStatusAvailable(playerDB.getUserName());
                dataAccessLayer.updatePlayerStatusOnline(playerDB.getUserName());
            } else {
                status = false;
            }
// get Player

            Client.clientsVector.entrySet().stream()
                    .filter(map -> (map.getKey()).equals(login.getUserName()))
                    .forEach(map -> {

                        try {
                            objectOutputStream = new ObjectOutputStream(os);
                            objectOutputStream.writeObject(playerDB);
                            objectOutputStream.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
            if (!status) {
                clientsVector.remove(login.getUserName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void sendRegisterMessage(Register register) {
        boolean status = false;
        playerDB = null;
        try {

            Player p = new Player(register.getUserName(), register.getPassword());
            if (!dataAccessLayer.checkPlayerForRegister(p)) {
                playerDB = p;
                status = true;
                try {
                    dataAccessLayer.registerPlayer(p);
                } catch (SQLException ex) {
                    Logger.getLogger(Client.class
                            .getName()).log(Level.SEVERE, null, ex);
                    //must return messgae there is an error or not by serlization
                }
            } else {
                status = false;
                playerDB = null;
            }

            Client.clientsVector.entrySet().stream()
                    .filter(map -> (map.getKey()).equals(p.getUserName()))
                    .forEach(map -> {

                        try {
                            objectOutputStream = new ObjectOutputStream(os);
                            objectOutputStream.writeObject(playerDB);
                            objectOutputStream.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
            if (!status) {
                clientsVector.remove(p.getUserName());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void sendSocketToIPScreen(Connection connection) {
        connection = new Connection(1, 1);
        // check if it exist in DB
        try {
            objectOutputStream = new ObjectOutputStream(os);
            objectOutputStream.writeObject(connection);
        } catch (IOException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

}
