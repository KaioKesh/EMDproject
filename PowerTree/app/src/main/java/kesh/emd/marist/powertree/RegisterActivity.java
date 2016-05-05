package kesh.emd.marist.powertree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
//        preferenceEditor=preferenceSettings.edit();
//        preferenceEditor.clear();
//        preferenceEditor.commit();
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
                    long extra = createAccount(name,email,title);
                    Intent profileIntent = new Intent(RegisterActivity.this, TreeActivity.class);
                    profileIntent.putExtra("userid", extra);
                    profileIntent.putExtra("loading", true);
                    profileIntent.putExtra("loadingindex", 0);
                    startActivity(profileIntent);
                    finish();
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
    private long createAccount(String name, String email, String title){

        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        long id = dbHandler.addUser();
        User newUser = new User(id);
        newUser.setProfile(new Profile(name,email,title));
        dbHandler.editProfile(newUser);
        dbHandler.linkSup(id,1);

        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceEditor.putLong("user",newUser.getUserid());
        preferenceEditor.putString("name",name);
        preferenceEditor.putString("email",email);
        preferenceEditor.putString("title",title);
        preferenceEditor.commit();

        return id;
    }

    private Boolean isLoggedIn(){
        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return preferenceSettings.contains("name");
    }
}