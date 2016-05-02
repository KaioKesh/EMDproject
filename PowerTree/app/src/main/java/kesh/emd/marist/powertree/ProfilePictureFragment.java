package kesh.emd.marist.powertree;

/**
 * Created by Keshine on 4/19/2016.
 */

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ProfilePictureFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 0;
    private ImageView profileImage;

    public ProfilePictureFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilepictureview = inflater.inflate(R.layout.profile_picture, container, false);
        setHasOptionsMenu(true);
        /*
        Button buttonLoadImage = (Button)profilepictureview.findViewById(R.id.pictureButton);
        profileImage = (ImageView)profilepictureview.findViewById(R.id.profileImage);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }});
            */
        return profilepictureview;
    }

}
