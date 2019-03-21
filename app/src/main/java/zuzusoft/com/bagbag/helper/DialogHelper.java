package zuzusoft.com.bagbag.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import zuzusoft.com.bagbag.R;


/**
 * Created by mukeshnarayan on 15/05/18.
 */

public class DialogHelper {

    private Context context;
    private ProgressDialog dialog;

    public interface MyDialogListener{

        void onClickOk();
        void onClickNo();
    }

    public DialogHelper(Context context){

        this.context = context;

    }

    public void setDialogInfo(String title, String message){

        dialog = new ProgressDialog(context, R.style.AlertDialogTheme);
        dialog.setCancelable(false);

        dialog.setTitle(title);
        dialog.setMessage(message);

    }

    public void setDialogInfoFull(String title, String message){

        dialog = new ProgressDialog(context, R.style.Fullscreen1);
        dialog.setCancelable(false);

        dialog.setTitle(title);
        dialog.setMessage(message);

    }

    public void showProgressDialog(){

        if(dialog != null){

            dialog.show();
        }
    }

    public void closeDialog(){

        if(dialog != null){

            dialog.dismiss();
        }
    }

    public static void showMessageDialog(Context context, String title, String message, final MyDialogListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(listener != null){

                    listener.onClickOk();

                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(listener != null){

                    listener.onClickNo();

                }

            }
        });

        builder.create().show();
    }

    public void showNotificationDialog(Context context, String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               // sendFollowAccept(followId, userId);

            }
        });

        builder.create().show();
    }

}
