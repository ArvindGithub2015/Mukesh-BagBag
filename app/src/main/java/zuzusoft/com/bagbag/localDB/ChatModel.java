package zuzusoft.com.bagbag.localDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class ChatModel {

    @NonNull
    @PrimaryKey
    private String chatId;

    private String message;

    @NonNull
    public String getChatId() {
        return chatId;
    }

    public void setChatId(@NonNull String chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "ChatModel{" +
                "chatId='" + chatId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
