package pl.sportywarsaw.adapters;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import pl.sportywarsaw.R;
import pl.sportywarsaw.activities.UserProfileActivity;
import pl.sportywarsaw.models.UserModel;


public class FriendsRecyclerViewAdapter extends EndlessScrollBaseAdapter<UserModel> {
    private Fragment fragment;

    public FriendsRecyclerViewAdapter(List<UserModel> items, RecyclerView recyclerView, Fragment fragment) {
        super(items, recyclerView);
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.fragment_friend_item, parent, false);
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
                    intent.putExtra(UserProfileActivity.USERNAME_KEY, item.getUsername());
                    fragment.startActivity(intent);
                }
            });

            viewHolder.removeFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fragment instanceof RemoveFriendCallback) {
                        ((RemoveFriendCallback) fragment).removeFriend(item.getUsername(), position);
                    }
                }
            });
        }
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView usernameView;
        public final TextView fullNameView;
        public final Button removeFriendButton;
        public UserModel item;

        public FriendViewHolder(View view) {
            super(view);
            this.view = view;
            usernameView = (TextView) view.findViewById(R.id.friend_username);
            fullNameView = (TextView) view.findViewById(R.id.friend_full_name);
            removeFriendButton = (Button) view.findViewById(R.id.remove_friend_button);
        }
    }

    public interface RemoveFriendCallback {
        void removeFriend(String username, int position);
    }

}
