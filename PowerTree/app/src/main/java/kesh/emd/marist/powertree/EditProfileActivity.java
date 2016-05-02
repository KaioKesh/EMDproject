package kesh.emd.marist.powertree;

/**
 * Created by Keshine on 4/20/2016.
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ViewSwitcher;

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
                //database save

                //sharedPreferences save
                preferenceSettings = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                preferenceEditor = preferenceSettings.edit();
                preferenceEditor.putString("name",name);
                preferenceEditor.putString("email", email);
                preferenceEditor.putString("title", title);
                preferenceEditor.commit();

                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                return true;
        }
        return false;
    }
}