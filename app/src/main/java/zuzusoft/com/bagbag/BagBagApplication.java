package zuzusoft.com.bagbag;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.devs.acr.AutoErrorReporter;

import zuzusoft.com.bagbag.localDB.ChatDatabase;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class BagBagApplication extends MultiDexApplication {

    private static final String DATABASE_NAME = "bagbag_chat.db_db";
    private static volatile ChatDatabase instance;

    public static synchronized ChatDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static ChatDatabase create(final Context context) {

        return Room.databaseBuilder(context,
                ChatDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

    }

    @Override
    public void onCreate() {
        super.onCreate();

        AutoErrorReporter.get(this)
                .setEmailAddresses("zuzuauthor@gmail.com")
                .setEmailSubject("BagBag Crash Report")
                .start();

    }

}
