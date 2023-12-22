package rmit.ad.spotrefresh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        // Find the buttons by their IDs
        Button buttonBecomeVolunteer = findViewById(R.id.buttonBecomeVolunteer);
        Button buttonRegisterLocation = findViewById(R.id.buttonRegister);

        // Set OnClickListener for "Become a Volunteer" button
        buttonBecomeVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for "Become a Volunteer" button
                // For example, you can start a new activity
                startActivity(new Intent(UserTypeActivity.this, VolunteerMapsActivity.class));
            }
        });

        // Set OnClickListener for "Register a Clean-Up Location" button
        buttonRegisterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for "Register a Clean-Up Location" button
                // For example, you can start a new activity
                startActivity(new Intent(UserTypeActivity.this, OwnerMapsActivity.class));
            }
        });
    }
}
