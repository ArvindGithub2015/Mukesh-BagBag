package zuzusoft.com.bagbag.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mukeshnarayan on 24/06/18.
 */

public class MknHelper {

    public static final String READ_TOKEN = "read_token";
    public static final String NETWORK_ERROR = "No Internet Connection!";
    public static final String NO_BAG_IMAGE_FILE_NAME = "no_bag_image";


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





    private static int[] colorList = {

            Color.parseColor("#AA00FF"),
            Color.parseColor("#FF00FF"),
            Color.parseColor("#FF00A9"),
            Color.parseColor("#FF0000"),
            Color.parseColor("#FFA400"),
            Color.parseColor("#DBFF00"),
            Color.parseColor("#00FF00"),
            Color.parseColor("#00FFC5"),
            Color.parseColor("#00CFFF"),
            Color.parseColor("#2139FF")


    };


    private static int[] colorList1 = {

            Color.parseColor("#000000"),
            Color.parseColor("#323232"),
            Color.parseColor("#ff4b4b"),
            Color.parseColor("#323296"),
            Color.parseColor("#ffa52e"),
            Color.parseColor("#9e1fff"),
            Color.parseColor("#008744"),
            Color.parseColor("#666547"),
            Color.parseColor("#00aedb"),
            Color.parseColor("#d41243")


    };


    public static Bitmap changeColor(int mode, int color, Bitmap bitmap){

        if(mode == 1){

            int [] allpixels = new int [bitmap.getHeight() * bitmap.getWidth()];
            bitmap.getPixels(allpixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            for(int i = 0; i < allpixels.length; i++)
            {
                if(allpixels[i] == Color.BLACK){

                    allpixels[i] = color;
                }

            }

            bitmap.setPixels(allpixels,0,bitmap.getWidth(),0, 0, bitmap.getWidth(),bitmap.getHeight());

            return bitmap;

        }else{

            return getMultiColorBitmap(bitmap);

        }

    }

    private static Bitmap getMultiColorBitmap(Bitmap bitmap) {

        //int [] allpixels = new int [bitmap.getHeight() * bitmap.getWidth()];

        Bitmap[] bitmaps = splitBitmap(bitmap, 10, 1);
        //change color of individual bitmap
        Bitmap source = combineImageIntoOne(bitmaps, bitmap);

        return source;
    }



    private static Bitmap combineImageIntoOne(Bitmap[] bitmap, Bitmap source) {

        int w = 0, h = 0;

        for (int i = 0; i < bitmap.length; i++) {

            if (i < bitmap.length - 1) {
                w = bitmap[i].getWidth() > bitmap[i + 1].getWidth() ? bitmap[i].getWidth() : bitmap[i + 1].getWidth();
            }

            h += bitmap[i].getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Log.d("HTML", "Width: "+temp.getWidth()+", "+temp.getHeight());
        Canvas canvas = new Canvas(temp);
        int left = 0;
        for (int i = 0; i < bitmap.length; i++) {
            Log.d("HTML", "Combine: "+i+"/"+bitmap.length+1);

            Bitmap colorBitmap = changeBitmapColor(i,bitmap[i]);

            left = (i == 0 ? 0 : left+bitmap[i].getWidth());

            canvas.drawBitmap(colorBitmap, left, 0f, null);
        }

        return temp;
    }

    private static Bitmap changeBitmapColor(int j, Bitmap bitmap) {

        int [] allpixels = new int [bitmap.getHeight() * bitmap.getWidth()];
        bitmap.getPixels(allpixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for(int i = 0; i < allpixels.length; i++) {
            if(allpixels[i] == Color.BLACK){

                if(i == 8){

                    allpixels[i] = colorList[0];

                }else{

                    allpixels[i] = colorList[j];
                }

            }

        }

        bitmap.setPixels(allpixels,0,bitmap.getWidth(),0, 0, bitmap.getWidth(),bitmap.getHeight());

        return bitmap;

    }


    private static Bitmap[] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
        // Allocate a two dimensional array to hold the individual images.
        Bitmap[] bitmaps = new Bitmap[xCount];
        int width, height;
        // Divide the original bitmap width by the desired vertical column count
        width = bitmap.getWidth() / xCount;
        // Divide the original bitmap height by the desired horizontal row count
        height = bitmap.getHeight() / yCount;
        // Loop the array and create bitmaps for each coordinate
        for(int x = 0; x < xCount; ++x) {

            for(int y = 0; y < yCount; ++y) {

                bitmaps[x] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);

            }
        }
        // Return the array
        return bitmaps;
    }




    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }



    public static void addAsContactConfirmed(final Context context, final String name,
                                             final String mobile, final String email) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, mobile);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);

        context.startActivity(intent);

    }



    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static int getImageRaw(Context context, String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "raw", context.getPackageName());

        return drawableResourceId;
    }

    public static int getImageDrawable(Context context, String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }


}
