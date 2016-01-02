package net.azurewebsites.sportywarsaw.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.models.MeetingModel;

import java.util.List;

public class MeetingsRecyclerViewAdapter extends RecyclerView.Adapter<MeetingsRecyclerViewAdapter.ViewHolder> {

    private final List<MeetingModel> values;
    private final MeetingsFragment.OnMeetingsListFragmentInteractionListener listener;
    private Context context;

    public MeetingsRecyclerViewAdapter(List<MeetingModel> items,
                                       MeetingsFragment.OnMeetingsListFragmentInteractionListener listener,
                                       Context context) {
        values = items;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meeting_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MeetingModel item = values.get(position);
        holder.item = values.get(position);
        holder.titleView.setText(item.getTitle());
        holder.startDateView.setText(DateUtils.getRelativeDateTimeString(
                context,
                item.getStartTime().getTime(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                0
        ));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleView;
        public final TextView startDateView;
        public MeetingModel item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleView = (TextView) view.findViewById(R.id.meeting_title);
            startDateView = (TextView) view.findViewById(R.id.meeting_start_date);
        }
    }
}
