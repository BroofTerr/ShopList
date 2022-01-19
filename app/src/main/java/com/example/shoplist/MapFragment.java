package com.example.shoplist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class Place
{
    String name;
    String placeLat;
    String placeLong;

    public Place(String n, String pLat, String pLong)
    {
        name = n;
        placeLat = pLat;
        placeLong = pLong;
    }
}

public class MapFragment extends Fragment {

    SupportMapFragment mapFragment;

    FusedLocationProviderClient locClient;
    String latitude = "";
    String longitude = "";

    List<Place> places = new ArrayList<>();
    String placesURL;

    public MapFragment() {
        // Required empty public constructor
    }

    private String readJSONfromFile() {
        String data = null;
        try {
            InputStream stream = getActivity().getAssets().open("places.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            data = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return data;
    }

    private String readJSONfromURL() {
        String data = null;
        try {
            URL url = new URL(placesURL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                data += line;
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return data;
    }

    private void parseJSON()
    {
        try
        {

            //JSONObject jsonObject = new JSONObject(readJSONfromURL());
            JSONObject jsonObject = new JSONObject(readJSONfromFile());

            JSONArray placesArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < placesArray.length(); i++)
            {
                JSONObject placesObject = placesArray.getJSONObject(i);
                JSONObject geometryObject = placesObject.getJSONObject("geometry");
                JSONObject locationObject = geometryObject.getJSONObject("location");

                String name = placesObject.getString("name");
                String lat = locationObject.getString("lat");
                String lng = locationObject.getString("lng");

                Place p = new Place(name, lat, lng);
                places.add(p);
            }
        }
        catch (JSONException e) {}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        //Update location
        getLocation();
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng loc = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            googleMap.addMarker(new MarkerOptions().position(loc).title("Your location marker").icon(BitmapDescriptorFactory.defaultMarker(100)));

            // For nearby locations, need to just loop through them and create markers
            for (Place p : places)
            {
                LatLng place = new LatLng(Double.valueOf(p.placeLat), Double.valueOf(p.placeLong));
                googleMap.addMarker(new MarkerOptions().position(place).title(p.name));
            }

            /*CircleOptions co = new CircleOptions()
                    .center(loc)
                    .radius(500)
                    .strokeColor(Color.RED);
            googleMap.addCircle(co);*/

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 12f));
            Toast.makeText(getContext(), "Your latitude: " + latitude + ", longitude: " + longitude, Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.fragment_map, container, false);

        locClient = LocationServices.getFusedLocationProviderClient(getContext());

        getLocation();


        return fragView;
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        if (checkPermissions()) {
            if (isLocationEnabled()) {

                locClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            LocationRequest locRequest = new LocationRequest();
                            locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            locRequest.setInterval(5);
                            locRequest.setFastestInterval(0);
                            locRequest.setNumUpdates(1);

                            locClient = LocationServices.getFusedLocationProviderClient(getContext());
                            locClient.requestLocationUpdates(locRequest, locCallback, Looper.myLooper());
                        } else {
                            updateLocation(location);
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private LocationCallback locCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLoc = locationResult.getLastLocation();
            updateLocation(lastLoc);
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET}, 1);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    public void updateLocation(@NonNull Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        placesURL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=Store&location="+latitude+","+longitude+"&radius=200&type=supermarket,bakery&key=AIzaSyBrJ0b1be3rMdkbR0-LzI4RAda6UuPn370";

        parseJSON();
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}