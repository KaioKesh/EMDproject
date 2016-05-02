package kesh.emd.marist.powertree;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Keshine on 4/24/2016.
 */
public class TreeActivity extends Activity {

    private TreeView myView;
    private TreeAnimatedView myAView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myView = new TreeView(this);
        setContentView(myView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuItem toggleSound = menu.add(0, TOGGLE_SOUND, 0, "Toggle Sound");
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.cribbage, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:

                break;
        }
        return false;
    }
}
