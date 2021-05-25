package com.example.civiladvocacy;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Map;

public class OfficialActivity extends AppCompatActivity {
    private OfficialDetails officialDetails;
    private TextView name;
    private TextView office;
    private TextView party;
    private TextView phone;
    private TextView phone_label;
    private TextView website;
    private TextView website_label;
    private TextView address;
    private TextView address_label;
    private TextView place;
    private TextView emailid;
    private TextView email;
    private ImageView OfficialImage;
    private ImageView repPartyImg;
    private ImageView facebookImg;
    private ImageView TwitterImg;
    private ImageView YoutubeImg;
    private String FacebookLink;
    private String YoutubeLink;
    private String TwitterLink;
    private String loc;
    private String url;
    private String web;
    private static final String TAG = "OfficialActivity";
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        name = findViewById(R.id.name);
        office = findViewById(R.id.title_label);
        party = findViewById(R.id.party_label);
        phone = findViewById(R.id.phone_text);
        phone_label=findViewById(R.id.phone_label);
        place = findViewById(R.id.area_label);
        email=findViewById(R.id.email);
        emailid=  findViewById(R.id.emailid);
        website = findViewById(R.id.site_text);
        website_label=findViewById(R.id.site_label);
        OfficialImage = findViewById(R.id.personimg);
        address = findViewById(R.id.address_text);
        address_label = findViewById(R.id.address_label);
        repPartyImg = findViewById(R.id.partysymb);
        facebookImg = findViewById(R.id.fbimgview);
        YoutubeImg = findViewById(R.id.ytimgview);
        TwitterImg = findViewById(R.id.twitimgview);

        Intent intent = getIntent();
        if (intent.hasExtra("LOCATION")) {
            loc = intent.getStringExtra("LOCATION");
            place.setText(loc);
        } else {
            place.setText("");
        }
        if (intent.hasExtra("DETAILS")) {
            officialDetails = (OfficialDetails) intent.getSerializableExtra("DETAILS");
            if (officialDetails != null) {
                setOfficialsData(officialDetails);
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
            OfficialImage.setImageResource(R.drawable.brokenimage);
        }

    }

    public void setOfficialsData(OfficialDetails officialDetails) {

        office.setText(officialDetails.getOffice());
        name.setText(officialDetails.getName());

        if (officialDetails.getParty().equals("Republican Party") || officialDetails.getParty().equals("Republican")) {
            String pt = "(" + officialDetails.getParty() + ")";
            party.setText(pt);
            findViewById(R.id.rep_data_layout).setBackgroundColor(Color.RED);
            repPartyImg.setImageResource(R.drawable.rep_logo);
        } else if (officialDetails.getParty().equals("Democratic Party") || officialDetails.getParty().equals("Democratic")) {
            String pt = "(" + officialDetails.getParty() + ")";
            party.setText(pt);
            findViewById(R.id.rep_data_layout).setBackgroundColor(Color.BLUE);
            repPartyImg.setImageResource(R.drawable.dem_logo);
        } else if(officialDetails.getParty().equals("Nonpartisan")){
            findViewById(R.id.rep_data_layout).setBackgroundColor(Color.BLACK);
            repPartyImg.setVisibility(View.INVISIBLE);
        }
        if(officialDetails.getAddress().length()==0){
            address_label.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            Log.d(TAG, "setOfficialsData: no it is in if");
        }else{
            address.setText(officialDetails.getAddress());
            Linkify.addLinks(address, Linkify.ALL);
            Log.d(TAG, "setOfficialsData: yes it is here");
        }
//
//
//        if (!officialDetails.getAddress().equals("No Data Provided")) {
//            Linkify.addLinks(address, Linkify.ALL);
//        }
        if(officialDetails.getPhone().length()==0){
            phone.setVisibility(View.GONE);
            phone_label.setVisibility(View.GONE);
            Linkify.addLinks(phone, Linkify.ALL);
        }
        phone.setText(officialDetails.getPhone());
        if (!officialDetails.getPhone().equals("No Data Provided")) {
            Linkify.addLinks(phone, Linkify.ALL);
        }
        if(officialDetails.getEmail().length()==0){
            email.setVisibility(View.GONE);
            emailid.setVisibility(View.GONE);
            Linkify.addLinks(emailid, Linkify.ALL);
        }
        emailid.setText(officialDetails.getEmail());
        Linkify.addLinks(emailid, Linkify.ALL);
        if(officialDetails.getWebsite().length()==0){
            website.setVisibility(View.GONE);
            website_label.setVisibility(View.GONE);
            Linkify.addLinks(website, Linkify.ALL);
        }
        else if(officialDetails.getWebsite().length()>35){
            web= officialDetails.getWebsite().substring(0,35)+"\n"+ officialDetails.getWebsite().substring(35);
            website.setText(web);
        }else{
            website.setText(officialDetails.getWebsite());
        }
        if (!officialDetails.getWebsite().equals("No Data Provided")) {
            Linkify.addLinks(website, Linkify.ALL);
        }
        if (officialDetails.getSocial_media() != null) {
            Map<String, String> socialmedia = officialDetails.getSocial_media();

            if (socialmedia.containsKey("Facebook")) {
                FacebookLink = socialmedia.get("Facebook");
               facebookImg.setVisibility(View.VISIBLE);
          } else {
                facebookImg.setVisibility(View.INVISIBLE);
            }

            if (socialmedia.containsKey("Twitter")) {
                TwitterLink = socialmedia.get("Twitter");
                TwitterImg.setVisibility(View.VISIBLE);
            } else {
                TwitterImg.setVisibility(View.INVISIBLE);
            }

            if (socialmedia.containsKey("YouTube")) {
                YoutubeLink = socialmedia.get("YouTube");
                YoutubeImg.setVisibility(View.VISIBLE);

            } else {
                YoutubeImg.setVisibility(View.INVISIBLE);
            }
        }

        DownloadPhotos();
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

    public void partywebsite(View V){
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


    public void DownloadPhotos() {
        if(!Internetcheck()){
            OfficialImage.setImageResource(R.drawable.brokenimage);
        }

        final String photoUrl = officialDetails.getPhoto();
        if (photoUrl != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = photoUrl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(OfficialImage);

                }
            }).build();

            picasso.load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(OfficialImage);
        } else {
            Picasso.get().load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missing)
                    .into(OfficialImage);

        }
    }

    public void photoButtonClicked(View v) {
        if(officialDetails.getPhoto() == null){
        } else {
            Intent intent_official = new Intent(this, PhotoActivity.class);
            intent_official.putExtra("LOCATION", loc);
            intent_official.putExtra("DETAILS", officialDetails);
            startActivity(intent_official);
        }
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + FacebookLink;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                urlToUse = "fb://page/" + FacebookLink;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
            }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }


    public void twitterClicked(View v) {
        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + TwitterLink));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        catch (Exception e) { // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + TwitterLink));
        }
        startActivity(intent);
    }

    public void youtubeClicked(View v) {

        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + YoutubeLink));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + YoutubeLink)));
        }
    }

}