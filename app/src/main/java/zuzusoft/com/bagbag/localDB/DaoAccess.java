package zuzusoft.com.bagbag.localDB;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleChat(ChatModel chatModel);

    @Insert
    void insertMultipleChats(List<ChatModel> chatModelList);

    /*
    // Example for live data
    @Query("SELECT * FROM orders WHERE order_id =:orderId")
    LiveData<Order> getLiveOrderById(int orderId);*/

    @Query("SELECT * FROM ChatModel WHERE ChatId =:ChatId")
    ChatModel fetchOneChatsByChatId(int ChatId);

    @Query("SELECT * FROM ChatModel ")
    List<ChatModel> getAllChatList();

    @Query("SELECT count(*) FROM  ChatModel")
    int getNoOfChat();

    /**
     * A method, annotated with @Update can return an int. This is the number of updated rows.
     * update will try to update all your fields using the value of the primary key in a where
     * clause. If your entity is not persisted in the database yet, the update query will not be
     * able to find a row and will not update anything.
     */
    @Update
    int updateChat(ChatModel chatModel);

    /*
    Update Multiple Record

    @Update
    int updateChat(ChatModel Chats);*/


    @Delete
    int deleteChat(ChatModel chatModel);

    @Query("DELETE FROM ChatModel WHERE ChatId = :ChatId")
    int deleteChatById(String ChatId);


    @Query("UPDATE ChatModel SET message=:name WHERE ChatId = :chat_Id")
    void updateChatName(String name, String chat_Id);


    @Delete
    void delete(ChatModel chatModel);

    @Query("DELETE FROM  ChatModel")
    int deleteAllRecord();


    /**
     * You can use @Insert(onConflict = OnConflictStrategy.REPLACE). This will try to insert the
     * entity and, if there is an existing row that has the same ID value, it will delete it and
     * replace it with the entity you are trying to insert. Be aware that, if you are using auto
     * generated IDs, this means that the the resulting row will have a different ID than the
     * original that was replaced. If you want to preserve the ID, then you have to come up with
     * a custom way to do it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChatModel chatModel);

}


