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
        this.userprofile = new Profile(username,useremail);
    }
    public void createProfile(String username,String useremail, String usertitle){
        this.userprofile = new Profile(username,useremail,usertitle);
    }
    public int getUserid(){
        return this.accountid;
    }
    public int getCompanyid(){
        return this.companyid;
    }
    public void setProfile(Profile newprofile){
        this.userprofile = newprofile;
    }
    public Profile getProfile(){
        return this.userprofile;
    }

    public Boolean equals(User compareuser){
        if(this.accountid==compareuser.accountid){
            return true;
        }
        else{
            return false;
        }
    }
    public void setCompanyid(int newcompany){
        this.companyid=newcompany;
    }


}
