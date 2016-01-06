package net.azurewebsites.sportywarsaw.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.models.CommentModel;

import java.util.List;

public class CommentsRecyclerViewAdapter extends EndlessScrollBaseAdapter<CommentModel> {

    public CommentsRecyclerViewAdapter(List<CommentModel> items, RecyclerView recyclerView) {
        super(items, recyclerView);
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
        public CommentModel item;

        public CommentViewHolder(View view) {
            super(view);
            this.view = view;
            authorView = (TextView) view.findViewById(R.id.author);
            textView =(TextView) view.findViewById(R.id.text);
        }
    }
}
