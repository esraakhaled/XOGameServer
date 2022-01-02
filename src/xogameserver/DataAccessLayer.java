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
    public void registerPlayer(Player p) throws SQLException {
        PreparedStatement ps = null;
        ps = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " (USERNAME, PASSWORD) " + " VALUES(?,?)");
        ps.setString(1, p.getUserName());
        ps.setString(2, p.getPassword());
        ps.executeUpdate();
        if (ps != null) {
            ps.close();
        }
    }

    /*
    return number of player in DB
    or return -1 if error happens
     */
    public int getPlayersnumber() throws SQLException {
        int numOfPlayers = 0;
        PreparedStatement ps = null;
        ps = connection.prepareStatement("SELECT count(id) FROM " + TABLE_NAME, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            numOfPlayers = rs.getInt(1);
        }

        if (ps != null) {
            ps.close();
        }
        return numOfPlayers;

    }

    //TODO : close 
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    // for client api

    public int getPlayerScore(String userName) throws SQLException {
        int score = 0;
        PreparedStatement ps = null;
        ps = connection.prepareStatement("SELECT " + SCORE + " FROM " + TABLE_NAME + " where " + USERNAME + " = ? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            score = rs.getInt(1);
        }
        if (ps != null) {
            ps.close();
        }
        return score;
    }

    public int getPlayerNumOfGames(String userName) throws SQLException {
        int numOfGames = 0;
        PreparedStatement ps = null;

        ps = connection.prepareStatement("SELECT " + NUMOFGAMES + " FROM " + TABLE_NAME + " where " + NUMOFGAMES + " = ? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            numOfGames = rs.getInt(1);
        }
        if (ps != null) {

            ps.close();
        }
        return numOfGames;

    }

    // TODO: implment these methods
    public void updatePlayerScore(String userName, int Score) throws SQLException {
        PreparedStatement ps = null;

        ps = connection.prepareStatement("update " + TABLE_NAME + " set " + SCORE + " = ? where " + USERNAME + " = ?");
        ps.setInt(1, Score);
        ps.setString(2, userName);
        ps.executeUpdate();
        if (ps != null) {
            ps.close();
        }

    }
// TODO: implment these methods

    public void updatePlayerStatusOnline(String userName) throws SQLException {
        PreparedStatement ps = null;

        ps = connection.prepareStatement("update " + TABLE_NAME + " set online = true where " + USERNAME + " = ?");
        ps.setString(1, userName);
        ps.executeUpdate();
        if (ps != null) {
            ps.close();
        }

    }

    public void updatePlayerStatusAvailable(String userName) throws SQLException {
        PreparedStatement ps = null;

        ps = connection.prepareStatement("update " + TABLE_NAME + " set available = true where " + USERNAME + " = ?");
        ps.setString(1, userName);
        ps.executeUpdate();
        if (ps != null) {
            ps.close();
        }

    }

    public void updatePlayerGames(String userName, int numOfGames) throws SQLException {
        PreparedStatement ps = null;

        ps = connection.prepareStatement("update " + TABLE_NAME + " set " + NUMOFGAMES + " = ? where " + USERNAME + " = ?");
        ps.setInt(1, numOfGames);
        ps.setString(2, userName);
        ps.executeUpdate();

        if (ps != null) {

            ps.close();

        }

    }

    public void updatePlayerWins(String userName, int win) throws SQLException {
        PreparedStatement ps = null;
        ps = connection.prepareStatement("update " + TABLE_NAME + " set " + WIN + " = ? where " + USERNAME + " = ?");
        ps.setInt(1, win);
        ps.setString(2, userName);
        ps.executeUpdate();
        if (ps != null) {
        }

    }

    public void updatePlayerLoses(String userName, int lose) throws SQLException {
        PreparedStatement ps = null;

        ps = connection.prepareStatement("update " + TABLE_NAME + " set " + LOSE + " = ? where " + USERNAME + " = ?");
        ps.setInt(1, lose);
        ps.setString(2, userName);
        ps.executeUpdate();
        if (ps != null) {

            ps.close();

        }
        // handle if exception --> put -1
    }

    public void updatePlayerDraw(String userName, int draw) throws SQLException {
        PreparedStatement ps = null;

        ps = connection.prepareStatement("update " + TABLE_NAME + " set " + DRAW + " = ? where " + USERNAME + " = ?");
        ps.setInt(1, draw);
        ps.setString(2, userName);
        ps.executeUpdate();
        if (ps != null) {
            ps.close();
        }
        // handle if exception --> put -1
    }

    public boolean checkPlayerForLogin(Player p) throws SQLException {
        String userName = p.getUserName().trim();
        String password = p.getPassword().trim();
        boolean flag = false;
        PreparedStatement ps = null;

        // SELECT EXISTS(SELECT * FROM yourTableName WHERE yourCondition);
        ps = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " where " + USERNAME + " = ? AND " + PASSWORD + " = ?  And " + online + " = false ", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            flag = true;
        }
        if (ps != null) {
            ps.close();
        }
        return flag;

    }

    public boolean checkPlayerForRegister(Player p) throws SQLException {
        String userName = p.getUserName().trim();
        String password = p.getPassword().trim();
        boolean flag = false;
        PreparedStatement ps = null;

        // SELECT EXISTS(SELECT * FROM yourTableName WHERE yourCondition);
        ps = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " where " + USERNAME + " = ? AND " + PASSWORD + " = ? ", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, userName);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            flag = true;
        }
        if (ps != null) {
            ps.close();
        }
        return flag;

    }

    public Player getPlayer(String userName) throws SQLException {
        Player p = null;
        PreparedStatement ps = null;
        ps = connection.prepareStatement("SELECT *  FROM " + TABLE_NAME + " where " + USERNAME + " = ? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            p = new Player(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getInt(4), rs.getInt(5), rs.getBoolean(6), rs.getBoolean(7),
                    rs.getInt(8), rs.getInt(9), rs.getInt(10));
        }
        if (ps != null) {
            ps.close();

        }
        return p;
    }
    // return null if there is problem in data base

    public Vector<Player> getAvailablePlayers() throws SQLException {
        Vector<Player> players = new Vector<>();
        PreparedStatement ps = null;

        ps = connection.prepareStatement("SELECT  * " + " FROM " + TABLE_NAME + " where " + available + " = true", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            players.add(new Player(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getInt(4), rs.getInt(5), rs.getBoolean(6), rs.getBoolean(7),
                    rs.getInt(8), rs.getInt(9), rs.getInt(10)));
        }
        if (ps != null) {
            ps.close();
        }
        return players;
    }

    // return null if there is problem in data base
    public Vector<Player> getOfflinePlayers() throws SQLException {
        Vector<Player> players = new Vector<>();
        PreparedStatement ps = null;
        ps = connection.prepareStatement("SELECT  * " + " FROM " + TABLE_NAME + " where " + online + " = false", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            players.add(new Player(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getInt(4), rs.getInt(5), rs.getBoolean(6), rs.getBoolean(7),
                    rs.getInt(8), rs.getInt(9), rs.getInt(10)));
        }
        if (ps != null) {
            ps.close();
        }
        return players;
    }

// 
    // return null if there is problem in data base
    public Vector<Player> getOnlinePlayers() {
        Vector<Player> players = new Vector<>();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT  * " + " FROM " + TABLE_NAME + " where " + online + " = true", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
    public void logout(Player p) throws SQLException {
        PreparedStatement ps = null;
        ps = connection.prepareStatement("update " + TABLE_NAME + " set " + available + " = false  set " + online + " =false " + "where " + USERNAME + " = ?");
        ps.setString(1, p.getUserName());
        ps.executeUpdate();
        if (ps != null) {
            ps.close();
        }
    }

    public int getPlayersOfflineNum() throws SQLException {
        int numOfPlayers = 0;
        PreparedStatement ps = null;
        ps = connection.prepareStatement("SELECT count(*)  FROM  " + TABLE_NAME + " where " + online + " = false", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            numOfPlayers = rs.getInt(1);
        }
        if (ps != null) {
            ps.close();
        }
        return numOfPlayers;
    }

    public int getPlayersOnlineNum() throws SQLException {
        int numOfPlayers = 0;
        PreparedStatement ps = null;
        ps = connection.prepareStatement("SELECT count(*)  FROM  " + TABLE_NAME + " where " + online + " = true", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            numOfPlayers = rs.getInt(1);
        }
        if (ps != null) {
            ps.close();
        }
        return numOfPlayers;
    }

}
