package net.azurewebsites.sportywarsaw.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.adapters.CommentsRecyclerViewAdapter;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.AddCommentModel;
import net.azurewebsites.sportywarsaw.models.CommentModel;
import net.azurewebsites.sportywarsaw.models.MeetingPlusModel;
import net.azurewebsites.sportywarsaw.models.SportFacilityPlusModel;
import net.azurewebsites.sportywarsaw.services.CommentsService;
import net.azurewebsites.sportywarsaw.services.MeetingsService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;

/**
 * Created by Marcin Chudy on 07/01/2016.
 */
public class MeetingDetailsFragment extends Fragment {
    private MeetingPlusModel model;
    @Inject MeetingsService meetingsService;
    @Inject SharedPreferences preferences;
    private Button leaveButton;
    private Button joinButton;
    private int defaultColor;

    public MeetingDetailsFragment() {
    }

    public static MeetingDetailsFragment newInstance(MeetingPlusModel model){
        MeetingDetailsFragment fragment = new MeetingDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("model", model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (MeetingPlusModel) getArguments().getSerializable("model");
        ((MyApplication) getActivity().getApplication()).getServicesComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_details, container, false);
        updateDetails(view);

        leaveButton = (Button) view.findViewById(R.id.leave_button);
        joinButton = (Button) view.findViewById(R.id.join_button);
        defaultColor = leaveButton.getCurrentTextColor();
        setButtons();
        return view;
    }

    private void setButtons() {
        final String username = preferences.getString("username", "");
        if(username.equals(model.getOrganizerName())){
            return; // organizer can never leave the meeting
        }
        Call<Boolean> call = meetingsService.isParticipant(username, model.getId());
        call.enqueue(new CustomCallback<Boolean>(getActivity()) {
            @Override
            public void onSuccess(Boolean result) {
                if(result == true) {
                    leaveButton.setEnabled(true);
                    leaveButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                } else {
                    joinButton.setEnabled(true);
                    joinButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }
            }
        });
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveMeeting();
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinMeeting();
            }
        });
    }

    private void joinMeeting() {
        Call<ResponseBody> call = meetingsService.joinMeeting(model.getId());
        call.enqueue(new CustomCallback<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(ResponseBody model) {
                Toast.makeText(getActivity(), R.string.message_joined_meeting, Toast.LENGTH_SHORT).show();
                leaveButton.setEnabled(true);
                leaveButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                joinButton.setEnabled(false);
                joinButton.setTextColor(defaultColor);
            }
        });
    }

    private void leaveMeeting() {
        Call<ResponseBody> call = meetingsService.leaveMeeting(model.getId());
        call.enqueue(new CustomCallback<ResponseBody>(getActivity()) {
            @Override
            public void onSuccess(ResponseBody model) {
                Toast.makeText(getActivity(), R.string.message_left_meeting, Toast.LENGTH_SHORT).show();
                joinButton.setEnabled(true);
                joinButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                leaveButton.setEnabled(false);
                leaveButton.setTextColor(defaultColor);
            }
        });
    }

    private void updateDetails(View view) {
        SportFacilityPlusModel facility = model.getSportsFacility();
        final String addressString = facility.getStreet() + " " + facility.getNumber() + ", " + facility.getDistrict();

        TextView addressView = (TextView) view.findViewById(R.id.sports_facility_address);
        addressView.setText(addressString);

        TextView sportTypeView = (TextView) view.findViewById(R.id.sport_type);
        sportTypeView.setText(model.getSportType().toString());

        TextView costView = (TextView) view.findViewById(R.id.cost);
        NumberFormat baseFormat = NumberFormat.getCurrencyInstance();
        String moneyString = baseFormat.format(model.getCost());
        if(model.getCost() == 0.0) {
            costView.setText(R.string.free);
        } else {
            costView.setText(moneyString);
        }

        TextView description = (TextView) view.findViewById(R.id.sports_facility_description);
        description.setText(facility.getDescription());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        TextView startDateTime = (TextView) view.findViewById(R.id.start_date_time);
        startDateTime.setText(df.format(model.getStartTime()));
        TextView endDateTime = (TextView) view.findViewById(R.id.end_date_time);
        endDateTime.setText(df.format(model.getEndTime()));

        TextView organizer = (TextView) view.findViewById(R.id.organizer);
        organizer.setText(model.getOrganizerName());

        ImageView mapView = (ImageView) view.findViewById(R.id.map_image);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String query = URLEncoder.encode(addressString, "utf-8");
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    startActivity(mapIntent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
