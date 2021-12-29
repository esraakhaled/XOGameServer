/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import xogameserver.models.Player;

/**
 *
 * @author Raiaan
 */
public class GameServer {
    public static final int SERVER_PORT = 5005;
    private static ServerSocket mGameConnectionsSocket;
    private static Thread mMainServiceThread;
    private DataInputStream mDataInputStream;
    private DataOutputStream mDataOutputStream;
    private static ConcurrentHashMap<String, Client> allClients = new ConcurrentHashMap<>();
    public static void startServer() {

        if (mGameConnectionsSocket != null && !mGameConnectionsSocket.isClosed()) {
            System.out.println("Server already running...");
            return;
        }

        try {
            mGameConnectionsSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server socket created...");
            System.out.println("Server socket: " + mGameConnectionsSocket.toString());

            System.out.println("attempt to start main server service...");
            startServerMainService();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        if (mGameConnectionsSocket != null && mGameConnectionsSocket.isClosed()) {
            System.out.println("Server is NOT running...");
            return;
        }
        try {
            if (mGameConnectionsSocket != null) {
                mGameConnectionsSocket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startServerMainService() {
        mMainServiceThread = new Thread(() -> {
            while (mGameConnectionsSocket != null && !mGameConnectionsSocket.isClosed()) {
                try {
                    System.out.println("attempt to accept clients...");
                    Socket clientSocket = mGameConnectionsSocket.accept();
                    System.out.println("accepted new client: " + clientSocket.toString() + "\nattempt to handle it...");
                    handleClient(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mMainServiceThread.start();

    }
    private static void handleClient(Socket clientSocket) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        String StringData = dataInputStream.readUTF();
        int requestType = Integer.parseInt(StringData);
        Client client = new Client();
        switch (requestType) {
            case RoutingBase.login:
                System.out.println("login");
                allClients.put(StringData,client);
                handleSignInRequest(clientSocket, "login "+client.getName());
                break;
            case RoutingBase.register:
                System.out.println("register");
                allClients.put(StringData,client);
                handleSignInRequest(clientSocket, "login "+client.getName());
                break;
                
        }
    }
    
    public final void send(String msg) throws IOException {
        if (mDataOutputStream == null)
            throw new IllegalStateException("Can not send msg!");

        System.out.println("[send] attempt to send to client");
        mDataOutputStream.writeUTF(msg);
    }

    public final String read() throws IOException {
        if (mDataInputStream == null)
            throw new IllegalStateException("Can not read msg!");

        return mDataInputStream.readUTF();
    }
    private static void handleSignInRequest(Socket clientSocket, String connectionJsonArg) {
        try {
            Player player = new Player();
            Client client = new Client();
            client.init(clientSocket);
            client.send("sign IN");
            client.setPlayer(player);
            client.start();
            client.send(player.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
