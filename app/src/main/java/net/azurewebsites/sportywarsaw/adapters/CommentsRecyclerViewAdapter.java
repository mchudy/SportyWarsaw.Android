package net.azurewebsites.sportywarsaw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.models.CommentModel;

import java.util.List;

public class CommentsRecyclerViewAdapter extends EndlessScrollBaseAdapter<CommentModel> {

    private Context context;

    public CommentsRecyclerViewAdapter(List<CommentModel> items, RecyclerView recyclerView, Context context) {
        super(items, recyclerView);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.comment_item, parent, false);
            viewHolder = new CommentViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  CommentViewHolder) {
            CommentViewHolder viewHolder = (CommentViewHolder) holder;
            final CommentModel item = items.get(position);
            viewHolder.item = item;
            viewHolder.authorView.setText(item.getUsername());
            viewHolder.textView.setText(item.getText());
            String dateString = DateUtils.getRelativeDateTimeString(
                    context,
                    item.getDate().getTime(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.WEEK_IN_MILLIS,
                    0
            ).toString();
            viewHolder.dateView.setText(dateString);
            viewHolder.authorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: show profile page
                }
            });
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView authorView;
        public final TextView textView;
        public final TextView dateView;
        public CommentModel item;

        public CommentViewHolder(View view) {
            super(view);
            this.view = view;
            authorView = (TextView) view.findViewById(R.id.author);
            textView =(TextView) view.findViewById(R.id.text);
            dateView = (TextView) view.findViewById(R.id.date);
        }
    }
}
