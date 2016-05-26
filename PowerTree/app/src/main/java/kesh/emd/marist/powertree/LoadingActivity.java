package kesh.emd.marist.powertree;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import kesh.emd.marist.powertree.Server.DBHandler;

public class LoadingActivity extends AppCompatActivity {

    DBHandler controller = new DBHandler(this, null, null, 1);
    ProgressDialog prgDialog;
    HashMap<String, String> userValues;
    HashMap<String, String> profileValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Transferring Data from Remote MySQL DB to SQLite. Please wait...");
        prgDialog.setCancelable(false);
        getSQLdata();
    }



    public void getSQLdata() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php
        client.post("http://10.0.2.2:80/mysqllitesync/getall.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, byte[] response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                String strresponse="no data received";
                try {
                     strresponse= new String(response, "UTF-8");  // Best way to decode using "UTF-8"
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //code doesn't work universally to parse json,so must have multiple functions for each table
                importData(strresponse);
            }
            // When error occured
            @Override
            public void onFailure(int statusCode, Header[] header, byte[] content, Throwable error ) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * takes in json strings and puts data into sqlite
     * @param response
     */
    public void importData(String response){
        //delete data from  all tables
        controller.deleteUsers();
        controller.deleteProfiles();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            //phpmyadmin removed password of connect account on computer reboot, if code fails with "Value <br of type java.lang.String cannot be converted to JSONArray"
            //then try and change user password.
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    // DB userValues Object to insert into SQLite
                    userValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    userValues.put("user_id", obj.get("userId").toString());
                    userValues.put("company_id", obj.get("companyId").toString());
                    userValues.put("superior_id", obj.get("superiorId").toString());
                    userValues.put("name", obj.get("userName").toString());
                    userValues.put("email", obj.get("userEmail").toString());
                    userValues.put("title", obj.get("userTitle").toString());
                    controller.insertUser(userValues);
                }
                // Reload the Main Activity
                Intent RegisterIntent = new Intent(this, RegisterActivity.class);
                startActivity(RegisterIntent);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
