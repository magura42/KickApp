package com.example.mharrer.kickapp.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.SlidingTabLayout;
import com.example.mharrer.kickapp.fragments.CoachDetailsFragment;
import com.example.mharrer.kickapp.fragments.CoachesListFragment;
import com.example.mharrer.kickapp.fragments.ParticipantDetailsFragment;
import com.example.mharrer.kickapp.fragments.ParticipantListFragment;
import com.example.mharrer.kickapp.fragments.TeamDetailsFragment;
import com.example.mharrer.kickapp.fragments.TeamListFragment;
import com.example.mharrer.kickapp.fragments.TrainingDetailsFragment;
import com.example.mharrer.kickapp.fragments.TrainingListFragment;
import com.example.mharrer.kickapp.model.Coach;
import com.example.mharrer.kickapp.model.Participant;
import com.example.mharrer.kickapp.model.Team;
import com.example.mharrer.kickapp.model.Training;
import com.example.mharrer.kickapp.service.CoachServiceClient;
import com.example.mharrer.kickapp.service.TeamServiceClient;
import com.example.mharrer.kickapp.settings.SettingsActivity;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.mharrer.kickapp.activity.TeamDetailsActivity.setupTeamDetailsList;


public class MainActivity extends FragmentActivity {

    private ViewPager pager;

    private final List<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();

    private void changeBackgroundImage(boolean adultMode) {
        Drawable background = null;
        if (adultMode) {
            background = getResources().getDrawable(R.drawable.team);
        } else {
            background = getResources().getDrawable(R.drawable.hintergrundrasen);
        }
        View view = this.findViewById(R.id.mainLayout);
        view.setBackground(background);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_main);

        fragments.add(new TrainingListFragment());
        fragments.add(new CoachesListFragment());
        fragments.add(new ParticipantListFragment());
        fragments.add(new TeamListFragment());

        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int i) {
                return fragments.get(i);
            }


            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Training";
                    case 1:
                        return "Trainer";
                    case 2:
                        return "Teilnehmer";
                    case 3:
                        return "Team";
                    default:
                        return "";
                }
            }
        });

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(pager);

        // setup action bar for tabs
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        // setup backgroundimage
        setupBackgroundimage();

        if (savedInstanceState != null) {
            pager.setCurrentItem(savedInstanceState.getInt("tab", 0));
        }

    }

    private void setupBackgroundimage() {

        changeBackgroundImage(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("settings_adult_mode", false));

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if ("settings_adult_mode".equals(key)) {
                    changeBackgroundImage(sharedPreferences.getBoolean(key, false));
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", pager.getCurrentItem());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void showCoachDetails(Optional<Coach> coach) {

        android.support.v4.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
        CoachesListFragment coachesListFragment = (CoachesListFragment) fragments.get(1);

        CoachDetailsFragment coachDetailsFragment = coachesListFragment.getCoachDetailsFragment();


        if (isTablet()) {

            TextView firstnameField = (TextView) coachDetailsFragment.getView().findViewById(R.id.editCoachDetailsFirstname);
            TextView lastnameField = (TextView) coachDetailsFragment.getView().findViewById(R.id.editCoachDetailsLastname);
            TextView phonenumberField = (TextView) coachDetailsFragment.getView().findViewById(R.id.editCoachDetailsPhonenumber);
            TextView emailField = (TextView) coachDetailsFragment.getView().findViewById(R.id.editCoachDetailsEmail);
            ImageView imageView = (ImageView) coachDetailsFragment.getView().findViewById(R.id.editCoachDetailsPhoto);

            if (coach.isPresent()) {
                firstnameField.setText(coach.get().getFirstName());
                lastnameField.setText(coach.get().getLastName());
                phonenumberField.setText(coach.get().getPhonenumber());
                coachDetailsFragment.setCurrentCoach(coach.get());
                emailField.setText(coach.get().getEmail());
                Bitmap photo = coach.get().getPhoto();
                if (photo != null) {
                    imageView.setImageBitmap(photo);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.placeholder);
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                firstnameField.setText(StringUtils.EMPTY);
                lastnameField.setText(StringUtils.EMPTY);
                phonenumberField.setText(StringUtils.EMPTY);
                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.placeholder);
                imageView.setImageBitmap(bitmap);
            }

            ((FrameLayout) coachDetailsFragment.getView().getParent()).setVisibility(View.VISIBLE);
            coachDetailsFragment.setCurrentCoach(coach.isPresent() ? coach.get() : null);

        } else { // open activity
            Intent intent = new Intent(this, CoachDetailsActivity.class);
            if (coach.isPresent()) {
                CoachDetailsActivity.currentCoachPhoto = coach.get().getPhoto();
                coach.get().setPhoto(null);
            }
            intent.putExtra("coach", (coach.isPresent() ? coach.get() : null));
            startActivity(intent);
        }

    }

    public void showTeamDetails(final Optional<Team> team) {

        TeamListFragment teamListFragment = (TeamListFragment) fragments.get(3);

        TeamDetailsFragment teamDetailsFragment = teamListFragment.getTeamDetailsFragment();

        if (isTablet()) {
            TextView nameField = (TextView) teamDetailsFragment.getView().findViewById(R.id.editTeamDetailsName);
            ListView teammembersView = (ListView) teamDetailsFragment.getView().findViewById(R.id.teamDetailsTeammembersList);

            // coach spinner
            Spinner coachField = (Spinner) teamDetailsFragment.getView().findViewById(R.id.spinner_teamDetails_coach);
            List<Coach> coaches = CoachServiceClient.getInstance().getCoaches();
            ArrayAdapter<Coach> dataAdapter = new ArrayAdapter<Coach>(this,
                    android.R.layout.simple_spinner_item, coaches);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            coachField.setAdapter(dataAdapter);

            if (team.isPresent()) {
                // name
                nameField.setText(team.get().getName());

                // coach
                if (team.get().getCoach() != null) {
                    Optional<Coach> firstCoach = FluentIterable.from(coaches).filter(new Predicate<Coach>() {
                        @Override
                        public boolean apply(Coach input) {
                            if (input.getId().longValue() == team.get().getCoach().getId().longValue()) {
                                return true;
                            }
                            return false;
                        }
                    }).first();
                    if (firstCoach.isPresent()) {
                        coachField.setSelection(coaches.indexOf(firstCoach.get()));
                    }
                }

                // teammembers
                setupTeamDetailsList(team.get().getTeammembers(), teammembersView, getApplicationContext(), getLayoutInflater(), teamDetailsFragment);
            } else {
                setupTeamDetailsList(Collections.EMPTY_LIST, teammembersView, getApplicationContext(), getLayoutInflater(), teamDetailsFragment);
                nameField.setText(StringUtils.EMPTY);
            }

            ((FrameLayout) teamDetailsFragment.getView().getParent()).setVisibility(View.VISIBLE);

            teamDetailsFragment.setCurrentTeam(team.isPresent() ? team.get() : null);
        } else { // open activity
            Intent intent = new Intent(this, TeamDetailsActivity.class);
            intent.putExtra("team", team.isPresent() ? team.get() : null);
            startActivity(intent);
        }

    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void showTrainingDetails(final Optional<Training> training) {

        TrainingListFragment trainingListFragment = (TrainingListFragment) fragments.get(0);

        TrainingDetailsFragment trainingDetailsFragment = trainingListFragment.getTrainingDetailsFragment();

        if (isTablet()) {

            TextView nameField = (TextView) trainingDetailsFragment.getView().findViewById(R.id.editTrainingDetailsName);
            TextView dateField = (TextView) trainingDetailsFragment.getView().findViewById(R.id.editTrainingDetailsDate);
            TextView startTimeField = (TextView) trainingDetailsFragment.getView().findViewById(R.id.editTrainingDetailsStarttime);
            TextView endTimeField = (TextView) trainingDetailsFragment.getView().findViewById(R.id.editTrainingDetailsEndtime);
            TextView locationField = (TextView) trainingDetailsFragment.getView().findViewById(R.id.editTrainingDetailsLocation);

            // coach spinner
            Spinner coachField = (Spinner) trainingDetailsFragment.getView().findViewById(R.id.spinner_trainingDetails_coach);
            List<Coach> coaches = CoachServiceClient.getInstance().getCoaches();
            ArrayAdapter<Coach> dataAdapter = new ArrayAdapter<Coach>(this,
                    android.R.layout.simple_spinner_item, coaches);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            coachField.setAdapter(dataAdapter);

            // team spinner
            Spinner teamField = (Spinner) trainingDetailsFragment.getView().findViewById(R.id.spinner_trainingDetails_team);
            List<Team> teams = TeamServiceClient.getInstance().getAllTeams();
            ArrayAdapter<Team> teamsAdapter = new ArrayAdapter<Team>(this,
                    android.R.layout.simple_spinner_item, teams);
            teamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            teamField.setAdapter(teamsAdapter);

            if (training.isPresent()) {
                nameField.setText(training.get().getName());
                dateField.setText(training.get().getDate());
                endTimeField.setText(training.get().getEndTime());
                startTimeField.setText(training.get().getStartTime());
                locationField.setText(training.get().getLocation());

                if (training.get().getCoach() != null) {
                    Optional<Coach> firstCoach = FluentIterable.from(coaches).filter(new Predicate<Coach>() {
                        @Override
                        public boolean apply(Coach input) {
                            if (input.getId().longValue() == training.get().getCoach().getId().longValue()) {
                                return true;
                            }
                            return false;
                        }
                    }).first();
                    if (firstCoach.isPresent()) {
                        coachField.setSelection(coaches.indexOf(firstCoach.get()));
                    }
                }

                if (training.get().getTeam() != null) {
                    Optional<Team> firstTeam = FluentIterable.from(teams).filter(new Predicate<Team>() {
                        @Override
                        public boolean apply(Team input) {
                            if (input.getId().longValue() == training.get().getTeam().getId().longValue()) {
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
                endTimeField.setText(StringUtils.EMPTY);
                startTimeField.setText(StringUtils.EMPTY);
                locationField.setText(StringUtils.EMPTY);
            }

            ((FrameLayout) trainingDetailsFragment.getView().getParent()).setVisibility(View.VISIBLE);

            trainingDetailsFragment.setCurrentTraining(training.isPresent() ? training.get() : null);
        } else { // open activity
            Intent intent = new Intent(this, TrainingDetailsActivity.class);
            intent.putExtra("training", training.isPresent() ? training.get() : null);
            startActivity(intent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent prefs = new Intent(this, SettingsActivity.class);
            startActivity(prefs);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ActionBar.Tab selectedTab = getActionBar().getSelectedTab();
            int selectedItem = pager.getCurrentItem();

            if (selectedItem == 2) {
                ParticipantListFragment participantListFragment = (ParticipantListFragment) fragments.get(2);
                ParticipantDetailsFragment participantDetailsFragment = participantListFragment.getParticipantDetailsFragment();
                ImageView imageView = (ImageView) participantDetailsFragment.getView().findViewById(R.id.editParticipantDetailsPhoto);
                imageView.setImageBitmap(imageBitmap);
            } else if (selectedItem == 1) {
                CoachesListFragment coachesDetailsFragment = (CoachesListFragment) fragments.get(1);
                CoachDetailsFragment coachDetailsFragment = coachesDetailsFragment.getCoachDetailsFragment();
                ImageView imageView = (ImageView) coachDetailsFragment.getView().findViewById(R.id.editCoachDetailsPhoto);
                imageView.setImageBitmap(imageBitmap);
            }

        }
    }

    public void showParticipantDetails(Optional<Participant> participant) {

        ParticipantListFragment participantListFragment = (ParticipantListFragment) fragments.get(2);


        ParticipantDetailsFragment participantDetailsFragment = participantListFragment.getParticipantDetailsFragment();

        if (isTablet()) {
            TextView firstnameField = (TextView) participantDetailsFragment.getView().findViewById(R.id.editParticipantDetailsFirstname);
            TextView lastnameField = (TextView) participantDetailsFragment.getView().findViewById(R.id.editParticipantDetailsLastname);
            ImageView imageView = (ImageView) participantDetailsFragment.getView().findViewById(R.id.editParticipantDetailsPhoto);

            if (participant.isPresent()) {
                firstnameField.setText(participant.get().getFirstName());
                lastnameField.setText(participant.get().getLastName());
                Bitmap photo = participant.get().getPhoto();
                if (photo != null) {
                    imageView.setImageBitmap(photo);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.placeholder);
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                firstnameField.setText(StringUtils.EMPTY);
                lastnameField.setText(StringUtils.EMPTY);
                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.placeholder);
                imageView.setImageBitmap(bitmap);
            }


            ((FrameLayout) participantDetailsFragment.getView().getParent()).setVisibility(View.VISIBLE);
            participantDetailsFragment.setCurrentParticipant(participant.isPresent() ? participant.get() : null);
        } else { // open activity
            Intent intent = new Intent(this, ParticipantDetailsActivity.class);
            if (participant.isPresent()) { // intent can be too large!!!
                ParticipantDetailsActivity.currentParticipantPhoto = participant.get().getPhoto();
                participant.get().setPhoto(null);
            }
            intent.putExtra("participant", participant.isPresent() ? participant.get() : null);
            startActivity(intent);
        }
    }


    public void reloadTrainingList() {
        TrainingListFragment trainingListFragment = (TrainingListFragment) fragments.get(0);
        trainingListFragment.reloadTrainingList();
    }

    public void reloadCoachesList() {
        CoachesListFragment coachesListFragment = (CoachesListFragment) fragments.get(1);
        coachesListFragment.reloadCoachesList();
    }

    public void reloadParticipantList() {
        ParticipantListFragment participantListFragment = (ParticipantListFragment) fragments.get(2);
        participantListFragment.reloadParticipantList();
    }

    public void reloadTeamList() {
        TeamListFragment teamListFragment = (TeamListFragment) fragments.get(3);
        teamListFragment.reloadTeamList();
    }
}
