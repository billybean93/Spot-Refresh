package rmit.ad.spotrefresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class VolunteerMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_maps);

        // Initialize the fused location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Start location updates
        startLocationUpdate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);




        // Move the camera to the device location
        getDeviceLocation();

        // Display markers from the real-time database
        addMarkersFromDatabase();
    }

    private void startLocationUpdate() {
        // Implement location update logic here if needed
        // ...
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker()));
                }

            }
        });
    }

    @SuppressLint("MissingPermission")
    public void getPosition(View view){
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                Toast.makeText(VolunteerMapsActivity.this, "(" + location.getLatitude() + ","
                        + location.getLongitude() +")", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ...

    private void addMarkersFromDatabase() {
        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the "spots" node in the database
        database.getReference("spots").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Loop through each marker in the database
                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                    // Get the values from the marker data
                    String title = markerSnapshot.child("title").getValue(String.class);
                    double latitude = markerSnapshot.child("latitude").getValue(Double.class);
                    double longitude = markerSnapshot.child("longitude").getValue(Double.class);

                    // Create a LatLng object with the retrieved latitude and longitude values
                    LatLng location = new LatLng(latitude, longitude);

                    // Create MarkerOptions and add a marker to the map
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(location)
                            .title(title);

                    mMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(VolunteerMapsActivity.this, "Failed to read markers from database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

// ...


    private Date parseDate(String dateString) {
        // Implement date parsing logic here based on the actual date format in your database
        // For example, using SimpleDateFormat:
        // SimpleDateFormat dateFormat = new SimpleDateFormat("your_date_format_here");
        // return dateFormat.parse(dateString);
        return new Date(); // Placeholder, modify as needed
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()
                , vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
