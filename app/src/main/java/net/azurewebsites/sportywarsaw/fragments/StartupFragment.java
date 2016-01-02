package net.azurewebsites.sportywarsaw.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;
import net.azurewebsites.sportywarsaw.infrastructure.CustomCallback;
import net.azurewebsites.sportywarsaw.models.SportsFacilityModel;
import net.azurewebsites.sportywarsaw.services.SportsFacilitiesService;

import javax.inject.Inject;

import retrofit.Call;


public class StartupFragment extends Fragment {

    @Inject SportsFacilitiesService service;

    public StartupFragment() {}

    public static StartupFragment newInstance() {
        return new StartupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getServicesComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_startup, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<SportsFacilityModel> call = service.getSportsFacility(1);
                call.enqueue(new CustomCallback<SportsFacilityModel>(getActivity()) {
                    @Override
                    public void onSuccess(SportsFacilityModel model) {
                        Toast.makeText(getActivity(), model.getDescription(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }

}
