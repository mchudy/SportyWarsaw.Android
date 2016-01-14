package pl.sportywarsaw.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import pl.sportywarsaw.R;
import pl.sportywarsaw.models.UserModel;
import pl.sportywarsaw.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

public class SearchFriendsAdapter extends ArrayAdapter<UserModel> {

    private static final int MAX_ITEMS = 25;
    private Context context;
    private int textViewResourceId;
    private UserService service;
    private List<UserModel> items;

    public SearchFriendsAdapter(Context context, int textViewResourceId, UserService service) {
        super(context, textViewResourceId);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.service = service;
        this.items = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public UserModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_user_item, parent, false);
        }
        UserModel item = items.get(position);
        if (item != null) {
            TextView userNameView = (TextView) view.findViewById(R.id.friend_username);
            TextView fullNameView = (TextView) view.findViewById(R.id.friend_full_name);
            if (userNameView != null)
                userNameView.setText(item.getUsername());
            if(fullNameView!=null)
            {
                fullNameView.setText(item.getFirstName() + " " + item.getLastName());
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((UserModel) resultValue).getUsername();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint != null) {
                Call<List<UserModel>> call = service.getPageFiltered(1, MAX_ITEMS, constraint.toString());
                try {
                    Response<List<UserModel>> response = call.execute();
                    if(response.isSuccess()){
                        items = response.body();
                    } else {
                        Log.e("Service", response.errorBody().string());
                    }
                } catch (IOException e) {
                    Log.e("Service", e.getMessage());
                }
                filterResults.values = items;
                filterResults.count = items.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results != null && results.count > 0) {
                notifyDataSetChanged();
            }
            else {
                notifyDataSetInvalidated();
            }
        }
    };
}