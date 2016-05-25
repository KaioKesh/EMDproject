package kesh.emd.marist.powertree;

/**
 * Created by Keshine on 4/19/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kesh.emd.marist.powertree.Server.DBHandler;
import kesh.emd.marist.powertree.UserTree.Profile;

public class ProfileDetailFragment extends Fragment {
    private SharedPreferences preferenceSettings;
    //xml views
    private TextView profilename;
    private TextView profiletitle;
    private TextView profileemail;

    private long userid;

    public ProfileDetailFragment(){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        //setProfiletext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profiledetailsview = inflater.inflate(R.layout.profile_details, container, false);
        setProfiletext(profiledetailsview);
        return profiledetailsview;
    }

    private void setProfiletext(View v){
        preferenceSettings =this.getActivity().getSharedPreferences("user_info",Context.MODE_PRIVATE);
        userid = ((ProfileActivity)this.getActivity()).getID();
        long usercheck = preferenceSettings.getLong("user",-1);
        String name;
        String title;
        String email;
        if(userid == usercheck){
            name = preferenceSettings.getString("name","name not found");
            title = preferenceSettings.getString("title","job title not found");
            email = preferenceSettings.getString("email","email not found");
        }
        else{
            DBHandler dbHandler = new DBHandler(v.getContext(), null, null, 1);
            Profile profile = dbHandler.getProfile(userid);
            name = profile.getName();
            title = profile.getTitle();
            email = profile.getEmail();
        }

        profilename = (TextView)v.findViewById(R.id.profilename);
        profiletitle = (TextView)v.findViewById(R.id.profiletitle);
        profileemail = (TextView)v.findViewById(R.id.profileemail);



        profilename.setText(name);
        profiletitle.setText(title);
        profileemail.setText(email);
    }
}