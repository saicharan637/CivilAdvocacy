package com.example.civiladvocacy;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OfficialDataDownloader implements Runnable {

    MainActivity mainActivity;
    private static final String TAG = "OfficialDataDownloader";
    private static final String OfficialInfoURL = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String API_KEY = "AIzaSyArDKX5GfQjXTtffgIRQm8d1KVrso-xvrM";
    String location;

    OfficialDataDownloader(MainActivity mainActivity, String location){
        this.mainActivity = mainActivity;
        this.location = location;
    }

    @Override
    public void run() {
        Uri.Builder builderURL = Uri.parse(OfficialInfoURL).buildUpon();
        builderURL.appendQueryParameter("key", API_KEY).appendQueryParameter("address", location);
        String urlToUse = builderURL.toString();

        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
            handleResults(null);
            return;
        }

        handleResults(sb.toString());

    }
    private void handleResults(String jsonString){
    final ArrayList<Object> resultObjList = parseJSON(jsonString);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.OfficialsList(resultObjList);
            }
        });
    }

    private ArrayList<Object> parseJSON(String s) {
        ArrayList<Object> resultObjList = new ArrayList<>();
        String location;
        ArrayList<OfficialDetails> officialList = new ArrayList<>();

        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONObject normalizedInput = jObjMain.getJSONObject("normalizedInput");

            location = normalizedInput.getString("line1")
                    + normalizedInput.getString("city")
                    + " "
                    + normalizedInput.getString("state")
                    + " "
                    + normalizedInput.getString("zip");

            JSONArray offices = jObjMain.getJSONArray("offices");
            JSONArray officials = jObjMain.getJSONArray("officials");

            for (int i = 0; i < offices.length(); i++) {
                JSONObject jObjOffice = (JSONObject) offices.get(i);

                String officeName = "";
                if (jObjOffice.has("name")) {
                    officeName = jObjOffice.getString("name");
                } else {
                    officeName = "-";
                }

                JSONArray officialIndices = jObjOffice.getJSONArray("officialIndices");

                for (int j = 0; j < officialIndices.length(); j++) {

                    JSONObject jObjOfficial = (JSONObject) officials.get((int) officialIndices.get(j));

                    String name = "";
                    String address = "";
                    String party = "";
                    String phones = "";
                    String urls = "";
                    String photoUrl = "";
                    String emails="";
                    Map<String, String> channels = new HashMap<String, String>();

                    if (jObjOfficial.has("name")) {
                        name = jObjOfficial.getString("name");
                    }

                    if (jObjOfficial.has("address")) {
                        JSONObject addressJObj = (JSONObject) jObjOfficial.getJSONArray("address").get(0);

                        String line1 = "";
                        String line2 = "";
                        String line3 = "";
                        String city = "";
                        String state = "";
                        String zip = "";

                        if (addressJObj.has("line1"))
                            line1 = addressJObj.getString("line1");
                        if (addressJObj.has("line2"))
                            line2 = addressJObj.getString("line2");
                        if (addressJObj.has("line3"))
                            line3 = addressJObj.getString("line3");

                        if (addressJObj.has("city"))
                            city = addressJObj.getString("city");
                        if (addressJObj.has("state"))
                            state = addressJObj.getString("state");
                        if (addressJObj.has("zip"))
                            zip = addressJObj.getString("zip");

                        address = line1 + " " + line2 + " " + line3 + "\n" + city + ", " + state + " " + zip;

                    }
                    if (jObjOfficial.has("party")) {
                        party = jObjOfficial.getString("party");
                    }
                    if (jObjOfficial.has("phones")) {
                        phones = (String) jObjOfficial.getJSONArray("phones").get(0);
                    }
                    if (jObjOfficial.has("emails")) {
                        emails = (String) jObjOfficial.getJSONArray("emails").get(0);
                        Log.d("OfficialDataDownloader+ it is here:", "parseJSON: ");
                    }
                    if (jObjOfficial.has("urls")) {
                        urls = (String) jObjOfficial.getJSONArray("urls").get(0);
                    }

                    if (jObjOfficial.has("photoUrl")) {
                        photoUrl = jObjOfficial.getString("photoUrl");
                    }
                    else {
                        photoUrl = null;
                    }

                    if (jObjOfficial.has("channels")) {
                        JSONArray channelsJArray = jObjOfficial.getJSONArray("channels");

                        for (int k = 0; k < channelsJArray.length(); k++) {
                            try {
                                JSONObject jObjChannel = (JSONObject) channelsJArray.get(k);
                                String type = jObjChannel.getString("type");
                                String id = jObjChannel.getString("id");
                                channels.put(type, id);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        channels = null;
                    }

                    officialList.add(new OfficialDetails(name, officeName, party, address , phones, emails,urls, photoUrl, channels));
                }

            }
            resultObjList.add(location);
            resultObjList.add(officialList);

            return resultObjList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
