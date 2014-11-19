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
import com.example.mharrer.kickapp.dialog.AddContactDialogFragment;
import com.example.mharrer.kickapp.model.Coach;
import com.example.mharrer.kickapp.service.CoachServiceClient;

import org.apache.commons.lang3.StringUtils;


public class CoachDetailsFragment extends android.support.v4.app.Fragment {

    private Coach currentCoach;

    private View coachDetailsView;

    public CoachDetailsFragment() {
        // Required empty public constructor
    }

    public void setCurrentCoach(Coach currentCoach) {
        this.currentCoach = currentCoach;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        coachDetailsView = inflater.inflate(R.layout.fragment_coach_details, container, false);

        // email button
        coachDetailsView.findViewById(R.id.coachDetailsEmailButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) getView().findViewById(R.id.editCoachDetailsEmail);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email.getText().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail über KickApp");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        // call button
        coachDetailsView.findViewById(R.id.coachDetailsCallButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText phoneNumber = (EditText) getView().findViewById(R.id.editCoachDetailsPhonenumber);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + String.valueOf(phoneNumber.getText())));
                startActivity(callIntent);
            }
        });

        final CoachDetailsFragment thizz = this;

        // button to add contact as coach:
        View userContactsButtonView = coachDetailsView.findViewById(R.id.coachDetailUseConctacts);
        userContactsButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactDialogFragment dialog = new AddContactDialogFragment(thizz);
                dialog.show(getFragmentManager(), "addTeammember");
            }
        });


        coachDetailsView.findViewById(R.id.saveCoachDetailsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = ((TextView) coachDetailsView.findViewById(R.id.editCoachDetailsFirstname)).getText().toString();
                String lastName = ((TextView) coachDetailsView.findViewById(R.id.editCoachDetailsLastname)).getText().toString();
                String phonenumber = ((TextView) coachDetailsView.findViewById(R.id.editCoachDetailsPhonenumber)).getText().toString();
                String email = ((TextView) coachDetailsView.findViewById(R.id.editCoachDetailsEmail)).getText().toString();
                BitmapDrawable drawable = (BitmapDrawable) ((ImageView) coachDetailsView.findViewById(R.id.editCoachDetailsPhoto)).getDrawable();

                if (currentCoach != null) {
                    currentCoach.setFirstName(firstName);
                    currentCoach.setLastName(lastName);
                    currentCoach.setPhonenumber(phonenumber);
                    currentCoach.setPhoto(drawable.getBitmap());
                    currentCoach.setEmail(email);
                    CoachServiceClient.getInstance().updateCoach(currentCoach);
                } else {
                    Coach newCoach = new Coach(firstName, lastName);
                    newCoach.setPhoto(drawable.getBitmap());
                    newCoach.setPhonenumber(phonenumber);
                    newCoach.setEmail(email);
                    CoachServiceClient.getInstance().createNewCoach(newCoach);
                    currentCoach = newCoach;
                }
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.reloadCoachesList();
                    ((FrameLayout) coachDetailsView.getParent().getParent()).setVisibility(View.INVISIBLE);
                } else {
                    getActivity().finish();
                }
            }
        });

        return coachDetailsView;

    }

    public void setNewCoachData(AddContactDialogFragment.ContactData selectedContact) {
        ((TextView) coachDetailsView.findViewById(R.id.editCoachDetailsFirstname)).setText(selectedContact.getFirstName());
        ((TextView) coachDetailsView.findViewById(R.id.editCoachDetailsLastname)).setText(selectedContact.getLastName());
        ((TextView) coachDetailsView.findViewById(R.id.editCoachDetailsPhonenumber)).setText(selectedContact.getPhoneNumber());
        ((TextView) coachDetailsView.findViewById(R.id.editCoachDetailsEmail)).setText(selectedContact.getEmail());
        if (StringUtils.isNotBlank(selectedContact.getPhotoUri())) {
            Uri photoUri = Uri.parse(selectedContact.getPhotoUri());
            ((ImageView) coachDetailsView.findViewById(R.id.editCoachDetailsPhoto)).setImageURI(photoUri);
        }
    }
}
