package net.azurewebsites.sportywarsaw.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.activities.MeetingDetailsActivity;
import net.azurewebsites.sportywarsaw.activities.SearchFriendsActivity;
import net.azurewebsites.sportywarsaw.models.UserModel;

import java.util.List;


public class FriendsRecyclerViewAdapter extends EndlessScrollBaseAdapter<UserModel> {
    private Fragment fragment;
    private Context context;

    public FriendsRecyclerViewAdapter(List<UserModel> items, RecyclerView recyclerView, Fragment fragment,Context context) {
        super(items, recyclerView);
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.fragment_friend_item, parent, false);
            viewHolder = new FriendViewHolder(view);
            ((FriendViewHolder) viewHolder).addFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // pokazanie nowego widoku do wyszukiwania przyjaciół
                    Intent intent = new Intent(context, SearchFriendsActivity.class);
                    //intent.putExtra("meetingId", item.getId());
                    context.startActivity(intent);
                }
            });
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
                    //TODO: show profile
                }
            });

            ((FriendViewHolder) holder).addFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // pokazanie nowego widoku do wyszukiwania przyjaciół
                    Intent intent = new Intent(context, SearchFriendsActivity.class);
                    //intent.putExtra("meetingId", item.getId());
                    context.startActivity(intent);
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
        public final Button addFriendButton;

        public FriendViewHolder(View view) {
            super(view);
            this.view = view;
            usernameView = (TextView) view.findViewById(R.id.friend_username);
            fullNameView = (TextView) view.findViewById(R.id.friend_full_name);
            removeFriendButton = (Button) view.findViewById(R.id.remove_friend_button);
            addFriendButton = (Button) view.findViewById(R.id.add_friendbutton);
        }
    }

    public interface RemoveFriendCallback {
        void removeFriend(String username, int position);
    }

}
