package zuzusoft.com.bagbag;

import android.support.multidex.MultiDexApplication;

import com.devs.acr.AutoErrorReporter;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class BagBagApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        /*AutoErrorReporter.get(this)
                .setEmailAddresses("zuzuauthor@gmail.com")
                .setEmailSubject("BagBag Crash Report")
                .start();*/
    }
}
