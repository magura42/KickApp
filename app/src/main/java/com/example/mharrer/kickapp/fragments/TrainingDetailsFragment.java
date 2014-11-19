package com.example.mharrer.kickapp.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.activity.MainActivity;
import com.example.mharrer.kickapp.model.Coach;
import com.example.mharrer.kickapp.model.Team;
import com.example.mharrer.kickapp.model.Training;
import com.example.mharrer.kickapp.service.TrainingServiceClient;


public class TrainingDetailsFragment extends android.support.v4.app.Fragment {

    private Training currentTraining;


    public void setCurrentTraining(Training currentTraining) {
        this.currentTraining = currentTraining;
    }

    public TrainingDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View trainingDetailsView = inflater.inflate(R.layout.fragment_training_details, container, false);

        trainingDetailsView.findViewById(R.id.saveTrainingDetailsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((TextView) trainingDetailsView.findViewById(R.id.editTrainingDetailsName)).getText().toString();
                String date = ((TextView) trainingDetailsView.findViewById(R.id.editTrainingDetailsDate)).getText().toString();
                String startTime = ((TextView) trainingDetailsView.findViewById(R.id.editTrainingDetailsStarttime)).getText().toString();
                String endTime = ((TextView) trainingDetailsView.findViewById(R.id.editTrainingDetailsEndtime)).getText().toString();
                String location = ((TextView) trainingDetailsView.findViewById(R.id.editTrainingDetailsLocation)).getText().toString();
                Coach coach =  (Coach)((Spinner)trainingDetailsView.findViewById(R.id.spinner_trainingDetails_coach)).getSelectedItem();
                Team team =  (Team)((Spinner)trainingDetailsView.findViewById(R.id.spinner_trainingDetails_team)).getSelectedItem();

                if (currentTraining != null) {
                    currentTraining.setName(name);
                    currentTraining.setDate(date);
                    currentTraining.setStartTime(startTime);
                    currentTraining.setEndTime(endTime);
                    currentTraining.setLocation(location);
                    currentTraining.setTeam(team);
                    currentTraining.setCoach(coach);
                    TrainingServiceClient.getInstance().updateTraining(currentTraining);
                } else {
                    Training newTraining = new Training(date, name);
                    newTraining.setStartTime(startTime);
                    newTraining.setEndTime(endTime);
                    newTraining.setLocation(location);
                    newTraining.setTeam(team);
                    newTraining.setCoach(coach);
                    TrainingServiceClient.getInstance().createNewTraining(newTraining);
                    currentTraining = newTraining;
                }


                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.reloadTrainingList();
                    ((FrameLayout) trainingDetailsView.getParent().getParent()).setVisibility(View.INVISIBLE);
                } else {

                    getActivity().finish();
                }
            }
        });


        // datepicker for date:
        final TextView dateView = (TextView)trainingDetailsView.findViewById(R.id.editTrainingDetailsDate);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setView(dateView);
                android.support.v4.app.FragmentManager supportFragmentManager = ((FragmentActivity) dateView.getContext()).getSupportFragmentManager();
                newFragment.show(supportFragmentManager, "datePicker");
            }
        });

        // timepicker for starttime:
        final TextView startTimeView = (TextView)trainingDetailsView.findViewById(R.id.editTrainingDetailsStarttime);
        startTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setView(startTimeView);
                android.support.v4.app.FragmentManager supportFragmentManager = ((FragmentActivity) startTimeView.getContext()).getSupportFragmentManager();
                newFragment.show(supportFragmentManager, "startTimePicker");
            }
        });

        // timepicker for endtime:
        final TextView endTimeView = (TextView)trainingDetailsView.findViewById(R.id.editTrainingDetailsEndtime);
        endTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setView(endTimeView);
                android.support.v4.app.FragmentManager supportFragmentManager = ((FragmentActivity) endTimeView.getContext()).getSupportFragmentManager();
                newFragment.show(supportFragmentManager, "endTimePicker");
            }
        });


        return trainingDetailsView;

    }




}
