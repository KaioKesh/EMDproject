package kesh.emd.marist.powertree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (isLoggedIn())
        {
            Intent intent = new Intent(this, ProfileActivity.class);
            this.startActivity (intent);
            this.finishActivity (0);
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
                if (!isValidEmail(email)) {
                    emailEditText.setError("Invalid Email");
                    validated=false;
                }

                final String name = nameEditText.getText().toString();
                if (!isValidName(name)) {
                    nameEditText.setError("Invalid Password");
                    validated=false;
                }
                //@todo add validator so title is constrained to values
                String title = titleAutoText.getText().toString();
                if(title.isEmpty()){
                    title = null;
                }
                if(validated){
                    createAccount(name,email,title);
                    startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
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

    // validating password with retype password
    private boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z]+");
    }

    //create new account and store it in sharedpreferences?, sqllite?
    private void createAccount(String name, String email, String title){
        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceEditor.putString("name",name);
        preferenceEditor.putString("email",email);
        preferenceEditor.putString("title",title);
        preferenceEditor.commit();
    }

    private Boolean isLoggedIn(){
        preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return preferenceSettings.contains("name");
    }
}