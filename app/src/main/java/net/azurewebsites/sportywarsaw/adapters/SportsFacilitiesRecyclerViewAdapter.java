package net.azurewebsites.sportywarsaw.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.activities.SportsFacilityDetailsActivity;
import net.azurewebsites.sportywarsaw.models.SportsFacilityModel;

import java.util.List;

/**
 * Adapter for the sports facilities {@link RecyclerView}
 *
 * @author Marcin Chudy
 */
public class SportsFacilitiesRecyclerViewAdapter extends EndlessScrollBaseAdapter<SportsFacilityModel> {

    public SportsFacilitiesRecyclerViewAdapter(List<SportsFacilityModel> items, RecyclerView recyclerView) {
        super(items, recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.fragment_sports_facility_item, parent, false);
            viewHolder = new SportsFacilityViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  SportsFacilityViewHolder) {
            SportsFacilityViewHolder viewHolder = (SportsFacilityViewHolder) holder;
            final SportsFacilityModel item = items.get(position);
            viewHolder.item = items.get(position);
            viewHolder.titleView.setText(item.getDescription());
            viewHolder.addressView.setText(getAddressString(item));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SportsFacilityDetailsActivity.class);
                    intent.putExtra("sportsFacilityId", item.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private String getAddressString(SportsFacilityModel item) {
       return String.format("%s %s, %s", item.getStreet(), item.getNumber(), item.getDistrict());
    }

    public static class SportsFacilityViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleView;
        public final TextView addressView;
        public SportsFacilityModel item;

        public SportsFacilityViewHolder(View view) {
            super(view);
            this.view = view;
            titleView = (TextView) view.findViewById(R.id.sports_facility_title);
            addressView = (TextView) view.findViewById(R.id.sports_facility_address);
        }
    }
}
