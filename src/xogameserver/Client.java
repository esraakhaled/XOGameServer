/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.util.Pair;
import serialize.models.LogOut;
import serialize.models.Login;
import serialize.models.Connection;
import serialize.models.Player;
import serialize.models.PlayingGame;
import serialize.models.Register;
import serialize.models.RequestGame;
import serialize.models.RequestProfileBase;
import serialize.models.RoomPlayer;
import static xogameserver.Client.clientsVector;

/**
 *
 * @author Raiaan
 */
public class Client extends Thread {

    Object obj;
    OutputStream os;
    InputStream is;
    Socket cs;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    DataAccessLayer dataAccessLayer;
    Pair<String, Client> playerPair;
    RoomPlayer roomPlayer;

    int gameSessionId = -1;

    static int serverRoom = 0;
    public static HashMap<String, Client> clientsVector = new HashMap<String, Client>();

    public static HashMap<Integer, RoomPlayer> gameRooms = new HashMap<>();

    Player playerDB = null;

    public Client(Socket _cs) {
        try {
            //dis = new DataInputStream(cs.getInputStream());
            //ps = new PrintStream(cs.getOutputStream());
            cs = _cs;
            os = cs.getOutputStream();
            is = cs.getInputStream();
            objectOutputStream = new ObjectOutputStream(os);
            objectInputStream = new ObjectInputStream(is);

            dataAccessLayer = DataAccessLayer.openConnection();
            start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            os.close();
            is.close();
            cs.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateAvailableListPlayer() {
        System.out.println("update profile");
        clientsVector.entrySet().stream().forEach((item) -> {
            getPlayersProfile(new RequestProfileBase(item.getKey(), null, null), item.getValue());
            System.out.println("entry set");
        });
    }

    public void run() {
        while (true) {
            try {
                obj = objectInputStream.readObject();
                if (obj instanceof Login) {
                    System.out.println("login enter 7e");
                    Login login = (Login) obj;
                    clientsVector.put(login.getUserName(), this);
                    System.out.println(clientsVector.size());
                    sendLoginMessage(login);
                } else if (obj instanceof Register) {
                    Register register = (Register) obj;
                    clientsVector.put(register.getUserName(), this);
                    System.out.println(clientsVector.size());

                    sendRegisterMessage(register);
                } else if (obj instanceof Connection) {
                    Connection connection = (Connection) obj;
                    //System.out.println(connection.getSignal());
                    sendSocketToIPScreen(connection);
                    System.out.println("connection done ");

                } else if (obj instanceof LogOut) {
                    LogOut logOut = (LogOut) obj;
                    sendLogOutMessage(logOut);
                    clientsVector.remove(logOut.getUserName());
                    updateAvailableListPlayer();

                } else if (obj instanceof RequestProfileBase) {
                    getPlayersProfile((RequestProfileBase) obj, this);
                } else if (obj instanceof RequestGame) {
                    RequestGame requestGame = (RequestGame) obj;
                    System.out.println(requestGame.getGameResponse());
                    switch (requestGame.getGameResponse()) {
                        case RequestGame.requestGame:
                            dataAccessLayer.updatePlayerStatusNotAvailable(requestGame.getRequstedUserName());
                            dataAccessLayer.updatePlayerStatusNotAvailable(requestGame.getChoosePlayerUserName());
                            updateAvailableListPlayer();
                            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(10));

                            pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        dataAccessLayer.updatePlayerStatusAvailable(requestGame.getRequstedUserName());
                                        dataAccessLayer.updatePlayerStatusAvailable(requestGame.getChoosePlayerUserName());
                                        updateAvailableListPlayer();
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            });
                            pauseTransition.play();
                            gameSessionId = ++serverRoom;
                            requestGame.setGameRoom(gameSessionId);
                            playerPair = new Pair<>(requestGame.getRequstedUserName(), this);
                            RoomPlayer room = new RoomPlayer(playerPair);
                            gameRooms.put(gameSessionId, room);
                            sendRequestToUser(requestGame.getChoosePlayerUserName(), requestGame);
                            System.out.println("request fom client to server: " + requestGame.getGameRoom());

                            break;
                        case RequestGame.acceptChallenge:

                            pauseTransition = new PauseTransition(Duration.seconds(10));
                            pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        dataAccessLayer.updatePlayerStatusNotAvailable(requestGame.getRequstedUserName());
                                        dataAccessLayer.updatePlayerStatusNotAvailable(requestGame.getChoosePlayerUserName());
                                        updateAvailableListPlayer();
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            });
                            pauseTransition.play();

                            gameSessionId = requestGame.getGameRoom();
                            playerPair = new Pair<>(requestGame.getChoosePlayerUserName(), this);

                            room = gameRooms.get(gameSessionId);

                            room.setPlayerB(playerPair);
                            gameRooms.put(gameSessionId, room);
                            sendRequestToUser(requestGame.getRequstedUserName(), requestGame);
                            System.out.println("acceppt fom client to server: " + requestGame.getGameRoom());

                            break;
                        case RequestGame.refuseChallenge:

                            

                            dataAccessLayer.updatePlayerStatusAvailable(requestGame.getRequstedUserName());
                            dataAccessLayer.updatePlayerStatusAvailable(requestGame.getChoosePlayerUserName());
                            updateAvailableListPlayer();
                            gameRooms.get(requestGame.getGameRoom()).getPlayerA().getValue().gameSessionId = -1;
                            gameRooms.remove(requestGame.getGameRoom());
                            sendRequestToUser(requestGame.getRequstedUserName(), requestGame);
                            break;

                    }

                } else if (obj instanceof PlayingGame) {

                    PlayingGame playingGame = (PlayingGame) obj;
                    sendMoveToPlayer(playingGame.getRoomId(), playingGame);

                    System.out.println("send Model from server to client");

                }

            } catch (EOFException EX) {
                try {
                    is.close();
                    os.close();
                    cs.close();
                    this.stop();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SocketException ex) {
                //check first
                if (gameSessionId != -1) {
                    tellTheOtherPlayer(gameSessionId, this, obj);
                    // change his gameSessionId

                }
                try {
                    //  objectOutputStream.close();
                    //  is.close();
                    // os.close();
                    cs.close();
                    this.stop();

                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {

                ex.printStackTrace();
            } catch (SQLException ex) {

                ex.printStackTrace();

            }
        }
    }

    void sendLoginMessage(Login login) {
        boolean status = false;
        playerDB = null;
        try {
            Player playerLogin = new Player(login.getUserName(), login.getPassword());
            if (dataAccessLayer.checkPlayerForLogin(playerLogin)) {
                status = true;
                playerDB = dataAccessLayer.getPlayer(playerLogin.getUserName());
                dataAccessLayer.updatePlayerStatusAvailable(playerDB.getUserName());
                dataAccessLayer.updatePlayerStatusOnline(playerDB.getUserName());
            } else {
                status = false;
            }
            clientsVector.entrySet().stream()
                    .filter(item -> item.getKey().equals(playerLogin.getUserName()))
                    .forEach((item) -> {
                        try {
                            // objectOutputStream = new ObjectOutputStream(os);
                            objectOutputStream.writeObject(playerDB);
                            objectOutputStream.flush();
                        } catch (SocketException ex) {
                            closeConnection();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
            updateAvailableListPlayer();
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
                }
            } else {
                status = false;
                playerDB = null;
            }
            clientsVector.entrySet().stream()
                    .filter(item -> item.getKey().equals(p.getUserName()))
                    .forEach((item) -> {
                        try {
                            // objectOutputStream = new ObjectOutputStream(os);
                            objectOutputStream.writeObject(playerDB);
                            objectOutputStream.flush();
                        } catch (SocketException ex) {
                            closeConnection();
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
            updateAvailableListPlayer();
        } catch (SQLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void sendSocketToIPScreen(Connection connection) {
        connection = new Connection(1, 1);
        try {
            objectOutputStream.writeObject(connection);
        } catch (IOException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    void getPlayersProfile(RequestProfileBase r, Client c) {
        r.setTopPlayers(dataAccessLayer.getTopPlayer());
        try {
            r.setOnlinePlayer(dataAccessLayer.getAvailablePlayers());
        } catch (SQLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientsVector.entrySet().stream()
                .filter(item -> item.getKey().equals(r.getPlayerUserName()))
                .forEach((item) -> {
                    try {
                        c.objectOutputStream.writeObject(r);
                        c.objectOutputStream.flush();
                    } catch (SocketException ex) {
                        closeConnection();
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
    }

    void sendLogOutMessage(LogOut logOut) {
        logOut = new LogOut(logOut.getUserName(), 1);
        try {

            dataAccessLayer.updatePlayerStatusNotAvailable(logOut.getUserName());
            dataAccessLayer.updatePlayerStatusOffLine(logOut.getUserName());
        } catch (SQLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            //  objectOutputStream = new ObjectOutputStream(os);
            objectOutputStream.writeObject(logOut);
            objectOutputStream.flush();
            updateAvailableListPlayer();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendToUser(String user, Object message) {
        try {
            Client s = clientsVector.get(user);
            System.out.println(s);
            s.objectOutputStream = new ObjectOutputStream(s.os);
            s.objectOutputStream.writeObject(message);
            s.objectOutputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void sendRequestToUser(String user, RequestGame message) {
        try {
            Client s = clientsVector.get(user);
            s.objectOutputStream.writeObject(message);
            s.objectOutputStream.flush();
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void sendMoveToPlayer(int roomId, PlayingGame playingGame) {

        RoomPlayer room = gameRooms.get(roomId);
        Client playerClient = null;
        if (room.getPlayerA().getKey().equals(playingGame.getTargetuser())) {
            playerClient = room.getPlayerA().getValue();
            System.out.println(room.getPlayerA().getKey() + "A");

        } else if (room.getPlayerB().getKey().equals(playingGame.getTargetuser())) {
            playerClient = room.getPlayerB().getValue();
            System.out.println(room.getPlayerB().getKey() + "B");

        } else {
            System.out.println("problem");
        }
        if (playerClient != null) {
            try {
                playerClient.objectOutputStream.writeObject(playingGame);
                playerClient.objectOutputStream.flush();
            } catch (SocketException ex) {
                // check if one go away
                playingGame.setFlag(false);
                if (playerClient == room.getPlayerA().getValue()) {
                    playerClient = room.getPlayerB().getValue();

                } else if (playerClient == room.getPlayerB().getValue()) {
                    playerClient = room.getPlayerB().getValue();
                }

                try {
                    playerClient.objectOutputStream.writeObject(playingGame);
                } catch (IOException ex1) {
                    ex1.printStackTrace();
                }

                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

    public void tellTheOtherPlayer(int roomId, Client closed, Object obj) {
        PlayingGame playingGame = (PlayingGame) obj;
        RoomPlayer room = gameRooms.get(roomId);
        Client playerClient = null;
        if (room.getPlayerA().getValue() == closed) {
            playerClient = room.getPlayerB().getValue();
        } else if (room.getPlayerB().getValue() == closed) {
            playerClient = room.getPlayerA().getValue();
        }
        if (playerClient != null) {
            playingGame.setFlag(false);
            try {
                playerClient.objectOutputStream.writeObject(playingGame);
                playerClient.objectOutputStream.flush();
            } catch (SocketException ex) {
                try {
                    // all of them is closed
                    room.getPlayerA().getValue().objectInputStream.close();
                    room.getPlayerA().getValue().objectOutputStream.close();
                    //
                    room.getPlayerB().getValue().objectInputStream.close();
                    room.getPlayerB().getValue().objectOutputStream.close();
                } catch (IOException ex1) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }
}
