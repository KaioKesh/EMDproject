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

                Intent saveintent = new Intent(this, ProfileActivity.class);
                saveintent.putExtra("userid", userid);
                this.startActivity (saveintent);
                return true;
        }
        return false;
    }
}