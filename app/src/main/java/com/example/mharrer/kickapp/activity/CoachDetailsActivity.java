package com.example.mharrer.kickapp.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mharrer.kickapp.R;
import com.example.mharrer.kickapp.fragments.CoachDetailsFragment;
import com.example.mharrer.kickapp.model.Coach;


public class CoachDetailsActivity extends FragmentActivity {

    private Coach currentCoach;

    public static Bitmap currentCoachPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_coach_details);
        Intent intent = getIntent();
        Object coachObj = intent.getExtras().get("coach");

        ImageView imageView = (ImageView) findViewById(R.id.editCoachDetailsPhoto);

        if (coachObj != null) {
            Coach coach = (Coach) coachObj;
            currentCoach = coach;
            currentCoach.setPhoto(currentCoachPhoto);
            TextView firstnameField = (TextView) findViewById(R.id.editCoachDetailsFirstname);
            firstnameField.setText(currentCoach.getFirstName());

            TextView lastnameField = (TextView) findViewById(R.id.editCoachDetailsLastname);
            lastnameField.setText(currentCoach.getLastName());

            TextView phonenumberField = (TextView) findViewById(R.id.editCoachDetailsPhonenumber);
            phonenumberField.setText(currentCoach.getPhonenumber());

            TextView emailField = (TextView) findViewById(R.id.editCoachDetailsEmail);
            emailField.setText(currentCoach.getEmail());

            if (currentCoach.getPhoto() != null) {
                imageView.setImageBitmap(currentCoach.getPhoto());
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.placeholder);
                imageView.setImageBitmap(bitmap);
            }
        } else {
            currentCoach = null;
            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.placeholder);
            imageView.setImageBitmap(bitmap);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (currentCoach != null) {
            ((CoachDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.coachDetails)).setCurrentCoach(currentCoach);
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

    static final int REQUEST_IMAGE_CAPTURE = 2;

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
            ImageView imageView = (ImageView) findViewById(R.id.editCoachDetailsPhoto);
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
