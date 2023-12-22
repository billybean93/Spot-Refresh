package rmit.ad.spotrefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddSpot extends Activity {

    private EditText editTextGeneralInfo;
    private DatePicker datePicker;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);

        // Initialize UI components
        editTextGeneralInfo = findViewById(R.id.editTextGeneralInfo);
        datePicker = findViewById(R.id.datePicker);
        submitButton = findViewById(R.id.submitButton);

        // Set up click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String generalInfo = editTextGeneralInfo.getText().toString();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1; // Months are 0-indexed
                int year = datePicker.getYear();

                // Do something with the input (e.g., display a toast)
                String message = "General Info: " + generalInfo + "\nDate: " + day + "/" + month + "/" + year;
                Toast.makeText(AddSpot.this, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), OwnerMapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
