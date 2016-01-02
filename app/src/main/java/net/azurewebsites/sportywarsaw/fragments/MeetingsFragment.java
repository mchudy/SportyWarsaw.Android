package net.azurewebsites.sportywarsaw.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.MeetingModel;
import net.azurewebsites.sportywarsaw.services.MeetingsService;

import java.util.Collections;

import javax.inject.Inject;

import retrofit.Call;

public class MeetingsFragment extends Fragment {

    private OnMeetingsListFragmentInteractionListener listener;

    @Inject MeetingsService service;

    public MeetingsFragment() {
    }

    public static MeetingsFragment newInstance() {
        return new MeetingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getServicesComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.meetings_list);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        loadMeetings(recyclerView);

        return view;
    }

    //TODO
    private void loadMeetings(final RecyclerView recyclerView) {
        Call<MeetingModel> call = service.getMeeting(1);
        call.enqueue(new CustomCallback<MeetingModel>(getActivity()) {
            @Override
            public void onSuccess(MeetingModel model) {
                recyclerView.setAdapter(new MeetingsRecyclerViewAdapter(Collections.singletonList(model), listener));
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

    public interface OnMeetingsListFragmentInteractionListener {
        void onListFragmentInteraction(MeetingModel item);
    }
}
