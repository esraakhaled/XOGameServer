
package serialize.serizationModels;

import java.io.Serializable;

/**
 *
 * @author ahmed
 */
public class PlayingGame implements Serializable{
    private int roomId;
    private String targetuser;
    private String playedButtonID;
    public PlayingGame(){}
    
    public PlayingGame(int roomId,String targetuser,String playedButtonID){
        this.roomId=roomId;
        this.targetuser=targetuser;
        this.playedButtonID=playedButtonID;
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

    public String getPlayedButtonID() {
        return playedButtonID;
    }

    public void setPlayedButtonID(String playedButtonID) {
        this.playedButtonID = playedButtonID;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    boolean flag=true;

}