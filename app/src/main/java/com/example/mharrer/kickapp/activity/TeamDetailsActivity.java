package com.example.mharrer.kickapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.fragments.TeamDetailsFragment;
import com.example.mharrer.kickapp.model.Coach;
import com.example.mharrer.kickapp.model.Participant;
import com.example.mharrer.kickapp.model.Team;
import com.example.mharrer.kickapp.service.CoachServiceClient;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.List;


public class TeamDetailsActivity extends FragmentActivity {

    private Team currentTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_team_details);

        Intent intent = getIntent();
        Object teamObj = intent.getExtras().get("team");

        // coach spinner
        Spinner coachField = (Spinner) findViewById(R.id.spinner_teamDetails_coach);
        List<Coach> coaches = CoachServiceClient.getInstance().getCoaches();
        ArrayAdapter<Coach> dataAdapter = new ArrayAdapter<Coach>(this,
                android.R.layout.simple_spinner_item, coaches);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coachField.setAdapter(dataAdapter);

        if (teamObj != null) {
            final Team team = (Team) teamObj;
            currentTeam = team;
            TextView nameField = (TextView) findViewById(R.id.editTeamDetailsName);
            nameField.setText(team.getName());
            ListView teammembersView = (ListView) findViewById(R.id.teamDetailsTeammembersList);
            TeamDetailsFragment teamDetailsFragment = (TeamDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.teamDetails);

            if (team.getCoach() != null) {
                Optional<Coach> firstCoach = FluentIterable.from(coaches).filter(new Predicate<Coach>() {
                    @Override
                    public boolean apply(Coach input) {
                        if (input.getId().longValue() == team.getCoach().getId().longValue()) {
                            return true;
                        }
                        return false;
                    }
                }).first();
                if (firstCoach.isPresent()) {
                    coachField.setSelection(coaches.indexOf(firstCoach.get()));
                }
            }

            setupTeamDetailsList(team.getTeammembers(), teammembersView, getApplicationContext(), getLayoutInflater(), teamDetailsFragment);

        } else {
            currentTeam = null;
        }
    }

    public static void setupTeamDetailsList(final List<Participant> teammembers, ListView teammembersListView, Context context, final LayoutInflater inflater, final TeamDetailsFragment teamDetailsFragment) {

        ArrayAdapter adapter = new ArrayAdapter<Participant>(context,
                android.R.layout.simple_list_item_1, android.R.id.text1, teammembers) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Participant teammember = teammembers.get(position);
                View view = convertView;
                if (view == null) {
                    view = inflater.inflate(R.layout.list_item, null);
                }
                ((TextView) view.findViewById(R.id.teamMembers_list_item_text)).setText(teammember.toString());

                view.findViewById(R.id.teamMembers_list_item_icon_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        teamDetailsFragment.deleteTeammember(teammember);
                    }
                });

                return view;
            }
        };
        teammembersListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentTeam != null) {
            ((TeamDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.teamDetails)).setCurrentTeam(currentTeam);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_training, menu);
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
