package kesh.emd.marist.powertree;

/**
 * Created by Keshine on 4/19/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class EditProfilePictureFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 0;
    private ImageView profileImage;

    public EditProfilePictureFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilepictureview = inflater.inflate(R.layout.edit_profile_picture, container, false);
        Button buttonLoadImage = (Button)profilepictureview.findViewById(R.id.pictureButton);
        profileImage = (ImageView)profilepictureview.findViewById(R.id.profileImage);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }});

        return profilepictureview;
    }

}
