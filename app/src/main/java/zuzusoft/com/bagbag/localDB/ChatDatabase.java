package zuzusoft.com.bagbag.localDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ChatModel.class}, version = 1, exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase {

    public abstract DaoAccess getDaoAccess();


}
