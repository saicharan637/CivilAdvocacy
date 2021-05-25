package com.example.civiladvocacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private Adapter Adapter;
    private List<OfficialDetails> officialDetailsList = new ArrayList<>();
    private TextView place;
    private TextView main_act_loc;
    private String location;
    OfficialDataDownloader datadownloader;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private static final String TAG = "MainActivity";
    private static String locationString = "";
    private static String loc = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        place = findViewById(R.id.main_act_loc);
        recyclerView = findViewById(R.id.recycler_view);
        Adapter = new Adapter(officialDetailsList, this);
        recyclerView.setAdapter(Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable =new ColorDrawable(Color.parseColor("#441144"));
        actionBar.setBackgroundDrawable(colorDrawable);
        if(Internetcheck()){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            determineLocation();
            Log.d(TAG, "onCreate: it is:"+locationString);
        }
        else {
            NoInternet();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                LocationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void LocationDialog(){
        final AlertDialog.Builder add_dialog = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.location, null);
        final EditText editText = customLayout.findViewById(R.id.location);
        add_dialog.setView(customLayout);
        add_dialog.setTitle("Enter Address ");

        add_dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!Internetcheck()){
                    NoInternet();
                }else{
                    String loc = editText.getText().toString();
                    if(loc.isEmpty()){
                        Toast.makeText(MainActivity.this, "Please Enter the Location", Toast.LENGTH_SHORT).show();
                    } else {
                        OfficialDataDownloader datadownloader = new OfficialDataDownloader(MainActivity.this, loc);
                        new Thread(datadownloader).start();
                    }
                }

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dial = add_dialog.create();
        dial.show();
    }

    private boolean Internetcheck(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager==null)
            return false;

        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities nw = connectivityManager.getNetworkCapabilities(network);
        return nw != null && (nw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                nw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    public void NoInternet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void OfficialsList(ArrayList<Object> resultObjList) {

        if (resultObjList == null) {
            location = "No Data For Location";
            officialDetailsList.clear();
        } else {
            location = (String) resultObjList.get(0);
            place.setText(location);
            officialDetailsList.clear();
            officialDetailsList.addAll((List<OfficialDetails>) resultObjList.get(1));
        }

        Adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        OfficialDetails official = officialDetailsList.get(position);
        String s="No Data For Location";
        Intent intent_official = new Intent(MainActivity.this, OfficialActivity.class);
        intent_official.putExtra("LOCATION", location);
        intent_official.putExtra("DETAILS", official);
        intent_official.putExtra("s", s);
        startActivity(intent_official);
    }

    private  void determineLocation() {
        if (checkPermission()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            locationString = String.format(Locale.getDefault(),
                                    "%.5f, %.5f", location.getLatitude(), location.getLongitude());
                            Log.d(TAG, "onCreate: it is this:"+locationString);
                            datadownloader = new OfficialDataDownloader(MainActivity.this, locationString);
                            new Thread(datadownloader).start();

                        }else{
                            Log.d(TAG, "determineLocation: "+"yes it is null");
                        }
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
        }

    }
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, LOCATION_REQUEST);
            Log.d(TAG, "onCreate: it is:"+locationString);
            return false;
        }
        Log.d(TAG, "onCreate: it is:"+locationString);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onCreate: it is:"+locationString);
        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    Toast.makeText(MainActivity.this, "Location permission was denied - cannot determine address", Toast.LENGTH_SHORT).show();;
                }
            }
        }
    }

}