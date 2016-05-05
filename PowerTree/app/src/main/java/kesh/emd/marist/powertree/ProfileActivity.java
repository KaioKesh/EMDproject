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

import kesh.emd.marist.powertree.AndroidDatabaseManager;
import kesh.emd.marist.powertree.UserTree.User;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences preferenceSettings;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private User usertoshow;
    private long userid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        //get userid to show
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            userid=extras.getLong("userid");
        }
        setContentView(R.layout.profile);

        //ActionBar 
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu_edit, menu);


        return true;
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        preferenceSettings =getSharedPreferences("user_info", Context.MODE_PRIVATE);
        long usercheck = preferenceSettings.getLong("user",-1);
        MenuItem register = menu.findItem(R.id.menu_edit_profile);
        if(usercheck!=getID())
        {
            register.setVisible(false);
        }
        else
        {
            register.setVisible(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_profile:
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                return true;
            case R.id.view_database:
                startActivity(new Intent(ProfileActivity.this, AndroidDatabaseManager.class));
                return true;
        }
        return false;
    }
    public long getID(){
        return this.userid;
    }
}