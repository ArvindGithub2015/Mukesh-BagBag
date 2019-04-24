package zuzusoft.com.bagbag.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.chat.ChatListAdapter;
import zuzusoft.com.bagbag.chat.ChatWindowActivity;
import zuzusoft.com.bagbag.helper.DialogHelper;
import zuzusoft.com.bagbag.helper.MknUtils;
import zuzusoft.com.bagbag.helper.SessionManager;
import zuzusoft.com.bagbag.home.adapters.NewMatchesAdapter;
import zuzusoft.com.bagbag.home.adapters.RecyclerViewAdapter;
import zuzusoft.com.bagbag.home.models.chat.ChatList;
import zuzusoft.com.bagbag.home.models.chat.ChatListResponse;
import zuzusoft.com.bagbag.home.models.chat.DataChatWindow;
import zuzusoft.com.bagbag.home.models.chat.NewMatch;
import zuzusoft.com.bagbag.rest.ApiClient;
import zuzusoft.com.bagbag.rest.ApiInterface;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class MyChatsFragment extends Fragment {

    @BindView(R.id.chatList)
    ListView chatList;

    android.support.v7.widget.RecyclerView newMatchRecyclerView;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

//    @BindView(R.id.emptyViewChat)
//    LinearLayout emptyViewChat;

    ImageView emptyViewNewMatch;
    private DialogHelper dialogHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //spread butter
        ButterKnife.bind(this, view);

        dialogHelper = new DialogHelper(getActivity());
        dialogHelper.setDialogInfo("", "Please Wait...");

        updateChatListHeader();

        // Inflate the header view and attach it to the ListView


        //init recycler view
        newMatchRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        newMatchRecyclerView.setLayoutManager(mLayoutManager);

        //checkViewEmpty(1);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyChatList();
            }
        });

        getMyChatList();

    }

    private void updateChatListHeader() {

        // Inflate the header view and attach it to the ListView
        View headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.listview_header, chatList, false);

        headerView.setElevation(0);
        //init child views
        newMatchRecyclerView = headerView.findViewById(R.id.newMatchRecyclerView);
        emptyViewNewMatch = headerView.findViewById(R.id.emptyViewNewMatch);

        //add chat list header
        chatList.addHeaderView(headerView);
    }


    /**
     * Get My Chat List
     */
    private void getMyChatList() {

        if (ApiClient.isNetworkAvailable(getActivity())) {

            myChatApi();

        } else {

            Toast.makeText(getActivity(), MknUtils.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void myChatApi() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        //dialogHelper.showProgressDialog();
        swipeRefresh.setRefreshing(true);


        SessionManager sessionManager = new SessionManager(getContext());
        String publicKey = sessionManager.getUserDetails().get(SessionManager.KEY_PUBLIC);
        String privateKey = sessionManager.getUserDetails().get(SessionManager.KEY_PRIVATE);

        Call<ChatListResponse> call = apiService.getMyChats(publicKey, privateKey);

        call.enqueue(new Callback<ChatListResponse>() {
            @Override
            public void onResponse(Call<ChatListResponse> call, Response<ChatListResponse> response) {

                //dialogHelper.closeDialog();
                swipeRefresh.setRefreshing(false);
                //KEY_REFRESH_MY_CLOSET = false;

                int code = response.code();
                if (code == 200) {

                    if (response.body().getStatus()) {

                        //get new matches and chat history
                        List<ChatList> dataSetChat = response.body().getChatList();

                        List<NewMatch> dataSetNewMatch = response.body().getNewMatches();
                        Log.d("TAG", "onResponse: New Matches" + dataSetNewMatch.size());
                        Log.d("TAG", "onResponse: Chat List" + dataSetChat.size());

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                        newMatchRecyclerView.setLayoutManager(layoutManager);
                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), dataSetNewMatch);
                        newMatchRecyclerView.setAdapter(adapter);
                        newMatchRecyclerView.setVisibility(View.VISIBLE);

                        updateChatList(dataSetChat);

                        //chat history check
                        if (!dataSetChat.isEmpty() && !dataSetNewMatch.isEmpty()) {

                            updateNewMatchRecycler(dataSetNewMatch);
//                            updateChatList(dataSetChat);
                            checkViewEmpty(4);

                        } else if (!dataSetChat.isEmpty() && dataSetNewMatch.isEmpty()) {

                            checkViewEmpty(3);


                        } else if (dataSetChat.isEmpty() && !dataSetNewMatch.isEmpty()) {

                            updateNewMatchRecycler(dataSetNewMatch);
                            checkViewEmpty(2);


                        } else {

                            //when both list empty
                            checkViewEmpty(5);
                        }

                    } else {

                        checkViewEmpty(5);
                        //Toast.makeText(getActivity(), "Error POResponse Code: " + code, Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ChatListResponse> call, Throwable t) {

                //dialogHelper.closeDialog();
                swipeRefresh.setRefreshing(false);
                t.printStackTrace();

            }
        });
    }


    private void updateChatList(List<ChatList> dataSetChat) {

        chatList.setAdapter(new ChatListAdapter(getActivity(), dataSetChat));
    }

    private void updateNewMatchRecycler(List<NewMatch> dataSetNewMatch) {

        NewMatchesAdapter mAdapter = new NewMatchesAdapter(getContext(), dataSetNewMatch);

        mAdapter.setOnThumbnailClickListener(new NewMatchesAdapter.OnThumbnailClickListener() {
            @Override
            public void onThumbnailClick(NewMatch bagData, int position) {

                Intent intent = new Intent(getActivity(), ChatWindowActivity.class);
                DataChatWindow dataChat = buildChatDataFromNewMatch(bagData);
                intent.putExtra(ChatWindowActivity.KEY_BUNDLE_CHAT, dataChat);
                startActivity(intent);
            }
        });

        newMatchRecyclerView.setAdapter(mAdapter);
        newMatchRecyclerView.getVisibility();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        chatList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ChatList cl = (ChatList) chatList.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ChatWindowActivity.class);
                DataChatWindow dataChat = buildChatDataFromChatList(cl);
                intent.putExtra(ChatWindowActivity.KEY_BUNDLE_CHAT, dataChat);
                startActivity(intent);

            }
        });

    }

    @NonNull
    private DataChatWindow buildChatDataFromChatList(ChatList cl) {

        DataChatWindow dataChat = new DataChatWindow();
        dataChat.setChatId(cl.getChatId());
        dataChat.setDistance("2");
        dataChat.setChatStatus(cl.getChatStatus());
        dataChat.setMyBag(cl.getMyBag());
        dataChat.setRosterBag(cl.getRosterBag());
        dataChat.setMessageList(null);

        return dataChat;
    }

    @NonNull
    private DataChatWindow buildChatDataFromNewMatch(NewMatch cl) {

        DataChatWindow dataChat = new DataChatWindow();
        dataChat.setChatId(cl.getChatId());
        dataChat.setDistance("2");
        dataChat.setChatStatus(cl.getChatStatus());
        dataChat.setMyBag(cl.getMyBag());
        dataChat.setRosterBag(cl.getRosterBag());
        dataChat.setMessageList(null);

        return dataChat;
    }

    private void checkViewEmpty(int mode) {

        switch (mode) {

            case 1:
                //default, hide both listview and empty view
//                emptyViewChat.setVisibility(View.GONE);

                newMatchRecyclerView.setVisibility(View.GONE);
                emptyViewNewMatch.setVisibility(View.GONE);

                break;


            case 2:
                //when new match ok and chat history empty
//                emptyViewChat.setVisibility(View.VISIBLE);

                newMatchRecyclerView.setVisibility(View.VISIBLE);
                emptyViewNewMatch.setVisibility(View.GONE);

                break;


            case 3:
                //when new match empty and chat history ok
//                emptyViewChat.setVisibility(View.GONE);

                newMatchRecyclerView.setVisibility(View.GONE);
                emptyViewNewMatch.setVisibility(View.VISIBLE);

                break;

            case 4:
                //when new match ok and chat history ok
//                emptyViewChat.setVisibility(View.GONE);

                newMatchRecyclerView.setVisibility(View.VISIBLE);
                emptyViewNewMatch.setVisibility(View.GONE);

                break;

            case 5:
                //when new match ok and chat history ok
//                emptyViewChat.setVisibility(View.VISIBLE);

                newMatchRecyclerView.setVisibility(View.GONE);
                emptyViewNewMatch.setVisibility(View.VISIBLE);

                break;

        }

    }


}
