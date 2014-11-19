package com.example.mharrer.kickapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.activity.MainActivity;
import com.example.mharrer.kickapp.model.Team;
import com.example.mharrer.kickapp.service.TeamServiceClient;
import com.google.common.base.Optional;

import java.util.List;

public class TeamListFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private TeamDetailsFragment teamDetailsFragment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamListFragment() {
    }

    public void reloadTeamList() {
        final List<Team> teams = TeamServiceClient.getInstance().getAllTeams();

        mAdapter = new ArrayAdapter<Team>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, teams) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final Team team = teams.get(position);

                View view = convertView;
                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ((ImageView)view.findViewById(R.id.teamMembers_list_item_icon)).setBackgroundResource(R.drawable.symbolteam);

                ((TextView) view.findViewById(R.id.teamMembers_list_item_text)).setText(team.toString());

                ((TextView) view.findViewById(R.id.teamMembers_list_item_text)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).showTeamDetails(Optional.of(team));
                    }
                });

                view.findViewById(R.id.teamMembers_list_item_icon_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TeamServiceClient.getInstance().deleteTeam(team);
                        reloadTeamList();
                    }
                });

                return view;
            }
        };
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadTeamList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_team_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add_team) {
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.showTeamDetails(Optional.<Team>absent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_list, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        View viewById = view.findViewById(R.id.teamDetailsFrameLayout);
        if (viewById != null) {
            teamDetailsFragment = new TeamDetailsFragment();

            getChildFragmentManager().beginTransaction().replace(viewById.getId(), teamDetailsFragment, "teamDetailsFragment").commit();
            viewById.setVisibility(View.INVISIBLE);
        }
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity activity = (MainActivity)getActivity();
        activity.showTeamDetails(Optional.of((Team) parent.getAdapter().getItem(position)));

    }

    public TeamDetailsFragment getTeamDetailsFragment() {
        return teamDetailsFragment;
    }
}
