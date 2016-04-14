package kesh.emd.marist.powertree.UserTree;

/**
 * Created by Speck on 4/3/2016.
 */
//@todo look up Google account sync
public class User {
    private int accountid;
    private int companyid;
    private Profile userprofile;
    public User(int newaccountid){
        //if ![accounts].contains(newaccountid)
        accountid = newaccountid;
    }
    public void createProfile(String username,String useremail){
        userprofile = new Profile(username,useremail);
    }

}
