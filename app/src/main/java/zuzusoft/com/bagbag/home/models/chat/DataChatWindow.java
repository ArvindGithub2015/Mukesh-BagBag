package zuzusoft.com.bagbag.home.models.chat;

import java.io.Serializable;
import java.util.List;

import zuzusoft.com.bagbag.chat.model.Message;

public class DataChatWindow implements Serializable {

    private String chatId;
    private int chatStatus;
    private String petitionId;
    private String rosterId;
    private String rosterName;
    private String distance;
    private String rosterImage;
    private List<Message> messageList;
    private RosterBag rosterBag;
    private MyBag myBag;

    public RosterBag getRosterBag() {
        return rosterBag;
    }

    public void setRosterBag(RosterBag rosterBag) {
        this.rosterBag = rosterBag;
    }

    public MyBag getMyBag() {
        return myBag;
    }

    public void setMyBag(MyBag myBag) {
        this.myBag = myBag;
    }

    public int getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(int chatStatus) {
        this.chatStatus = chatStatus;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getPetitionId() {
        return petitionId;
    }

    public void setPetitionId(String petitionId) {
        this.petitionId = petitionId;
    }

    public String getRosterId() {
        return rosterId;
    }

    public void setRosterId(String rosterId) {
        this.rosterId = rosterId;
    }

    public String getRosterName() {
        return rosterName;
    }

    public void setRosterName(String rosterName) {
        this.rosterName = rosterName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRosterImage() {
        return rosterImage;
    }

    public void setRosterImage(String rosterImage) {
        this.rosterImage = rosterImage;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }


}
