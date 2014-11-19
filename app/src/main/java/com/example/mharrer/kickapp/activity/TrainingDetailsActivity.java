package com.example.mharrer.kickapp.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.fragments.TrainingDetailsFragment;
import com.example.mharrer.kickapp.model.Coach;
import com.example.mharrer.kickapp.model.Team;
import com.example.mharrer.kickapp.model.Training;
import com.example.mharrer.kickapp.service.CoachServiceClient;
import com.example.mharrer.kickapp.service.TeamServiceClient;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class TrainingDetailsActivity extends FragmentActivity {

    private Training currentTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_training_details);

        Intent intent = getIntent();
        Object trainingObj = intent.getExtras().get("training");

        TextView nameField = (TextView) findViewById(R.id.editTrainingDetailsName);
        TextView dateField = (TextView) findViewById(R.id.editTrainingDetailsDate);
        TextView startTimeField = (TextView) findViewById(R.id.editTrainingDetailsStarttime);
        TextView endTimeField = (TextView) findViewById(R.id.editTrainingDetailsEndtime);
        TextView locationField = (TextView) findViewById(R.id.editTrainingDetailsLocation);

        // coach spinner
        Spinner coachField = (Spinner) findViewById(R.id.spinner_trainingDetails_coach);
        List<Coach> coaches = CoachServiceClient.getInstance().getCoaches();
        ArrayAdapter<Coach> dataAdapter = new ArrayAdapter<Coach>(this,
                android.R.layout.simple_spinner_item, coaches);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coachField.setAdapter(dataAdapter);

        // team spinner
        Spinner teamField = (Spinner) findViewById(R.id.spinner_trainingDetails_team);
        List<Team> teams = TeamServiceClient.getInstance().getAllTeams();
        ArrayAdapter<Team> teamsAdapter = new ArrayAdapter<Team>(this,
                android.R.layout.simple_spinner_item, teams);
        teamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamField.setAdapter(teamsAdapter);

        if (trainingObj != null) {
            final Training training = (Training) trainingObj;
            currentTraining = training;
            nameField.setText(training.getName());
            dateField.setText(training.getDate());
            startTimeField.setText(training.getStartTime());
            endTimeField.setText(training.getEndTime());
            locationField.setText(training.getLocation());

            if (training.getCoach() != null) {
                Optional<Coach> firstCoach = FluentIterable.from(coaches).filter(new Predicate<Coach>() {
                    @Override
                    public boolean apply(Coach input) {
                        if (input.getId().longValue() == training.getCoach().getId().longValue()) {
                            return true;
                        }
                        return false;
                    }
                }).first();
                if (firstCoach.isPresent()) {
                    coachField.setSelection(coaches.indexOf(firstCoach.get()));
                }
            }

            if (training.getTeam() != null) {
                Optional<Team> firstTeam = FluentIterable.from(teams).filter(new Predicate<Team>() {
                    @Override
                    public boolean apply(Team input) {
                        if (input.getId().longValue() == training.getTeam().getId().longValue()) {
                            return true;
                        }
                        return false;
                    }
                }).first();
                if (firstTeam.isPresent()) {
                    teamField.setSelection(teams.indexOf(firstTeam.get()));
                }
            }

        } else {
            nameField.setText(StringUtils.EMPTY);
            dateField.setText(StringUtils.EMPTY);
            startTimeField.setText(StringUtils.EMPTY);
            endTimeField.setText(StringUtils.EMPTY);
            locationField.setText(StringUtils.EMPTY);
            currentTraining = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentTraining != null) {
            ((TrainingDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.trainingDetails)).setCurrentTraining(currentTraining);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coach_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
