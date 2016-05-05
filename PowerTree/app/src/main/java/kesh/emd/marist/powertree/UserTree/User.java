package kesh.emd.marist.powertree.UserTree;

/**
 * Created by Speck on 4/3/2016.
 */
//@todo look up Google account sync
public class User {
    private long accountid;
    private long companyid;
    private Profile userprofile;
    public User(long newaccountid){
        //if ![accounts].contains(newaccountid)
        this.accountid = newaccountid;
        this.companyid=-1;
        this.userprofile=new Profile();
    }
    public long getUserid(){
        return this.accountid;
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
    public void setCompanyid(long newcompany){
        this.companyid=newcompany;
    }
    public long getCompanyid(){
        return this.companyid;
    }


}
