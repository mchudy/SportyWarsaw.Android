package net.azurewebsites.sportywarsaw.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.adapters.SportsFacilitiesRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.models.SportsFacilityModel;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;
import net.azurewebsites.sportywarsaw.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Fragment containing a list of sports facilities
 *
 * @author Marcin Chudy
 */
public class SportsFacilitiesFragment extends Fragment {
    List<SportsFacilityModel> list  = new ArrayList<>();

    @Inject SportsFacilitiesService service;

    public SportsFacilitiesFragment() {}

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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.sports_facilities_list);
        Context context = view.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        //TODO load a page from the webservice
        for (int i = 0; i < 15; i++) {
            list.add(getModel(i + 1));
        }

        final SportsFacilitiesRecyclerViewAdapter adapter = new SportsFacilitiesRecyclerViewAdapter(list, recyclerView);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new SportsFacilitiesRecyclerViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadItems(adapter);
            }
        });

        return view;
    }

    private void loadItems(final SportsFacilitiesRecyclerViewAdapter adapter) {
        adapter.showProgressBar();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.hideProgressBar();
                //TODO load a page from the webservice
                for (int i = 0; i < 5; i++) {
                    list.add(getModel(list.size() + i));
                    adapter.notifyItemInserted(list.size());
                }
                adapter.setLoaded();
            }
        }, 2000);
    }

    //TODO
    public SportsFacilityModel getModel(int i) {
        SportsFacilityModel model = new SportsFacilityModel();
        model.setDescription("Sports facility " + Integer.toString(i));
        model.setStreet("Marszałkowska");
        model.setNumber(Integer.toString(i));
        model.setDistrict("Sródmieście");
        return model;
    }
}
