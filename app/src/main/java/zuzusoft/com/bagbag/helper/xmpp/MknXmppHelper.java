package zuzusoft.com.bagbag.helper.xmpp;

import android.content.Context;
import android.util.Log;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import zuzusoft.com.bagbag.helper.SessionManager;

/**
 * Created by mukeshnarayan on 01/01/19.
 */

public class MknXmppHelper {

    private static final String TAG = "MknXmppHelper";
    private SessionManager sessionManager;

    //Constructor
    public MknXmppHelper(Context context, AbstractConnectionListener connectionListener) {

        sessionManager = new SessionManager(context);
        //connecting Xmpp Server
        connectXmppServer(getXmppConnection(connectionListener));

    }

    //Join Chat Room
    public static void joinChatRoom(XMPPConnection xmppConnection, String chatId, String nickName,
                                    MessageListener messageListener) {

        try {

            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(xmppConnection);

            //Create MultiUserChat for xmppConnection for a room
            MultiUserChat multiUserChat = manager.getMultiUserChat("chat" + chatId + "@conference.localhost.localdomain");

            //User2 joins the new room
            //the room service will decide the amount of history to send
            multiUserChat.join(nickName);

            multiUserChat.addParticipantListener(new PresenceListener() {
                @Override
                public void processPresence(Presence presence) {

                    Log.d(TAG, "AddParticipantListener : " + presence.getStatus());

                }
            });

            multiUserChat.addMessageListener(messageListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Send Message to Chat Room
    public static void sendGroupMessage(XMPPConnection xmppConnection, String chatId, String message) {

        try {

//            Message msg = new Message("chat" + chatId + "@conference.localhost.localdomain", Message.Type.groupchat);
            Message msg = new Message("chat" + chatId + "@conference.localhost.localdomain", Message.Type.groupchat);
            msg.setBody(message);
//            xmppConnection.sendPacket(msg);
            xmppConnection.sendStanza(msg);

        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageToRoomOccupants(XMPPTCPConnection connection) throws SmackException.NotConnectedException {
        //Message msg = new Message("room789@conference.dishaserver", Message.Type.groupchat);
        //msg.setBody("This is nagarjuna friednds. Please join this room and let us have fun."); connection.sendPacket(msg);
    }

    //Xmpp Configuration
    private AbstractXMPPConnection getXmppConnection(AbstractConnectionListener connectionListener) {

        //Build configuration
        XMPPTCPConnectionConfiguration xmppConfiguration = XMPPTCPConnectionConfiguration.builder()
                .setCompressionEnabled(false)
                .setDebuggerEnabled(MknXmppConstants.MKN_XMPP_DEBUG_ENABLED)
                .setHost(MknXmppConstants.MKN_XMPP_HOST)
                .setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        Log.d(TAG, "verify: hostname " + hostname);
                        return false;
                    }
                })
                .setPort(MknXmppConstants.MKN_XMPP_PORT)
                .setResource(MknXmppConstants.MKN_XMPP_RESOURCE)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(true)
                .setServiceName(MknXmppConstants.MKN_XMPP_HOST)
                .setUsernameAndPassword(sessionManager.getUserDetails().get(SessionManager.KEY_SOCIAL_ID),
                        MknXmppConstants.MKN_XMPP_PASSWORD)
                .build();

        AbstractXMPPConnection tcpConnection = new XMPPTCPConnection(xmppConfiguration);
        tcpConnection.addConnectionListener(connectionListener);

        return tcpConnection;

    }

    //Connect to server and login
    private void connectXmppServer(final AbstractXMPPConnection connection) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //connect now
                    connection.connect();

                    if (connection.isConnected())
                        Log.d(TAG, "run: isConnected OK");

                    connection.login();

                    if (connection.isAuthenticated())
                        Log.d(TAG, "run: isAuthenticated OK");

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }
        }).start();

    }

    //Get Chat room info
    public void getChatRoomInfo(XMPPConnection xmppConnection) {

        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(xmppConnection);
            RoomInfo roomInfo = manager.getRoomInfo("chat21@conference.localhost.localdomain");
            Log.d(TAG, "getChatRoomInfo: " + roomInfo.getDescription());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<HostedRoom> getHostRooms(XMPPConnection xmppConnection) {
        List<HostedRoom> roominfos = new ArrayList<HostedRoom>();
        try {
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(xmppConnection);
            Collection<HostedRoom> hostrooms = manager.getHostedRooms("conference.localhost.localdomain");

            for (HostedRoom entry : hostrooms) {
                roominfos.add(entry);
                Log.i("room", "first name：" + entry.getName() + " - ID:" + entry.getJid());
            }
            Log.i("room", "Number of service meetings:" + roominfos.size());
        } catch (XMPPException e) {
            Log.e("getHostRooms", e.getMessage());
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }
        return roominfos;
    }

}
