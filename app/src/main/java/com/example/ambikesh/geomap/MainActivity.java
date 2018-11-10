package com.example.ambikesh.geomap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvlatitue, tvlongitude, city,state,country,postalcode,knownplace,address;

    FusedLocationProviderClient fusedLocationProviderClient;

    Location lastlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvlatitue = findViewById(R.id.latitude);
        tvlongitude = findViewById(R.id.longitude);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        postalcode = findViewById(R.id.postalcode);
        knownplace = findViewById(R.id.knownplace);
        address=findViewById(R.id.address);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
    }

    public void dothis(View view) {
        getLocation();
    }

    void getLocation()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            Log.d("TAG", "getLocation: permissions granted");
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location != null)
                    {
                        lastlocation = location;
                        double latitude = lastlocation.getLatitude();
                        double longitue = lastlocation.getLongitude();

                        tvlatitue.setText(""+latitude);
                        tvlongitude.setText(""+longitue);

                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());


                        try {
                            //List<Address> locationlist = geocoder.getFromLocation(latitude,longitue,1);
                            List<Address> addresses= geocoder.getFromLocation(latitude, longitue, 1);


                            if(addresses.size() >0)
                            {
                               // Address address = locationlist.get(0);
                                String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city1 = addresses.get(0).getSubAdminArea();
                                String state1 = addresses.get(0).getAdminArea();
                                String country1 = addresses.get(0).getCountryName();
                                String postalCode1 = addresses.get(0).getPostalCode();
                                String knownName1 = addresses.get(0).getFeatureName();
                                address.setText(address1);
                                city.setText(city1);
                                state.setText(state1);
                                country.setText(country1);
                                postalcode.setText(postalCode1);
                                knownplace.setText(knownName1);


                               // tvphysicaladdress.setText("Address is:"+ address);
                            }

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    getLocation();
                else
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void openMap(View view) {

       startActivity(new Intent(this,MapActivity.class));

    }

}
