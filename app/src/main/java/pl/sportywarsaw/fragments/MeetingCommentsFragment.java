package pl.sportywarsaw.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;
import pl.sportywarsaw.adapters.CommentsRecyclerViewAdapter;
import pl.sportywarsaw.infrastructure.CustomCallback;
import pl.sportywarsaw.models.AddCommentModel;
import pl.sportywarsaw.models.CommentModel;
import pl.sportywarsaw.models.MeetingPlusModel;
import pl.sportywarsaw.services.CommentsService;
import retrofit.Call;

/**
 * Created by Marcin Chudy on 07/01/2016.
 */
public class MeetingCommentsFragment extends Fragment {
    private MeetingPlusModel model;
    @Inject CommentsService service;
    private CommentsRecyclerViewAdapter adapter;
    private List<CommentModel> comments = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView commentsView;
    private EditText newCommentText;

    public MeetingCommentsFragment() {
    }

    public static MeetingCommentsFragment newInstance(MeetingPlusModel model){
        MeetingCommentsFragment fragment = new MeetingCommentsFragment();
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
        View view = inflater.inflate(R.layout.fragment_meeting_comments, container, false);

        org.solovyev.android.views.llm.LinearLayoutManager layoutManager =
                new org.solovyev.android.views.llm.LinearLayoutManager(getActivity());
        commentsView = (RecyclerView) view.findViewById(R.id.comments_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        commentsView.setLayoutManager(layoutManager);
        adapter = new CommentsRecyclerViewAdapter(comments, commentsView, getActivity());
        commentsView.setAdapter(adapter);

        final Button addCommentButton = (Button) view.findViewById(R.id.add_comment_button);
        newCommentText = (EditText) view.findViewById(R.id.new_comment_text);
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
                newCommentText.setText(null);
            }
        });
    }
}
