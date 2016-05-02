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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileDetailFragment extends Fragment {

    private SharedPreferences preferenceSettings;

    public EditProfileDetailFragment(){

    }
    public void onAttach(Context context){
        super.onAttach(context);
        //setProfiletext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View editprofiledetailsview = inflater.inflate(R.layout.edit_profile_details, container, false);
        setProfiletext(editprofiledetailsview);
        return editprofiledetailsview;
    }

    private void setProfiletext(View v){
        EditText profilename = (EditText)v.findViewById(R.id.edit_profilename);
        AutoCompleteTextView profiletitle = (AutoCompleteTextView) v.findViewById(R.id.edit_profiletitle);
        EditText profileemail = (EditText)v.findViewById(R.id.edit_profileemail);

        preferenceSettings =this.getActivity().getSharedPreferences("user_info",Context.MODE_PRIVATE);
        String name = preferenceSettings.getString("name","name not found");
        String email = preferenceSettings.getString("email","email not found");
        String title = preferenceSettings.getString("title","job title not found");

        profilename.setText(name);
        profileemail.setText(email);
        profiletitle.setText(title);


        //@todo change string array to access positions of the company
        String[] title_names=getResources().getStringArray(R.array.job_positions);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_dropdown_item_1line,title_names);
        profiletitle.setThreshold(1);
        profiletitle.setAdapter(adapter);
    }

}