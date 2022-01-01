/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.net.ConnectException;
import serialize.models.Player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author ahmed
 */
public class DataAccessLayer {

    private static final String ID = "id";
    private static final String TABLE_NAME = "PLAYER";
    private static final String USERNAME = "userName";
    private static final String PASSWORD = "password";
    private static final String SCORE = "score";
    private static final String NUMOFGAMES = "numberOfGames";
    private static final String WIN = "win";
    private static final String LOSE = "lose";
    private static final String DRAW = "draw";
    private static final String available = "available";
    private static final String online = "online";

    private static Connection connection;
    static DataAccessLayer dataAccessLayer;

    private DataAccessLayer() {
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/GameDB", "root", "root");

        } catch (SQLNonTransientConnectionException ex) {
            // show some pop
            System.out.println("ss");
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DataAccessLayer openConnection() {
        if (dataAccessLayer == null) {
            dataAccessLayer = new DataAccessLayer();
        }
        return dataAccessLayer;
    }

    /*
    use to register player 
    take player as an argumnent
    rturn 1 if it's done correctly
    or return -1 if error happens
     */
    public int registerPlayer(Player p) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (USERNAME, PASSWORD) " + " VALUES(?,?)");
            ps.setString(1, p.getUserName());
            ps.setString(2, p.getPassword());
            ps.executeUpdate();
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /*
    return number of player in DB
    or return -1 if error happens
     */
    public int getPlayersnumber() {
        int numOfPlayers = 0;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT count(id) FROM " + TABLE_NAME, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numOfPlayers = rs.getInt(1);
            }
            return numOfPlayers;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //TODO : close 
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    // for client api

    public int getPlayerScore(String userName) {
        int score = 0;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT " + SCORE + " FROM " + TABLE_NAME + " where " + USERNAME + " = ? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                score = rs.getInt(1);
            }
            return score;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public int getPlayerNumOfGames(String userName) {
        int numOfGames = 0;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT " + NUMOFGAMES + " FROM " + TABLE_NAME + " where " + NUMOFGAMES + " = ? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numOfGames = rs.getInt(1);
            }
            return numOfGames;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // TODO: implment these methods
    public int updatePlayerScore(String userName, int Score) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("update " + TABLE_NAME + " set " + SCORE + " = ? where " + USERNAME + " = ?");
            ps.setInt(1, Score);
            ps.setString(2, userName);
            ps.executeUpdate();
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return -1;
    }

    public int updatePlayerGames(String userName, int numOfGames) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("update " + TABLE_NAME + " set " + NUMOFGAMES + " = ? where " + USERNAME + " = ?");
            ps.setInt(1, numOfGames);
            ps.setString(2, userName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return -1;
    }

    public int updatePlayerWins(String userName, int win) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("update " + TABLE_NAME + " set " + WIN + " = ? where " + USERNAME + " = ?");
            ps.setInt(1, win);
            ps.setString(2, userName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return -1;
    }

    public int updatePlayerLoses(String userName, int lose) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("update " + TABLE_NAME + " set " + LOSE + " = ? where " + USERNAME + " = ?");
            ps.setInt(1, lose);
            ps.setString(2, userName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return -1;
    }

    public int updatePlayerDraw(String userName, int draw) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("update " + TABLE_NAME + " set " + DRAW + " = ? where " + USERNAME + " = ?");
            ps.setInt(1, draw);
            ps.setString(2, userName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return -1;
    }

    public boolean checkPlayer(Player p) {
       String userName =p.getUserName();
        String password=p.getPassword();
        boolean flag = false;
        PreparedStatement ps = null;
        try {
            // SELECT EXISTS(SELECT * FROM yourTableName WHERE yourCondition);

            ps = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " where " + USERNAME + " = ? AND " + PASSWORD + " = ? ", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                flag = true;
            }
            return flag;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return flag;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public Player getPlayer(String userName) {
        Player p = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT *  FROM " + TABLE_NAME + " where " + USERNAME + " = ? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Player(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getInt(4), rs.getInt(5), rs.getBoolean(6), rs.getBoolean(7),
                        rs.getInt(8), rs.getInt(9), rs.getInt(10));
            }
            return p;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return p;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    // return null if there is problem in data base

    public Vector<Player> getAvailablePlayers() {
        Vector<Player> players = new Vector<>();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT  * " + " FROM " + TABLE_NAME + " where " + available + " = true", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                players.add(new Player(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getInt(4), rs.getInt(5), rs.getBoolean(6), rs.getBoolean(7),
                        rs.getInt(8), rs.getInt(9), rs.getInt(10)));
            }
            return players;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // return null if there is problem in data base
    public Vector<Player> getOfflinePlayers() {
        Vector<Player> players = new Vector<>();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT  * " + " FROM " + TABLE_NAME + " where " + online + " = false", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                players.add(new Player(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getInt(4), rs.getInt(5), rs.getBoolean(6), rs.getBoolean(7),
                        rs.getInt(8), rs.getInt(9), rs.getInt(10)));
            }
            return players;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
// 

    public void logout(Player p) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("update " + TABLE_NAME + " set " + available + " = false  set " + online + " =false " + "where " + USERNAME + " = ?");
            ps.setString(1, p.getUserName());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
