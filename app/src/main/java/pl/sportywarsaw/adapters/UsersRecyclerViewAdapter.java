package pl.sportywarsaw.adapters;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.sportywarsaw.R;
import pl.sportywarsaw.activities.UserProfileActivity;
import pl.sportywarsaw.models.UserModel;

import java.util.List;

public class UsersRecyclerViewAdapter extends EndlessScrollBaseAdapter<UserModel> {
    private Fragment fragment;

    public UsersRecyclerViewAdapter(List<UserModel> items, RecyclerView recyclerView, Fragment fragment) {
        super(items, recyclerView);
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.fragment_user_item, parent, false);
            viewHolder = new FriendViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof  FriendViewHolder) {
            FriendViewHolder viewHolder = (FriendViewHolder) holder;
            final UserModel item = items.get(position);
            viewHolder.item = items.get(position);
            viewHolder.usernameView.setText(item.getUsername());
            viewHolder.fullNameView.setText(item.getFirstName() + " " + item.getLastName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(fragment.getActivity(), UserProfileActivity.class);
                    intent.putExtra("username", item.getUsername());
                    fragment.getActivity().startActivity(intent);
                }
            });
        }
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView usernameView;
        public final TextView fullNameView;
        public UserModel item;

        public FriendViewHolder(View view) {
            super(view);
            this.view = view;
            usernameView = (TextView) view.findViewById(R.id.friend_username);
            fullNameView = (TextView) view.findViewById(R.id.friend_full_name);
        }
    }
}