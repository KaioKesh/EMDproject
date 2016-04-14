package entmobdev.marist.speck.powertree.UserTree;

/**
 * Created by Speck on 4/3/2016.
 */

import android.graphics.Bitmap;

public class Profile {
    private String name;
    private String title; //job title/position
    private String email;
    protected Bitmap userphoto;

    public Profile(String inname, String inemail){
        this.name = inname;
        this.email = inemail;
    }

    public void setPhoto(Bitmap profilephoto) {
        this.userphoto = profilephoto;
    }

    public void setTitle(String newtitle) {
        this.title = newtitle;
    }
}
