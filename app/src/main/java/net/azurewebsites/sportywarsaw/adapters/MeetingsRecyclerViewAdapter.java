package net.azurewebsites.sportywarsaw.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.activities.MeetingDetailsActivity;
import net.azurewebsites.sportywarsaw.models.MeetingModel;

import java.util.List;

/**
 * Adapter for the meetings {@link RecyclerView}
 *
 * @author Marcin Chudy
 */
public class MeetingsRecyclerViewAdapter extends RecyclerView.Adapter<MeetingsRecyclerViewAdapter.MeetingViewHolder> {

    private final List<MeetingModel> values;
    private Context context;

    public MeetingsRecyclerViewAdapter(List<MeetingModel> items,
                                       Context context) {
        values = items;
        this.context = context;
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meeting_item, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MeetingViewHolder holder, int position) {
        final MeetingModel item = values.get(position);
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
                Intent intent = new Intent(context, MeetingDetailsActivity.class);
                intent.putExtra("meetingId", item.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleView;
        public final TextView startDateView;
        public MeetingModel item;

        public MeetingViewHolder(View view) {
            super(view);
            this.view = view;
            titleView = (TextView) view.findViewById(R.id.meeting_title);
            startDateView = (TextView) view.findViewById(R.id.meeting_start_date);
        }
    }
}
