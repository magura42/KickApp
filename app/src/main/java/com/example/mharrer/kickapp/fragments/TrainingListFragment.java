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

import com.example.mharrer.kickapp.model.Training;
import com.example.mharrer.kickapp.service.TrainingServiceClient;
import com.google.common.base.Optional;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * interface.
 */
public class TrainingListFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private TrainingDetailsFragment trainingDetailsFragment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrainingListFragment() {

    }

    public void reloadTrainingList() {
        final List<Training> trainings = TrainingServiceClient.getInstance().getAllTrainings();

        mAdapter = new ArrayAdapter<Training>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, trainings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final Training training = trainings.get(position);

                View view = convertView;
                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ((ImageView)view.findViewById(R.id.teamMembers_list_item_icon)).setBackgroundResource(R.drawable.football_ground);

                ((TextView) view.findViewById(R.id.teamMembers_list_item_text)).setText(training.toString());

                ((TextView) view.findViewById(R.id.teamMembers_list_item_text)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).showTrainingDetails(Optional.of(training));
                    }
                });

                view.findViewById(R.id.teamMembers_list_item_icon_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TrainingServiceClient.getInstance().deleteTraining(training);
                        reloadTrainingList();
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
        reloadTrainingList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_training_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add_training) {
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.showTrainingDetails(Optional.<Training>absent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_training_list, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        View viewById = view.findViewById(R.id.trainingDetailsFrameLayout);
        if (viewById != null) {
            trainingDetailsFragment = new TrainingDetailsFragment();

            getChildFragmentManager().beginTransaction().replace(viewById.getId(), trainingDetailsFragment, "trainingDetailsFragment").commit();
            viewById.setVisibility(View.INVISIBLE);
        }
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity activity = (MainActivity)getActivity();
        activity.showTrainingDetails(Optional.of((Training) parent.getAdapter().getItem(position)));

    }

    public TrainingDetailsFragment getTrainingDetailsFragment() {
        return trainingDetailsFragment;
    }
}
