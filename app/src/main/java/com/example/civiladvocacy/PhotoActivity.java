package com.example.civiladvocacy;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    private ImageView Image;
    private ImageView partyImage;
    private TextView name;
    private TextView place;
    private TextView title;
    private String loc;
    private OfficialDetails officialDetails;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Image = findViewById(R.id.personimg_photoact);
        partyImage = findViewById(R.id.partysymb_photoact);
        name = findViewById(R.id.name_photo);
        place = findViewById(R.id.area_label_photo);
        title = findViewById(R.id.title_label_photo);

        Intent intent = getIntent();
        if (intent.hasExtra("LOCATION")) {
            loc = intent.getStringExtra("LOCATION");
            place.setText(loc);
        } else {
            place.setText("");
        }
        if (intent.hasExtra("DETAILS")) {
            OfficialDetails officialDetails =(OfficialDetails) intent.getSerializableExtra("DETAILS");
            if (officialDetails != null) {
                setPhotoData(officialDetails);
            } else {
                Toast.makeText(this, "No official data provided.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Fail to receive official data.", Toast.LENGTH_SHORT).show();
        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable =new ColorDrawable(Color.parseColor("#441144"));
        actionBar.setBackgroundDrawable(colorDrawable);
        if(!Internetcheck()){
            Image.setImageResource(R.drawable.brokenimage);

        }
    }

    public void setPhotoData(OfficialDetails officialDetails) {

        if (officialDetails.getOffice().isEmpty()) {
            title.setText("No Data Provided");
        } else {
            title.setText(officialDetails.getOffice());
        }

        if (officialDetails.getName().isEmpty()) {
            name.setText("No Data Provided");
        } else {
            name.setText(officialDetails.getName());
        }

        if (officialDetails.getParty().isEmpty()) {
            findViewById(R.id.rep_photo).setBackgroundColor(Color.WHITE);
        } else {
            if (officialDetails.getParty().equals("Republican Party")) {
                findViewById(R.id.rep_photo).setBackgroundColor(Color.RED);
                partyImage.setImageResource(R.drawable.rep_logo);
            } else if (officialDetails.getParty().equals("Democratic Party")) {
                findViewById(R.id.rep_photo).setBackgroundColor(Color.BLUE);
                partyImage.setImageResource(R.drawable.dem_logo);
            } else {
                findViewById(R.id.rep_photo).setBackgroundColor(Color.BLACK);
                partyImage.setVisibility(View.INVISIBLE);
            }
        }

        DownloadPhotos(officialDetails.getPhoto());
    }
    public void partywebsitephoto(View V){
        if(officialDetails.getParty().equals("Democratic Party")){
            url="https://democrats.org/";
        }else if(officialDetails.getParty().equals("Republican Party")){
            url="https://www.gop.com/";
        }
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
        startActivity(intent);

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
    public void DownloadPhotos(final String photoUrl) {

        if (photoUrl != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {

                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = photoUrl.replace("http:", "https:");

                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(Image);

                }
            }).build();

            picasso.load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(Image);
            picasso.setLoggingEnabled(true);
        }
    }
}
