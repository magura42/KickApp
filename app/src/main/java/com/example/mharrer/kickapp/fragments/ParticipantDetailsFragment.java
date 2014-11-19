package com.example.mharrer.kickapp.fragments;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.activity.MainActivity;
import com.example.mharrer.kickapp.model.Participant;
import com.example.mharrer.kickapp.service.ParticipantServiceClient;


public class ParticipantDetailsFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private Participant currentParticipant;

    public void setCurrentParticipant(Participant currentParticipant) {
        this.currentParticipant = currentParticipant;
    }

    public ParticipantDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        EditText phoneNumber = (EditText) getView().findViewById(R.id.editParticipantDetailsPhonenumber);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + String.valueOf(phoneNumber.getText())));
        startActivity(callIntent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View participantDetailsView = inflater.inflate(R.layout.fragment_participant_details, container, false);

        participantDetailsView.findViewById(R.id.participantCallButton).setOnClickListener(this);

        participantDetailsView.findViewById(R.id.saveParticipantDetailsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = ((TextView) participantDetailsView.findViewById(R.id.editParticipantDetailsFirstname)).getText().toString();
                String lastName = ((TextView) participantDetailsView.findViewById(R.id.editParticipantDetailsLastname)).getText().toString();
                String phoneNumber = ((TextView) participantDetailsView.findViewById(R.id.editParticipantDetailsPhonenumber)).getText().toString();
                BitmapDrawable drawable = (BitmapDrawable) ((ImageView) participantDetailsView.findViewById(R.id.editParticipantDetailsPhoto)).getDrawable();

                if (currentParticipant != null) {
                    currentParticipant.setFirstName(firstName);
                    currentParticipant.setLastName(lastName);
                    currentParticipant.setPhoto(drawable.getBitmap());
                    currentParticipant.setPhonenumber(phoneNumber);
                    ParticipantServiceClient.getInstance().updateParticipant(currentParticipant);
                } else {
                    Participant newParticipant = new Participant(firstName, lastName, phoneNumber);
                    newParticipant.setPhoto(drawable.getBitmap());
                    ParticipantServiceClient.getInstance().createNewParticipant(newParticipant);
                    currentParticipant = newParticipant;
                }
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.reloadParticipantList();
                    ((FrameLayout) participantDetailsView.getParent().getParent()).setVisibility(View.INVISIBLE);
                } else {
                    getActivity().finish();
                }
            }
        });

        return participantDetailsView;

    }
}
