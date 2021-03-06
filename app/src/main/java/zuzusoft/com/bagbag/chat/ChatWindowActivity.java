package zuzusoft.com.bagbag.chat;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.MutableLiveData;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Domainpart;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.chat.model.Message;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.helper.xmpp.MknXmppConstants;
import zuzusoft.com.bagbag.helper.xmpp.MknXmppHelper;
import zuzusoft.com.bagbag.helper.xmpp.MknXmppService;
import zuzusoft.com.bagbag.home.models.chat.DataChatWindow;

/**
 * Created by mukeshnarayan on 17/11/18.
 */

public class ChatWindowActivity extends BaseActivity implements BagExchangeDialog.OnBagListener {


    public static final String KEY_BUNDLE_CHAT = "chat_bundle";

    private static final String TAG = ChatWindowActivity.class.getSimpleName();

    @BindView(R.id.btnSend)
    ImageView btnSend;
    @BindView(R.id.inputMessage)
    EditText inputMessage;
    @BindView(R.id.chat)
    ListView chat;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    //roster bag image
    @BindView(R.id.bagImage1)
    ImageView bagImage1;
    //my bag image
    @BindView(R.id.bagImage2)
    ImageView bagImage2;
    @BindView(R.id.btnExchange)
    ImageView btnExchange;
    @BindView(R.id.profileImage)
    CircleImageView profileImage;
    @BindView(R.id.rosterName)
    TextView rosterName;
    @BindView(R.id.rosterDistance)
    TextView rosterDistance;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    private List<Message> dataSet;
    private DialogHelper dialogHelper;
    private DataChatWindow chatData;

    public ChatWindowActivity() throws XmppStringprepException {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_window);
        initViews();

    }

    //   Typing start
    private void addInputMessageChatListener() {

        inputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkTextLength(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void checkTextLength(CharSequence charSequence) {

        if (!TextUtils.isDigitsOnly(charSequence)){
            try {
                sendChatState(ChatState.composing);
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }
        }

    }

    private MultiUserChat multiUserChat = null;
    private  MutableLiveData liveChatState = new MutableLiveData<String>();
    private Handler chatStateHandler = new Handler(Looper.getMainLooper());
    private String NAMESPACE = "http://jabber.org/protocol/chatstates";
    private Boolean isComposingChatStateSent = false;
    private  Boolean isPausedChatStateSent = false;
    private String username;
    private CompositeDisposable disposable  = new CompositeDisposable();
    private DomainBareJid xmppServiceGroupDomain = JidCreate.domainBareFrom("domain_name_for_group_chat");


    private void sendChatState(ChatState chatState) throws XmppStringprepException {
        if (MknXmppService.xmppConnection != null && MknXmppService.xmppConnection.isConnected()) {
            if (!isComposingChatStateSent || ! isPausedChatStateSent) {
                org.jivesoftware.smack.packet.Message chatStateMessage = new org.jivesoftware.smack.packet.Message();

                //create empty body message
                chatStateMessage.setBody(null);
                chatStateMessage.setType( org.jivesoftware.smack.packet.Message.Type.groupchat);
                chatStateMessage.setSubject(null);
                chatStateMessage.setTo("chat"+chatData.getChatId());

                //the user who is typing i.e. logged in user
                chatStateMessage.setFrom(JidCreate.bareFrom(Localpart.from(
                        sessionManager.getUserDetails().get(SessionManager.KEY_SOCIAL_ID)))+"@conference.localhost.localdomain");


                //create extension using ChatStateExtension
                //this extension will be fetched in incoming message listener and is explained below
                ChatStateExtension extension = new ChatStateExtension(chatState);
                chatStateMessage.addExtension(extension);
                /*if (!isComposingChatStateSent && chatState == ChatState.composing) {
                    //message sent with composing(typing) event
                    multiUserChat = new MultiUserChat();
                    multiUserChat?.sendMessage(chatStateMessage)
                    isComposingChatStateSent = true
                    //message sent with pause event  after 3 seconds
                    //code is written below in chatStaePauseRunnable
                    chatStateHandler.postDelayed(chatStatePausedRunnable, 3000)
                } else {
                    if (!isPausedChatStateSent && chatState == ChatState.paused) {
                        multiUserChat?.sendMessage(chatStateMessage)
                        isPausedChatStateSent = true

                        disposable.add(Observable.interval(6000L, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(
                                        { t ->
                        if (isComposingChatStateSent && isPausedChatStateSent) {
                            isComposingChatStateSent = false
                            isPausedChatStateSent = false
                        }
                                    },
                        {},
                        {}
                   ))
                    }
                }*/

            }
        }

    }

    //   Typing end


    private void initViews() {

        //spread butterbtnSend
        ButterKnife.bind(this);

        dialogHelper = new DialogHelper(context);
        dialogHelper.setDialogInfo("", "Please Wait...");

        chatData = (DataChatWindow) getIntent().getSerializableExtra(KEY_BUNDLE_CHAT);

        if (chatData == null) {

            finish();
            return;

        }

        bagImage1.setOnClickListener(this);
        bagImage2.setOnClickListener(this);
        btnExchange.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        updateViews();


        if (MknXmppService.xmppConnection != null) {
//            MknXmppHelper.joinChatRoom(MknXmppService.xmppConnection, chatData.getChatId(),
//                    chatData.getRosterBag().getUserName());

            MknXmppHelper.joinChatRoom(MknXmppService.xmppConnection, chatData.getChatId(),
                    sessionManager.getUserDetails().get(SessionManager.KEY_SOCIAL_ID) /*"c9vf46siqdupvvwzw6qlufns6bg3" chatData.getRosterBag().getUserId()*/, new MessageListener() {
                        @Override
                        public void processMessage(final org.jivesoftware.smack.packet.Message message) {
                            System.out.println(" Received group cht messages...Received group cht messages... ");

                            String otherOccupentId = message.getFrom().split("/")[1];


                            /*runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setMessage(message.getBody(), false);
                                }
                            });*/

                            if (!otherOccupentId.equals(sessionManager.getUserDetails().get(SessionManager.KEY_SOCIAL_ID))) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        /*Toast toast = Toast.makeText(context, message.getBody(), Toast.LENGTH_SHORT);
                                        View view = toast.getView();
                                        view.getBackground().setColorFilter(Color.parseColor("#46B0D5"), PorterDuff.Mode.SRC_IN);
                                        TextView text = view.findViewById(android.R.id.message);
                                        text.setTextColor(Color.parseColor("#FFFFFF"));
                                        toast.show();*/

                                        setMessage(message.getBody(), false);
                                    }
                                });

                            } else {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setMessage(message.getBody(), true);
                                    }
                                });

                            }


                        }
                    });

        } else {
            Toast.makeText(context, "Xmpp connection null : Unable to join room = chat" + chatData.getChatId(),
                    Toast.LENGTH_SHORT).show();
        }

    }


    /*
     * By Arvind
     * as of now we are binding message but it is not correct way to deo this
     * */
    private void setMessage(String message, boolean isSelf) {
        Message m = new Message();
        m.setSelf(isSelf);
        m.setMessage(message + "\n");
        dataSet.add(m);
        checkViewEmpty(3);
        ((ChatMessageAdapter) chat.getAdapter()).notifyDataSetChanged();

        scrollMyListViewToBottom();
    }

    private void updateViews() {

        String image1, image2;

        //check chat status
        if (chatData.getChatStatus() == 4) {

            image1 = chatData.getMyBag().getMyBagAllimage().get(0).getBagUrl();
            image2 = chatData.getRosterBag().getMyBagAllimage().get(0).getBagUrl();

        } else {

            image1 = chatData.getRosterBag().getMyBagAllimage().get(0).getBagUrl();
            image2 = chatData.getMyBag().getMyBagAllimage().get(0).getBagUrl();

        }


        //set bag images
        Glide.with(context)
                .load(image2)
                // .placeholder(R.raw.default_profile)
                .into(bagImage2);

        Glide.with(context)
                .load(image1)
                // .placeholder(R.raw.default_profile)
                .into(bagImage1);


        //set roster profile image
        Glide.with(context)
                .load(chatData.getRosterBag().getUserImage())
                // .placeholder(R.raw.default_profile)
                .into(profileImage);

        rosterName.setText(chatData.getRosterBag().getUserName());
        rosterDistance.setText("a " + chatData.getDistance() + " km distance");


        if (chatData.getMessageList() != null && !chatData.getMessageList().isEmpty()) {

            dataSet = getMessageData();
            chat.setAdapter(new ChatMessageAdapter(context, dataSet));
            scrollMyListViewToBottom();
            checkViewEmpty(3);

        } else {

            dataSet = new ArrayList<>();
            chat.setAdapter(new ChatMessageAdapter(context, dataSet));
            checkViewEmpty(2);

        }

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.btnBack:

                finish();

                break;

            case R.id.bagImage1:

                gotoActivity(ChatBagDetailsActivity.class, getBagData(1, chatData.getChatStatus()));

                break;

            case R.id.bagImage2:

                gotoActivity(ChatBagDetailsActivity.class, getBagData(2, chatData.getChatStatus()));

                break;

            case R.id.btnExchange:

                BagExchangeDialog bed = new BagExchangeDialog();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                bed.show(fragmentManager, "BED");

                break;

            case R.id.btnSend:

                if (!inputMessage.getText().toString().isEmpty()) {

                    /*Message m = new Message();
                    m.setSelf(true);
                    m.setMessage(inputMessage.getText().toString() + "\n");
                    dataSet.add(m);
                    checkViewEmpty(3);
                    ((ChatMessageAdapter) chat.getAdapter()).notifyDataSetChanged();

                    scrollMyListViewToBottom();*/

                    if (MknXmppService.xmppConnection != null) {
                        MknXmppHelper.sendGroupMessage(MknXmppService.xmppConnection,
                                chatData.getChatId(), inputMessage.getText().toString());
                    }

                    inputMessage.setText("");


                } else {

                    scrollMyListViewToBottom();

                }

                break;

        }
    }

    private Bundle getBagData(int mode, int chatStatus) {

        Bundle args = new Bundle();

        args.putSerializable(ChatBagDetailsActivity.KEY_BUNDLE_BAG_DATA, chatData.getRosterBag());
        args.putInt(ChatBagDetailsActivity.KEY_BUNDLE_WHICH_BAG, 2);
        return args;
    }


    private void scrollMyListViewToBottom() {
        chat.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chat.setSelection(chat.getAdapter().getCount() - 1);
            }
        });
    }

    /*private void btnSendListener() {

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!inputMessage.getText().toString().isEmpty()){

                    Message m = new Message();
                    m.setSelf(true);
                    m.setMessage(inputMessage.getText().toString()+"\n");
                    dataSet.add(m);
                    ((ChatMessageAdapter) chat.getAdapter()).notifyDataSetChanged();

                    scrollMyListViewToBottom();

                    inputMessage.setText("");

                }else{

                    scrollMyListViewToBottom();

                }

            }
        });
    }*/

    private List<Message> getMessageData() {

        List<Message> dataSet = new ArrayList<>();

        Message m1 = new Message();
        m1.setMessage("Hello, How are you? ");
        m1.setSelf(true);


        Message m2 = new Message();
        m2.setMessage("I used to think I was indecisive, but now I'm not too sure. \n");
        m2.setSelf(false);

        Message m3 = new Message();
        m3.setMessage("Maybe you should eat some makeup so you can be pretty on the inside too. \n");
        m3.setSelf(true);


        Message m4 = new Message();
        m4.setMessage("A bank is a place that will lend you money if you can prove that you don't need it. \n");
        m4.setSelf(false);


        Message m5 = new Message();
        m5.setMessage("One advantage of talking to yourself is that you know at least somebody's listening. \n");
        m5.setSelf(false);


        Message m6 = new Message();
        m6.setMessage("The answer you're looking for is inside of you, but it's wrong \n");
        m6.setSelf(true);


        Message m7 = new Message();
        m7.setMessage("I am not lazy, I am on energy saving mode. \n");
        m7.setSelf(true);

        Message m8 = new Message();
        m8.setMessage("Doing nothing is hard, you never know when you're done. \n");
        m8.setSelf(false);

        Message m9 = new Message();
        m9.setMessage("An apple a day keeps anyone away if you throw it hard enough. \n");
        m9.setSelf(false);

        dataSet.add(m1);
        dataSet.add(m2);
        dataSet.add(m3);
        dataSet.add(m4);
        dataSet.add(m5);
        dataSet.add(m6);
        dataSet.add(m7);
        dataSet.add(m8);
        dataSet.add(m9);

        return dataSet;

    }

    private void checkViewEmpty(int mode) {

        switch (mode) {

            case 1:
                //default, hide both listview and empty view
                chat.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);

                break;


            case 2:
                //when my closet empty
                chat.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

                break;


            case 3:
                //when my closet not empty
                chat.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                break;
        }

    }

    @Override
    public void onBagExchange() {

        finish();
    }

    @Override
    public void onBagExchangeCancel() {

        finish();
    }

    private void startJobService() {

        ComponentName componentName = new ComponentName(this, MknXmppService.class);
        JobInfo jobInfo = new JobInfo.Builder(MknXmppConstants.MKN_XMPP_JOB_ID, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {

            Log.d(MknXmppConstants.MKN_XMPP_CHAT_TAG, "startJobService: True");

        } else {

            Log.d(MknXmppConstants.MKN_XMPP_CHAT_TAG, "startJobService: False");
        }

    }


}
