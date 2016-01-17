package pl.sportywarsaw.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;
import pl.sportywarsaw.adapters.SportsFacilitiesRecyclerViewAdapter;
import pl.sportywarsaw.infrastructure.CustomCallback;
import pl.sportywarsaw.models.SportsFacilityModel;
import pl.sportywarsaw.services.SportsFacilitiesService;
import pl.sportywarsaw.utils.DividerItemDecoration;
import retrofit.Call;

/**
 * Fragment containing a list of sports facilities
 *
 * @author Marcin Chudy
 */
public class SportsFacilitiesFragment extends Fragment {
    private static final int PAGE_SIZE = 20;

    private List<SportsFacilityModel> list = new ArrayList<>();
    private int currentPage = 1;
    private boolean allPagesLoaded = false;

    @Inject SportsFacilitiesService service;
    private SportsFacilitiesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public SportsFacilitiesFragment() {
    }

    public static SportsFacilitiesFragment newInstance() {
        return new SportsFacilitiesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getServicesComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sports_facilities, container, false);

        Context context = view.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.sports_facilities_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        adapter = new SportsFacilitiesRecyclerViewAdapter(list, recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new SportsFacilitiesRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadNextPage(adapter);
            }
        });
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loadNextPage(adapter);
        return view;
    }

    private void loadNextPage(final SportsFacilitiesRecyclerViewAdapter adapter) {
        if (allPagesLoaded) return;
        adapter.showProgressBar();
        Call<List<SportsFacilityModel>> call = service.getPage(currentPage, PAGE_SIZE);
        call.enqueue(new CustomCallback<List<SportsFacilityModel>>(getActivity()) {
            @Override
            public void onSuccess(List<SportsFacilityModel> models) {
                adapter.hideProgressBar();
                if (models.size() < PAGE_SIZE) {
                    allPagesLoaded = true;
                }
                for (SportsFacilityModel model : models) {
                    list.add(model);
                    adapter.notifyItemInserted(list.size());
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
}
