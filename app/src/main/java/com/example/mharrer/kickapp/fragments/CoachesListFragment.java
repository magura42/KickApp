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

import com.example.mharrer.kickapp.activity.MainActivity;
import com.example.mharrer.kickapp.R;

import com.example.mharrer.kickapp.model.Coach;
import com.example.mharrer.kickapp.service.CoachServiceClient;
import com.google.common.base.Optional;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class CoachesListFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{


    private CoachDetailsFragment coachDetailsFragment;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CoachesListFragment() {
    }

    public void reloadCoachesList() {
        final List<Coach> coaches = CoachServiceClient.getInstance().getCoaches();

        mAdapter = new ArrayAdapter<Coach>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, coaches) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final Coach coach = coaches.get(position);

                View view = convertView;
                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ((ImageView)view.findViewById(R.id.teamMembers_list_item_icon)).setBackgroundResource(R.drawable.teacher);

                ((TextView) view.findViewById(R.id.teamMembers_list_item_text)).setText(coach.toString());

                ((TextView) view.findViewById(R.id.teamMembers_list_item_text)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).showCoachDetails(Optional.of(coach));
                    }
                });

                view.findViewById(R.id.teamMembers_list_item_icon_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CoachServiceClient.getInstance().deleteCoach(coach);
                        reloadCoachesList();
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
        reloadCoachesList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_coach_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add_coach) {
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.showCoachDetails(Optional.<Coach>absent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coaches_list, container, false);


        mListView = (AbsListView) view.findViewById(android.R.id.list);


        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        final View viewById = view.findViewById(R.id.coachDetailsFrameLayout);
        if (viewById != null) {
            coachDetailsFragment = new CoachDetailsFragment();
            getChildFragmentManager().beginTransaction().replace(viewById.getId(), coachDetailsFragment, "coachDetailsFragment").commit();
            viewById.setVisibility(View.INVISIBLE);

        }

        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity activity = (MainActivity)getActivity();
        activity.showCoachDetails(Optional.of((Coach)parent.getAdapter().getItem(position)));
    }

    public CoachDetailsFragment getCoachDetailsFragment() {
        return coachDetailsFragment;
    }
}
