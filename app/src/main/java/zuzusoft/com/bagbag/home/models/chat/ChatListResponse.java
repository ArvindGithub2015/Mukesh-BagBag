
package zuzusoft.com.bagbag.home.models.chat;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatListResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("new_matches")
    @Expose
    private List<NewMatch> newMatches = null;
    @SerializedName("chat_list")
    @Expose
    private List<ChatList> chatList = null;
    private final static long serialVersionUID = 6762184699115374166L;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NewMatch> getNewMatches() {
        return newMatches;
    }

    public void setNewMatches(List<NewMatch> newMatches) {
        this.newMatches = newMatches;
    }

    public List<ChatList> getChatList() {
        return chatList;
    }

    public void setChatList(List<ChatList> chatList) {
        this.chatList = chatList;
    }


    @Override
    public String toString() {
        return "ChatListResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", newMatches=" + newMatches +
                ", chatList=" + chatList +
                '}';
    }
}
