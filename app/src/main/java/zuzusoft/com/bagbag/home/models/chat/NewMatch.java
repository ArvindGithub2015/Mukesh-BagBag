
package zuzusoft.com.bagbag.home.models.chat;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewMatch implements Serializable
{

    @SerializedName("chat_id")
    @Expose
    private String chatId;
    @SerializedName("chat_status")
    @Expose
    private Integer chatStatus;
    @SerializedName("new_message_count")
    @Expose
    private Integer newMessageCount;
    @SerializedName("last_message_time")
    @Expose
    private String lastMessageTime;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("xmpp_messages")
    @Expose
    private List<Object> xmppMessages = null;
    @SerializedName("my_bag")
    @Expose
    private MyBag myBag;
    @SerializedName("roster_bag")
    @Expose
    private RosterBag rosterBag;
    private final static long serialVersionUID = 820701402128354185L;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Integer getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(Integer chatStatus) {
        this.chatStatus = chatStatus;
    }

    public Integer getNewMessageCount() {
        return newMessageCount;
    }

    public void setNewMessageCount(Integer newMessageCount) {
        this.newMessageCount = newMessageCount;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<Object> getXmppMessages() {
        return xmppMessages;
    }

    public void setXmppMessages(List<Object> xmppMessages) {
        this.xmppMessages = xmppMessages;
    }

    public MyBag getMyBag() {
        return myBag;
    }

    public void setMyBag(MyBag myBag) {
        this.myBag = myBag;
    }

    public RosterBag getRosterBag() {
        return rosterBag;
    }

    public void setRosterBag(RosterBag rosterBag) {
        this.rosterBag = rosterBag;
    }

}
