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
import net.azurewebsites.sportywarsaw.adapters.CommentsRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.adapters.SportsFacilitiesRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.CommentModel;
import net.azurewebsites.sportywarsaw.models.MeetingPlusModel;
import net.azurewebsites.sportywarsaw.models.UserModel;
import net.azurewebsites.sportywarsaw.services.CommentsService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;

public class MeetingDetailsFragment extends Fragment {
    private MeetingPlusModel model;
    @Inject CommentsService service;
    private CommentsRecyclerViewAdapter adapter;
    private List<CommentModel> comments = new ArrayList<>();

    public MeetingDetailsFragment() {
    }

    public static MeetingDetailsFragment newInstance(MeetingPlusModel model){
        MeetingDetailsFragment fragment = new MeetingDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("model", model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (MeetingPlusModel) getArguments().getSerializable("model");
        ((MyApplication) getActivity().getApplication()).getServicesComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_details, container, false);
        Context context = view.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        RecyclerView commentsView = (RecyclerView) view.findViewById(R.id.comments_view);
        commentsView.setLayoutManager(layoutManager);
        adapter = new CommentsRecyclerViewAdapter(comments, commentsView);
        commentsView.setAdapter(adapter);

        loadComments();
        return view;
    }

    private void loadComments() {
        adapter.showProgressBar();
        Call<List<CommentModel>> call = service.getAll(model.getId());
        call.enqueue(new CustomCallback<List<CommentModel>>(getActivity()) {
            @Override
            public void onSuccess(List<CommentModel> models) {
                adapter.hideProgressBar();
                for (CommentModel model : models) {
                    comments.add(model);
                    adapter.notifyItemInserted(comments.size());
                }
                adapter.setLoaded();
            }
        });
    }
}
