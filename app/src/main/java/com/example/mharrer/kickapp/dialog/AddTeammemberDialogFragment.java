package com.example.mharrer.kickapp.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.fragments.TeamDetailsFragment;
import com.example.mharrer.kickapp.model.Participant;
import com.example.mharrer.kickapp.service.TeamServiceClient;

import java.util.List;

/**
 * Created by mharrer on 12.11.14.
 */
public class AddTeammemberDialogFragment extends android.support.v4.app.DialogFragment {

    private TeamDetailsFragment parent;

    public AddTeammemberDialogFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public AddTeammemberDialogFragment(final TeamDetailsFragment parent) {
        super();
        this.parent = parent;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_add_teammember);

        final List<Participant> participants = TeamServiceClient.getInstance().getFreeParticipants();

        ArrayAdapter adapter = new ArrayAdapter<Participant>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, participants);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Participant selectedParticipant = participants.get(which);
                parent.addNewTeammember(selectedParticipant);
            }
        });
        return builder.create();

    }



}
