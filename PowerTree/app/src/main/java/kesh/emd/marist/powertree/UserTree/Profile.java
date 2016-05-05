package kesh.emd.marist.powertree.UserTree;

/**
 * Created by Speck on 4/3/2016.
 */

import android.graphics.Bitmap;

public class Profile {
    private String name;
    private String title; //job title/position
    private String email;
    //@todo store photos on device and only save the filepath
    private String photofilepath;
    protected Bitmap userphoto;

    public Profile(){
    this.name = "Name was not set";
    this.email = "Email was not set";
    this.title = "Title was not set";
    this.photofilepath = "";
    }
    public Profile(String inname, String inemail){
        this.name = inname;
        this.email = inemail;
        this.title = "Title was not set";
        this.photofilepath = "";
    }
    public Profile(String inname, String inemail,String intitle){
        this.name = inname;
        this.email = inemail;
        this.title = intitle;
        this.photofilepath = "";
    }

    public void setPhoto(Bitmap profilephoto) {
        this.userphoto = profilephoto;
    }

    public void setPhoto(String filepath) {
        this.photofilepath = filepath;
    }
    public String getPhoto() {
        return this.photofilepath;
    }

    public void setName(String newname) {
        this.name = newname;
    }
    public void setTitle(String newtitle) {
        this.title = newtitle;
    }
    public void setEmail(String newemail) {
        this.email = newemail;
    }
    public String getName() {
        return this.name;
    }
    public String getTitle() {
        return this.title;
    }
    public String getEmail() {
        return this.email;
    }
}
