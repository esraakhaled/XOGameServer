/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialize.models;

import java.io.Serializable;
import javafx.scene.control.Button;

/**
 *
 * @author ahmed
 */
public class PlayingGame implements Serializable{
    private int roomId;
    private String targetuser;
    private Button playedButton;
    boolean flag=true;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public PlayingGame(int roomId,String targetUser,Button playedButton){
        this.roomId=roomId;
        this.targetuser=targetUser;
        this.playedButton=playedButton;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getTargetuser() {
        return targetuser;
    }

    public void setTargetuser(String targetuser) {
        this.targetuser = targetuser;
    }

    public Button getPlayedButton() {
        return playedButton;
    }

    public void setPlayedButton(Button playedButton) {
        this.playedButton = playedButton;
    }

    

    

}