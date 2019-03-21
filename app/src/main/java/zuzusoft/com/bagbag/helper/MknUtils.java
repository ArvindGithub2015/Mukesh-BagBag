package zuzusoft.com.bagbag.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mukeshnarayan on 15/05/18.
 */

public class MknUtils {

    public static String PRIVATE_KEY;
    public static String PUBLIC_KEY;

    public static final String READ_TOKEN = "read_token";
    public static final String NETWORK_ERROR = "No Internet Connection!";

    public static final String URL_QUOTE_IMAGE = "http://zuzusoft.co.in/zuzusoft.co.in/hope/img/quoteimage/";
    public static final String URL_BLOG_IMAGE = "http://zuzusoft.co.in/zuzusoft.co.in/hope/img/blogimage/";
    public static final String URL_PRODUCT_IMAGE = "http://zuzusoft.co.in/zuzusoft.co.in/hope/img/productimage/";
    public static final String URL_PROFILE_IMAGE = "http://zuzusoft.co.in/zuzusoft.co.in/hope/img/photo/";
    public static final String URL_BAG_IMAGE = "https://bagbag.app/admin/bagbag/img/bag/";

    public static final String KEY_READ_BG = "quote_of_the_day";


    public static void saveToken(Context context, String valueKey, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(valueKey, value);
        edit.commit();
    }

    public static String readToken(Context context, String valueKey, String valueDefault) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getString(valueKey, valueDefault);
    }

}
