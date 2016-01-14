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
import pl.sportywarsaw.models.SportsFacilityModel;
import pl.sportywarsaw.services.SportsFacilitiesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

public class SearchFacilityArrayAdapter extends ArrayAdapter<SportsFacilityModel> {

    private static final int MAX_ITEMS = 25;
    Context context;
    int textViewResourceId;
    private SportsFacilitiesService service;
    List<SportsFacilityModel> items;

    public SearchFacilityArrayAdapter(Context context, int textViewResourceId, SportsFacilitiesService service) {
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
    public SportsFacilityModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_sports_facility_item, parent, false);
        }
        SportsFacilityModel item = items.get(position);
        if (item != null) {
            TextView lblName = (TextView) view.findViewById(R.id.sports_facility_title);
            TextView addressView = (TextView) view.findViewById(R.id.sports_facility_address);
            if (lblName != null)
                lblName.setText(item.getDescription());
            addressView.setText(item.getStreet());
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
            String str = ((SportsFacilityModel) resultValue).getDescription();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint != null) {
                Call<List<SportsFacilityModel>> call = service.getPageFiltered(1, MAX_ITEMS, constraint.toString());
                try {
                    Response<List<SportsFacilityModel>> response = call.execute();
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