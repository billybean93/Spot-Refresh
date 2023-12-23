package rmit.ad.spotrefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddSpot extends Activity {

    private EditText editTextGeneralInfo;
    private DatePicker datePicker;
    private Button submitButton;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);

        // Initialize UI components
        editTextGeneralInfo = findViewById(R.id.editTextGeneralInfo);
        datePicker = findViewById(R.id.datePicker);
        submitButton = findViewById(R.id.submitButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("spots");


        // Set up click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String generalInfo = editTextGeneralInfo.getText().toString();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1; // Months are 0-indexed
                int year = datePicker.getYear();

                // Convert DatePicker values to a Date object
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date selectedDate = calendar.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String formattedDate = sdf.format(selectedDate);

                // Do something with the input (e.g., display a toast)
                String message = "General Info: " + generalInfo + "\nDate: " + day + "/" + month + "/" + year;
                Toast.makeText(AddSpot.this, message, Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                if (intent != null) {
                    double markerLatitude = intent.getDoubleExtra("marker_latitude", 0.0);
                    double markerLongitude = intent.getDoubleExtra("marker_longitude", 0.0);

                    // Now you have the data, you can save it to the database
                    saveSpotToDatabase(generalInfo, markerLatitude, markerLongitude, selectedDate);
                }
                finish();
            }
        });
    }

    private void saveSpotToDatabase(String title, double latitude, double longitude, Date date) {
        // Generate a unique key for the spot
        String spotId = databaseReference.push().getKey();

        // Create a SpotInfo object with the received data
        SpotInfo spot = new SpotInfo(title, latitude, longitude, date);

        // Save the SpotInfo object to the database using the unique key
        databaseReference.child(spotId).setValue(spot);
    }

    // Assuming you have a method to get the current user's ID
    private String getCurrentUserID() {
        // Implement this method to get the current user's ID (Firebase Authentication)
        // For example, if you are using Firebase Authentication:
        // FirebaseAuth.getInstance().getCurrentUser().getUid();
        return "replace_with_actual_user_id";
    }
}
