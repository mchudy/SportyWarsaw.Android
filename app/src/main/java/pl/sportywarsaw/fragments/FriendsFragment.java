package pl.sportywarsaw.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;
import pl.sportywarsaw.activities.SearchFriendsActivity;
import pl.sportywarsaw.adapters.FriendsRecyclerViewAdapter;
import pl.sportywarsaw.adapters.SportsFacilitiesRecyclerViewAdapter;
import pl.sportywarsaw.infrastructure.CustomCallback;
import pl.sportywarsaw.models.UserModel;
import pl.sportywarsaw.services.UserService;
import pl.sportywarsaw.utils.DividerItemDecoration;
import retrofit.Call;

public class FriendsFragment extends Fragment implements FriendsRecyclerViewAdapter.RemoveFriendCallback{
    private FriendsRecyclerViewAdapter adapter;
    private List<UserModel> items = new ArrayList<>();
    private ProgressBar progressBar;

    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private boolean allPagesLoaded = false;


    @Inject UserService service;
    private RecyclerView recyclerView;

    public FriendsFragment() {
    }

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getServicesComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        Context context = view.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        adapter = new FriendsRecyclerViewAdapter(items, recyclerView, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton but = (FloatingActionButton) view.findViewById(R.id.add_friendbutton);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchFriendsActivity.class);
                startActivity(intent);
            }
        });
        showProgressBar();
        adapter.setOnLoadMoreListener(new SportsFacilitiesRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadFriends(adapter);
            }
        });
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        loadFriends(adapter);
        return view;
    }

    //TODO: pagination
    private void loadFriends(final FriendsRecyclerViewAdapter adapter) {
        adapter.showProgressBar();
        Call<List<UserModel>> call = service.getMyFriends();
        call.enqueue(new CustomCallback<List<UserModel>>(getActivity()) {
            @Override
            public void onSuccess(List<UserModel> models) {
                adapter.hideProgressBar();
                for (UserModel model : models) {
                    items.add(model);
                    adapter.notifyItemInserted(items.size());
                }
                adapter.setLoaded();
            }
            @Override
            public void always() {
                hideProgressBar();
            }
        });
    }

    private void hideProgressBar() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeFriend(final String username, final int position) {
        showProgressBar();
        Call<ResponseBody> call = service.removeFriend(username);
        call.enqueue(new CustomCallback<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(ResponseBody model) {
                Toast.makeText(getActivity(), getString(R.string.message_friend_removed, username),
                        Toast.LENGTH_LONG).show();
                items.remove(position);
                adapter.notifyItemRemoved(position);
            }
            @Override
            public void always() {
                hideProgressBar();
            }
        });
    }
}