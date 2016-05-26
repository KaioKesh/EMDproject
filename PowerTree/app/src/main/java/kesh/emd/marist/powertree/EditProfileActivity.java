package kesh.emd.marist.powertree;

/**
 * Created by Keshine on 4/20/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import kesh.emd.marist.powertree.Server.DBHandler;
import kesh.emd.marist.powertree.UserTree.Profile;
import kesh.emd.marist.powertree.UserTree.User;

public class EditProfileActivity extends AppCompatActivity {

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.edit_profile);
        ActionBar actionBar = getSupportActionBar();

        actionBar.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu_save, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_profile:
                //retrieve values
                EditText profilename = (EditText) findViewById(R.id.edit_profilename);
                AutoCompleteTextView profiletitle = (AutoCompleteTextView) findViewById(R.id.edit_profiletitle);
                EditText profileemail = (EditText) findViewById(R.id.edit_profileemail);
                String name = profilename.getText().toString();
                String email = profileemail.getText().toString();
                String title = profiletitle.getText().toString();

                //sharedPreferences save
                preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                preferenceEditor = preferenceSettings.edit();
                preferenceEditor.putString("name",name);
                preferenceEditor.putString("email", email);
                preferenceEditor.putString("title", title);
                preferenceEditor.commit();

                long userid = preferenceSettings.getLong("user", -1);
                //database save
                DBHandler dbHandler = new DBHandler(this, null, null, 1);
                User thisuser = new User(userid);
                thisuser.setProfile(new Profile(name,email,title));
                dbHandler.editProfile(thisuser);
                String json = strprofiletojson(name,email,title,userid);
                uploadProfile(json);

                Intent saveintent = new Intent(this, ProfileActivity.class);
                saveintent.putExtra("userid", userid);
                this.startActivity (saveintent);
                return true;
        }
        return false;
    }
    public String strprofiletojson(String name, String email, String title,long userid){
        Gson gson = new GsonBuilder().create();
        HashMap<String, String> map = new HashMap<String, String>();
        ArrayList<HashMap<String, String>> jsonarray = new ArrayList<>();
        // Add status for each User in Hashmap
        map.put("id",String.valueOf(userid));
        map.put("name", name);
        map.put("email", email);
        map.put("title", title);
        jsonarray.add(map);
        return gson.toJson(jsonarray);
    }
    public void uploadProfile(String json) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("profile", json);
        client.post("http://10.0.2.2:80/mysqllitesync/updateprofile.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, byte[] response) {
                Toast.makeText(getApplicationContext(),	"Profile updated", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] header, byte[] response, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });
    }
}