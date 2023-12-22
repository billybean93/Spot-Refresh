package rmit.ad.spotrefresh;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OwnerMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final long UPDATE_INTERVAL = 20*1000 ;
    private static final long FASTEST_INTERVAL = 10*1000 ;
    protected FusedLocationProviderClient client;
    protected LocationRequest mLocationRequest;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(OwnerMapsActivity.this);
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng);
            }
        });

        // Set up marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Handle marker click
                // Start the new activity here to allow the user to add more info
                startMarkerDetailsActivity(marker);
                return true;
            }
        });

        startLocationUpdate();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(OwnerMapsActivity.this, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @SuppressLint("MissingPermission")
    public void getPosition(View view){
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                Toast.makeText(OwnerMapsActivity.this, "(" + location.getLatitude() + ","
                        + location.getLongitude() +")", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint({"MissingPermission", "RestrictedApi"})
    private void startLocationUpdate(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        client.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult){
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Toast.makeText(OwnerMapsActivity.this, "(" + location.getLatitude() + ","
                        + location.getLongitude() +")", Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    private void addMarker(LatLng latLng) {
        // Add a marker at the clicked location
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));

        // Move camera to the marker
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

        // Optionally, you can perform additional actions with the marker
        // For example, you can store the marker information in Firebase or perform other tasks.
    }

    private void startMarkerDetailsActivity(Marker marker) {
        // Start the MarkerDetailsActivity and pass necessary information
        Intent intent = new Intent(this, AddSpot.class);
        intent.putExtra("marker_title", marker.getTitle());
        intent.putExtra("marker_latitude", marker.getPosition().latitude);
        intent.putExtra("marker_longitude", marker.getPosition().longitude);
        startActivity(intent);
    }

//    private class GetRestaurant extends AsyncTask<Void,Void,Void> {
//        String jsonString ="";
//        @Override
//        protected Void doInBackground(Void... voids) {
//            jsonString = HttpHandler.getRequest(RESTAURANT_API_URL);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid){
//            super.onPostExecute(aVoid);
//
//            try {
//                JSONArray jsonArray = new JSONArray(jsonString);
//                for (int i=0; i < jsonArray.length();i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    LatLng position = new LatLng(
//                            jsonObject.getDouble("latitude"),
//                            jsonObject.getDouble("longitude"));
//                    mMap.addMarker(new MarkerOptions()
//                            .position(position)
//                            .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.restaurant))
//                            .snippet(jsonObject.getString("name")));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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