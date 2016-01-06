package net.azurewebsites.sportywarsaw.fragments;

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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.activities.SearchFriendsActivity;
import net.azurewebsites.sportywarsaw.adapters.FriendsRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.adapters.SportsFacilitiesRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.SportsFacilityModel;
import net.azurewebsites.sportywarsaw.models.UserModel;
import net.azurewebsites.sportywarsaw.services.UserService;
import net.azurewebsites.sportywarsaw.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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

        final Context context = view.getContext();
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
        //TODO: pagination
        showProgressBar();
        adapter.setOnLoadMoreListener(new SportsFacilitiesRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadNextPage(adapter);
            }
        });
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loadNextPage(adapter);

        loadFriends(adapter);
        return view;
    }


    private void loadNextPage(final FriendsRecyclerViewAdapter adapter) {
        if (allPagesLoaded) return;
        adapter.showProgressBar();
        Call<List<UserModel>> call = service.getPage(currentPage, PAGE_SIZE);
        call.enqueue(new CustomCallback<List<UserModel>>(getActivity()) {
            @Override
            public void onSuccess(List<UserModel> models) {
                adapter.hideProgressBar();
                if (models.size() < PAGE_SIZE) {
                    allPagesLoaded = true;
                }
                for (UserModel model : models) {
                    items.add(model);
                    adapter.notifyItemInserted(items.size());
                }
                adapter.setLoaded();
                if(currentPage == 1) {
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                currentPage++;
            }
        });
    }
    private void loadFriends(final FriendsRecyclerViewAdapter adapter) {
        adapter.showProgressBar();
        Call<List<UserModel>> call = service.getMyFriends();
        call.enqueue(new CustomCallback<List<UserModel>>(getActivity()) {
            @Override
            public void onSuccess(List<UserModel> models) {
                // w razie sukcesu dodawanie przyjaciół
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