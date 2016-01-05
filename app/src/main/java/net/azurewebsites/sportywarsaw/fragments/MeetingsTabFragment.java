package net.azurewebsites.sportywarsaw.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.adapters.MeetingsRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.fragments.listeners.OnMeetingsListFragmentInteractionListener;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.MeetingModel;
import net.azurewebsites.sportywarsaw.services.MeetingsService;
import net.azurewebsites.sportywarsaw.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;

public class MeetingsTabFragment extends Fragment {

    private OnMeetingsListFragmentInteractionListener listener;

    @Inject MeetingsService service;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public MeetingsTabFragment() {}

    //TODO: take value indicating which meetings should be loaded
    public static MeetingsTabFragment newInstance() {
        return new MeetingsTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getServicesComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetings_tab, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.meetings_list);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        loadMeetings(recyclerView);

        return view;
    }

    //TODO
    private void loadMeetings(final RecyclerView recyclerView) {
        final List<MeetingModel> meetings = new ArrayList<>();

        Call<MeetingModel> call = service.getMeeting(1);
        call.enqueue(new CustomCallback<MeetingModel>(getActivity()) {
            @Override
            public void onSuccess(MeetingModel model) {
                meetings.add(model);
                Call<MeetingModel> call = service.getMeeting(2);
                call.enqueue(new CustomCallback<MeetingModel>(getActivity()) {
                    @Override
                    public void onSuccess(MeetingModel model) {
                        meetings.add(model);
                        recyclerView.setAdapter(new MeetingsRecyclerViewAdapter(
                                meetings, listener, getActivity()));
                    }
                });
            }
            @Override
            public void always() {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMeetingsListFragmentInteractionListener) {
            listener = (OnMeetingsListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMeetingsListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
