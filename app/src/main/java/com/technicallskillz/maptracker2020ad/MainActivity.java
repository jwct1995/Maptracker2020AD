package com.technicallskillz.maptracker2020ad;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.technicallskillz.maptracker2020ad.Room.Item;
import com.technicallskillz.maptracker2020ad.Room.RoomDatabaseClass;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_PERMISIION = 10101;
    private static final int ERROR_CODE = 1111;

    MapView mapView;
    GoogleMap gMap;

    FusedLocationProviderClient mLocationClient;
    LocationCallback mLocationCallback;

    Toolbar toolbar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToolBar
        toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Map Tracker 2020 AD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);

        //navigation drawer layout
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


        //mapView initilization
        mapView = findViewById(R.id.mapView);
        initMap();
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);


        //call method for livae location
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Location location = locationResult.getLastLocation();

                    GotoMyLocation(location.getLatitude(), location.getLongitude());
                }
            }
        };


        //check if device has enable gps or not ,if enable ,then call getLocationUpdate method
        if (isGpsEnabled()) {
            getLocationUpdate();
        } else {
            Toast.makeText(MainActivity.this, "Please Enable GPS Location", Toast.LENGTH_SHORT).show();
        }


    }

    //gps enable checking method implementation
    boolean isGpsEnabled() {
        LocationManager locationManage = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManage.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    //get Updates from gps ,like longitude and latitude ,quardinates
    private void getLocationUpdate() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //request for gps time
        locationRequest.setInterval(5000);
        //data save time
        locationRequest.setFastestInterval(30000);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

    //after getting live updates from gps redirect our marker on map to current location
    private void GotoMyLocation(final double lat, double lng) {

        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(latLng, 10f);
        MarkerOptions myLocationMarker = new MarkerOptions();
        myLocationMarker.position(latLng);
        myLocationMarker.title("I am Here");
        myLocationMarker.draggable(false);
        gMap.addMarker(myLocationMarker);

        gMap.animateCamera(cameraUpdate1);
        StorageDatainDatabase(latLng);

    }

    //after getting date,time,location and location name store info into phone memory
    private void StorageDatainDatabase(LatLng latLng) {

        //get date and time
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        //convert location into address name
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(39.6985207, -104.8954315, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        if (addresses.size() > 0) {
            Address address = addresses.get(0);
            sb.append(address.getAddressLine(0)).append("\n");
            //sb.append(address.getPlace().getName())//.append("\n");
            sb.append(address.getLocality()).append("\n");
            sb.append(address.getPostalCode()).append("\n");
            sb.append(address.getCountryName());

        }

        //store into phone memory
        Item item = new Item(sb.toString(), date, latLng.longitude, latLng.latitude);
        RoomDatabaseClass database = Room.databaseBuilder(MainActivity.this, RoomDatabaseClass.class, "myDatabase").allowMainThreadQueries().build();
        database.myDao().InsertSong(item);
        Toast.makeText(this, "Data Stored", Toast.LENGTH_SHORT).show();
    }


    //for google map device required google play services without play services we cant show google map.
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initMap() {
        if (isServicesOK()) {
            if (PermissionOK()) {
                Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
            } else {
                RequestPermission();
            }
        }
    }

    //check google play services is availabale or not
    private boolean isServicesOK() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int result = apiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            Toast.makeText(this, "Google Play Services OK", Toast.LENGTH_SHORT).show();
            return true;
        } else if (apiAvailability.isUserResolvableError(result)) {
            Dialog dialog = apiAvailability.getErrorDialog(MainActivity.this, result, ERROR_CODE, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(MainActivity.this, "Dialog is Cancelled By User", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else {
            Toast.makeText(this, "Google Play Service is required for Google maps", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    //check google map and gps permission
    private boolean PermissionOK() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void RequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISIION);
        }
    }

    //after cehcing services google map call this method map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
    }



    //all these methods used for google map but not for any task.
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

  //  check which items is selected on navigation
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    //redirect to next activity when click on navigation menu items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.record) {
            startActivity(new Intent(this, ViewActivity.class));

        }
        return true;
    }
}