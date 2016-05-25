package kesh.emd.marist.powertree;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import kesh.emd.marist.powertree.Server.DBHandler;
import kesh.emd.marist.powertree.UserTree.Profile;
import kesh.emd.marist.powertree.UserTree.User;

/**
 * Created by Keshine on 4/27/2016.
 * email verification snippets from http://javatechig.com/android/edittext-validation-in-android-example
 */
public class RegisterActivity extends Activity{

    private EditText emailEditText;
    private EditText nameEditText;
    private AutoCompleteTextView titleAutoText;

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private String[] title_names;
    private long newid;
    private Boolean loading = true;
    private String newname;
    private String newemail;
    private String newtitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        preferenceEditor=preferenceSettings.edit();
        preferenceEditor.clear();
        preferenceEditor.commit();
        if (isLoggedIn())
        {
            Intent intent = new Intent(this, TreeActivity.class);
            intent.putExtra("userid", preferenceSettings.getLong("user", -1));
            intent.putExtra("loading", true);
            intent.putExtra("loadingindex", 0);
            this.startActivity (intent);
            finish();
        }
        else {
//            DBHandler dbHandler = new DBHandler(this, null, null, 1);
//            dbHandler.populateDB();
//            dbHandler.updateDB();
        }


        setContentView(R.layout.activity_register);
        emailEditText = (EditText) findViewById(R.id.register_email);
        nameEditText = (EditText) findViewById(R.id.register_name);
        titleAutoText = (AutoCompleteTextView) findViewById(R.id.register_title);
        //@todo change string array to access positions of the company
        title_names=getResources().getStringArray(R.array.job_positions);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,title_names);
        titleAutoText.setThreshold(1);
        titleAutoText.setAdapter(adapter);

        findViewById(R.id.email_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Boolean validated=true;
                final String email = emailEditText.getText().toString();
                if(email.isEmpty()){
                    emailEditText.setError("Email Required");
                }
                else if (!isValidEmail(email)) {
                    emailEditText.setError("Invalid Email");
                    validated=false;
                }

                final String name = nameEditText.getText().toString();
                if (name.isEmpty()) {
                    nameEditText.setError("Name Required");
                    validated=false;
                }
                //@todo add validator so title is constrained to values
                String title = titleAutoText.getText().toString();
                if(title.isEmpty()){
                    title = null;
                }
                if(validated){
                    newname=name;
                    newemail=email;
                    newtitle=title;
                    requestNewUser();
                }

            }
        });
    }



    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //create new account and store it in sharedpreferences?, sqllite?
    private void createAccount(){
        //code seems to continue without waiting for return on database acces.
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        dbHandler.addUser(newid);
        User newUser = new User(newid);
        newUser.setProfile(new Profile(newname,newemail,newtitle));
        dbHandler.editProfile(newUser);
        dbHandler.linkSup(newid,1);
        //uploadProfile(name, email , title);

        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceEditor.putLong("user",newUser.getUserid());
        preferenceEditor.putString("name",newname);
        preferenceEditor.putString("email",newemail);
        preferenceEditor.putString("title",newtitle);
        preferenceEditor.commit();

        Intent profileIntent = new Intent(RegisterActivity.this, TreeActivity.class);
        profileIntent.putExtra("userid", newid);
        profileIntent.putExtra("loading", true);
        profileIntent.putExtra("loadingindex", 0);
        startActivity(profileIntent);
        finish();
    }
    public String strprofiletojson(String name, String email, String title){
        Gson gson = new GsonBuilder().create();
        HashMap<String, String> map = new HashMap<String, String>();
        // Add status for each User in Hashmap
        map.put("name", name);
        map.put("email", email);
        map.put("title", title);
        return gson.toJson(map);
    }
    public long requestNewUser() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final ProgressDialog prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Creating account...");
        prgDialog.setCancelable(false);
        prgDialog.show();
        client.post("http://10.0.2.2:80/mysqllitesync/requestnewid.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, byte[]response) {
                String strresponse;
                loading=false;
                try {
                    strresponse= new String(response, "UTF-8");  // Best way to decode using "UTF-8"
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    strresponse = "[{'FUCK THIS SHIT'}]";
                }
                try{
                    JSONObject responseobj = new JSONObject(strresponse);
                    newid = responseobj.getLong("newid");
                    //newid=Long.parseLong();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),	"New User Created", Toast.LENGTH_LONG).show();
                createAccount();
            }


            @Override
            public void onFailure(int statusCode, Header[] header, byte[] response, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });
        return newid;
    }
    public void uploadProfile(String name, String email, String title) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("title", title);
        client.post("http://10.0.2.2:80/mysqllitesync/updateprofile.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] header, byte[] response) {
                try {
                    String strresponse= new String(response, "UTF-8");  // Best way to decode using "UTF-8"
                    newid= Long.parseLong(strresponse);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),	"New User Created", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onFailure(int statusCode, Header[] header, byte[] response, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });

    }

    private Boolean isLoggedIn(){
        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return preferenceSettings.contains("name");
    }
}