package com.example.mharrer.kickapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.activity.MainActivity;
import com.example.mharrer.kickapp.dialog.AddTeammemberDialogFragment;
import com.example.mharrer.kickapp.model.Coach;
import com.example.mharrer.kickapp.model.Participant;
import com.example.mharrer.kickapp.model.Team;
import com.example.mharrer.kickapp.service.TeamServiceClient;

import java.util.ArrayList;
import java.util.List;


public class TeamDetailsFragment extends android.support.v4.app.Fragment {

    private Team currentTeam;


    private List<Participant> newTeamMembers = new ArrayList<Participant>();
    private List<Participant> deletedTeamMembers = new ArrayList<Participant>();

    private View teamDetailsView;

    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }


    public TeamDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        teamDetailsView = inflater.inflate(R.layout.fragment_team_details, container, false);

        teamDetailsView.findViewById(R.id.saveTeamDetailsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTeamDetails();
            }


        });
        final TeamDetailsFragment thizz = this;

        teamDetailsView.findViewById(R.id.teamDetailAddTeammember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTeammemberDialogFragment dialog = new AddTeammemberDialogFragment(thizz);
                dialog.show(getFragmentManager(), "addTeammember");
            }
        });

        return teamDetailsView;
    }


    private void saveTeamDetails() {
        String name = ((TextView) teamDetailsView.findViewById(R.id.editTeamDetailsName)).getText().toString();
        Coach coach = (Coach) ((Spinner) teamDetailsView.findViewById(R.id.spinner_teamDetails_coach)).getSelectedItem();

        if (currentTeam != null) {
            currentTeam.setName(name);
            currentTeam.setCoach(coach);
            updateTeammembers(currentTeam.getTeammembers());
            TeamServiceClient.getInstance().updateTeam(currentTeam);
        } else {
            Team newTeam = new Team(name);
            newTeam.setCoach(coach);
            updateTeammembers(newTeam.getTeammembers());
            TeamServiceClient.getInstance().createNewTeam(newTeam);
            currentTeam = newTeam;
        }
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.reloadTeamList();
            ((FrameLayout) teamDetailsView.getParent().getParent()).setVisibility(View.INVISIBLE);
        } else {
            getActivity().finish();
        }
    }

    private void updateTeammembers(List<Participant> teammembers) {
        for (Participant newParticipant : newTeamMembers) {
            teammembers.add(newParticipant);
        }
        newTeamMembers.clear();
        for (Participant deletedParticipant : deletedTeamMembers) {
            teammembers.remove(deletedParticipant);
        }
        deletedTeamMembers.clear();
    }

    public void addNewTeammember(Participant newTeammember) {
        newTeamMembers.add(newTeammember);
        ListView listView = (ListView) teamDetailsView.findViewById(R.id.teamDetailsTeammembersList);
        ((ArrayAdapter) listView.getAdapter()).add(newTeammember);
    }

    public void deleteTeammember(Participant deletedTeammember) {
        deletedTeamMembers.add(deletedTeammember);
        ListView listView = (ListView) teamDetailsView.findViewById(R.id.teamDetailsTeammembersList);
        ((ArrayAdapter) listView.getAdapter()).remove(deletedTeammember);
    }
}
