package net.azurewebsites.sportywarsaw.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.adapters.CommentsRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.adapters.SportsFacilitiesRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.AddCommentModel;
import net.azurewebsites.sportywarsaw.models.CommentModel;
import net.azurewebsites.sportywarsaw.models.MeetingPlusModel;
import net.azurewebsites.sportywarsaw.models.SportFacilityPlusModel;
import net.azurewebsites.sportywarsaw.models.SportsFacilityModel;
import net.azurewebsites.sportywarsaw.models.UserModel;
import net.azurewebsites.sportywarsaw.services.CommentsService;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;

public class MeetingDetailsFragment extends Fragment {
    private MeetingPlusModel model;
    @Inject CommentsService service;
    private CommentsRecyclerViewAdapter adapter;
    private List<CommentModel> comments = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView commentsView;

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
        updateDetails(view);

        Context context = view.getContext();
        org.solovyev.android.views.llm.LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        commentsView = (RecyclerView) view.findViewById(R.id.comments_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        commentsView.setLayoutManager(layoutManager);
        adapter = new CommentsRecyclerViewAdapter(comments, commentsView, getActivity());
        commentsView.setAdapter(adapter);

        final Button addCommentButton = (Button) view.findViewById(R.id.add_comment_button);
        final EditText newCommentText = (EditText) view.findViewById(R.id.new_comment_text);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newCommentText.getText().toString();
                if (!text.isEmpty()) {
                    addComment(text);
                }
            }
        });

        loadComments();

        return view;
    }

    private void updateDetails(View view) {
        SportFacilityPlusModel facility = model.getSportsFacility();
        String addressString = facility.getStreet() + " " + facility.getNumber() + ", " + facility.getDistrict();

        TextView addressView = (TextView) view.findViewById(R.id.sports_facility_address);
        addressView.setText(addressString);

        TextView sportTypeView = (TextView) view.findViewById(R.id.sport_type);
        sportTypeView.setText(model.getSportType().toString());

        TextView costView = (TextView) view.findViewById(R.id.cost);
        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        String moneyString = baseFormat.format(model.getCost());
        if(model.getCost() == 0.0) {
            costView.setText(R.string.free);
        }

        TextView description = (TextView) view.findViewById(R.id.sports_facility_description);
        description.setText(facility.getDescription());

    }

    private void addComment(String text) {
        AddCommentModel newComment = new AddCommentModel();
        newComment.setMeetingId(model.getId());
        newComment.setText(text);
        Call<ResponseBody> call = service.post(newComment);
        call.enqueue(new CustomCallback<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(ResponseBody model) {
                comments.clear();
                adapter.notifyDataSetChanged();
                loadComments();
            }
        });
    }

    private void loadComments() {
        commentsView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
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
            @Override
            public void always() {
                commentsView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
