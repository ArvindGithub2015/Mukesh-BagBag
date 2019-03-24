package zuzusoft.com.bagbag.helper.xmpp;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by mukeshnarayan on 01/01/19.
 */

public class MknXmppService extends JobService {

    private static final String TAG = "MknXmppService";
    private boolean jobCanceled = false;
    MknXmppHelper mknXmppHelper;

    public static XMPPConnection xmppConnection;

    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "onStartJob: OK");

        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    //connect to server
                    mknXmppHelper = new MknXmppHelper(MknXmppService.this, connectionListener);

                }catch (Exception e){

                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }


    private AbstractConnectionListener connectionListener = new AbstractConnectionListener(){

        @Override
        public void connected(XMPPConnection connection) {
            super.connected(connection);

            Log.d(TAG, "connected: Ok");

            xmppConnection = connection;
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            super.authenticated(connection, resumed);
            Log.d(TAG, "authenticated: Ok");

            //set message listener
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {

                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {

                            Log.d(TAG, "processMessage: "+message.getFrom()+" : "+message.getBody());
                            message.getBody();
                        }
                    });
                }
            });
        }

        @Override
        public void connectionClosed() {
            super.connectionClosed();

            Log.d(TAG, "connectionClosed: Ok");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            super.connectionClosedOnError(e);

            Log.d(TAG, "connectionClosedOnError: Ok");
        }

        @Override
        public void reconnectingIn(int seconds) {
            super.reconnectingIn(seconds);

            Log.d(TAG, "reconnectingIn: Ok");
        }

        @Override
        public void reconnectionFailed(Exception e) {
            super.reconnectionFailed(e);

            Log.d(TAG, "reconnectionFailed: Ok");
        }

        @Override
        public void reconnectionSuccessful() {
            super.reconnectionSuccessful();

            Log.d(TAG, "reconnectionSuccessful: Ok");
        }
    };

}
