package kesh.emd.marist.powertree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import kesh.emd.marist.powertree.UserTree.DBHandler;
import kesh.emd.marist.powertree.UserTree.Node;
import kesh.emd.marist.powertree.UserTree.User;

/**
 * Created by Keshine on 4/24/2016.
 * displays current selected user and displays their subordinates in horizontal scroll view
 * horizontal scroll view apparently can't handle images well, find alternative for future.
 */
public class TreeActivity extends Activity {

    //private TreeView2 myView;
    private TreeAnimatedView myAView;
    private Node treetodraw;
    private long userid;
    private long loadid;
    private Boolean loading;
    private int loadingindex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_scroll);
        //myView = new TreeView2(this);
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            userid = extras.getLong("userid");
            loading = extras.getBoolean("loading");
            loadingindex = extras.getInt("loadingindex");
        }
        else {
            Log.d("treeloading","bundle not found");
        }
        if(!loading){
            DBHandler db = new DBHandler(this, null, null, 1);
            treetodraw = db.getUser(userid);
        }
        else{
            DBHandler db = new DBHandler(this, null, null, 1);
            ArrayList<Long> chain = db.superiorchain(userid);
            if(loadingindex==chain.size()){
                loading = false;
                treetodraw = db.getUser(userid);
            }
            else{
                loadingindex++;
                loadid = chain.get(chain.size()-loadingindex);
                treetodraw = db.getUser(loadid);
            }
        }
        ImageButton parentButton = (ImageButton) findViewById(R.id.nodeparentImage) ;
        parentButton.setImageResource(R.drawable.edit_profile);
        parentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                if(loading){
                    intent.putExtra("userid", loadid);
                }
                else{
                    intent.putExtra("userid", userid);
                }
                startActivity (intent);
            }
        });

        List<Node<User>> children = treetodraw.getChildren();
        for(Node<User>child: children) {
            final long newid = child.getUser().getUserid();
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.child_image, null);

            // fill in any details dynamically here
            ImageButton childButton = (ImageButton) v.findViewById(R.id.nodechildrenImage);
            childButton.setImageResource(R.drawable.profilesilouhette);
            childButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TreeActivity.class);
                    intent.putExtra("userid", newid);
                    intent.putExtra("loading", false);
                    startActivity (intent);
                }
            });

            // insert into main view
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.scrollviewlinearlayout);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if(loading){
            Intent intent = new Intent(this, TreeActivity.class);
            intent.putExtra("userid", userid);
            intent.putExtra("loading", loading);
            intent.putExtra("loadingindex", loadingindex);
            startActivity (intent);
        }
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
