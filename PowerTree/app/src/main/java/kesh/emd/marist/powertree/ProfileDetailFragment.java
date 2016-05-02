package kesh.emd.marist.powertree;

/**
 * Created by Keshine on 4/19/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileDetailFragment extends Fragment {
    private SharedPreferences preferenceSettings;

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

    //must be called after onAttach()
    private void setProfiletext(View v){
        TextView profilename = (TextView)v.findViewById(R.id.profilename);
        TextView profiletitle = (TextView)v.findViewById(R.id.profiletitle);
        TextView profileemail = (TextView)v.findViewById(R.id.profileemail);

        preferenceSettings =this.getActivity().getSharedPreferences("user_info",Context.MODE_PRIVATE);
        String name = preferenceSettings.getString("name","name not found");
        String title = preferenceSettings.getString("title","job title not found");
        String email = preferenceSettings.getString("email","email not found");


        profilename.setText(name);
        profiletitle.setText(title);
        profileemail.setText(email);
    }
}