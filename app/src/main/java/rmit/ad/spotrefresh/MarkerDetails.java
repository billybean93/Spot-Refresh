package rmit.ad.spotrefresh;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MarkerDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_details);

        // Get data passed from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("marker_title");
            double latitude = intent.getDoubleExtra("marker_latitude", 0.0);
            double longitude = intent.getDoubleExtra("marker_longitude", 0.0);

            // Update TextViews with marker information
            TextView textViewTitle = findViewById(R.id.textViewTitle);
            TextView textViewDate = findViewById(R.id.textViewDate);
            TextView textViewLatitude = findViewById(R.id.textViewLatitude);
            TextView textViewLongitude = findViewById(R.id.textViewLongitude);

            textViewTitle.setText(title);
            // Assuming date is passed as a string, replace with actual date handling if needed
            textViewDate.setText("Date: " + "Your Date String Here");
            textViewLatitude.setText("Latitude: " + latitude);
            textViewLongitude.setText("Longitude: " + longitude);

            // Add more TextViews and update them with additional information as needed
        }
    }
}
