/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import xogameserver.models.Player;

/**
 *
 * @author Raiaan
 */
public class Client extends Thread{
    private Socket mSocket = null;
    private DataInputStream mDataInputStream;
    private DataOutputStream mDataOutputStream;
    private Boolean mIsShutdownCalled =false;
    Player mPlayer;
    public final void init(Socket socket)throws IOException  {
        if (socket == null)
            throw new NullPointerException("Socket is NULL!");

        mSocket = socket;
        mDataInputStream = new DataInputStream(mSocket.getInputStream());
        mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());

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
    @Override
    public void run() {
        while (!mIsShutdownCalled) {
            try {
                System.out.println("Attempt to read from client");
                String msg = read();
                if (!msg.isEmpty()) {
                    //handleMessageFromClient(msg);
                }
        

            } catch (Exception e) {
            }
        }
    }
        public void shutdown() {
        if (mDataInputStream != null) {
            try {
                mDataInputStream.close();
                mDataInputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (mDataOutputStream != null) {
            try {
                mDataOutputStream.close();
                mDataOutputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
                if (mSocket != null) {
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        try {
//            set users to offline
//            DatabaseHelper.updatePlayerStatus(mPlayer.email, Constants.PlayerStatus.OFFLINE);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        //GameServer.onSomeClientDisconnected(mPlayer.email);

        mIsShutdownCalled =true;

        //mPlayer = null;
    }
     public void setPlayer(Player mPlayer) {
        this.mPlayer = mPlayer;
    }
}
