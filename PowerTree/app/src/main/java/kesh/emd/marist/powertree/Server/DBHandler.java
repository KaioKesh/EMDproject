package kesh.emd.marist.powertree.Server;

/**
 * Created by Keshine on 5/1/2016.
 */
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kesh.emd.marist.powertree.UserTree.Node;
import kesh.emd.marist.powertree.UserTree.Profile;
import kesh.emd.marist.powertree.UserTree.User;

public class DBHandler extends SQLiteOpenHelper {

    //database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userTree.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PROFILES = "user_profiles";

    //user attributes
    public static final String USER_ID = "user_id";
    public static final String USER_COMPANY = "company_id";
    public static final String USER_SUPERIOR = "superior_id";

    //profile attributes
    public static final String PROFILE_NAME = "name";
    public static final String PROFILE_EMAIL = "email";
    public static final String PROFILE_TITLE = "title";
    //public static final String PROFILE_PHOTO = "photo";
    //
    ArrayList<Long> userids= new ArrayList<>();
    ArrayList<Long> superiorids= new ArrayList<>();

    public DBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "("
                + USER_ID + " INTEGER PRIMARY KEY," + USER_COMPANY
                 + " INTEGER," + USER_SUPERIOR
                + " INTEGER"+")";
        String CREATE_PROFILES_TABLE = "CREATE TABLE " +
                TABLE_PROFILES + "("
                + USER_ID + " INTEGER PRIMARY KEY,"
                + PROFILE_NAME + " TEXT,"
                + PROFILE_EMAIL + " TEXT,"
                + PROFILE_TITLE + " TEXT"
                //+ PROFILE_PHOTO + "TEXT, "
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PROFILES_TABLE);
    }

    public long addUser() {
        ContentValues uservalues = new ContentValues();
        uservalues.put(USER_COMPANY, -1);
        uservalues.put(USER_SUPERIOR, -1);

        ContentValues profilevalues = new ContentValues();
        profilevalues.put(PROFILE_NAME, "create name");
        profilevalues.put(PROFILE_EMAIL, "create email");
        profilevalues.put(PROFILE_TITLE, "choose title");

        SQLiteDatabase db = this.getWritableDatabase();
        long rowid = db.insertOrThrow(TABLE_USERS, null, uservalues);
        long rowidcheck = db.insertOrThrow(TABLE_PROFILES,null,profilevalues);
        if(rowid!=rowidcheck){
            Log.d("userinsert","rows not matching up");
        }
        db.close();
        return rowid;
    }
    public void addUser(long id) {
        ContentValues uservalues = new ContentValues();
        uservalues.put(USER_ID, id);
        uservalues.put(USER_COMPANY, -1);
        uservalues.put(USER_SUPERIOR, -1);

        ContentValues profilevalues = new ContentValues();
        profilevalues.put(USER_ID, id);
        profilevalues.put(PROFILE_NAME, "create name");
        profilevalues.put(PROFILE_EMAIL, "create email");
        profilevalues.put(PROFILE_TITLE, "choose title");

        SQLiteDatabase db = this.getWritableDatabase();
        long rowid = db.insertOrThrow(TABLE_USERS, null, uservalues);
        long rowidcheck = db.insertOrThrow(TABLE_PROFILES,null,profilevalues);
        if(rowid!=rowidcheck){
            Log.d("userinsert","rows not matching up");
        }
        db.close();
    }
    public void insertUser(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues uservalues = new ContentValues();
        uservalues.put(USER_ID, String.valueOf(queryValues.get("user_id")));
        uservalues.put(USER_COMPANY, String.valueOf(queryValues.get("company_id")));
        uservalues.put(USER_SUPERIOR, String.valueOf(queryValues.get("superior_id")));
        database.insertOrThrow(TABLE_USERS, null, uservalues);

        ContentValues profilevalues = new ContentValues();
        profilevalues.put(USER_ID, String.valueOf(queryValues.get("user_id")));
        profilevalues.put(PROFILE_NAME, queryValues.get("name"));
        profilevalues.put(PROFILE_EMAIL, queryValues.get("email"));
        profilevalues.put(PROFILE_TITLE, queryValues.get("title"));
        database.insertOrThrow(TABLE_PROFILES, null, profilevalues);
        database.close();

    }
    public void linkCompany(User user,long companyid) {

        ContentValues values = new ContentValues();
        values.put(USER_COMPANY, companyid);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_USERS, values, USER_ID+"="+user.getUserid(), null);
        db.close();
    }
    public void linkSup(long userid,long superiorid) {
        ContentValues values = new ContentValues();
        values.put(USER_SUPERIOR, superiorid);

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_USERS, values, USER_ID+"="+userid, null);
        db.close();
    }

    public void linkUsers(Node usernode) {
        ContentValues values = new ContentValues();
        List<Node<User>> children = usernode.getChildren();
        String[] childIds = new String[children.size()];
        for(int i=0;i<children.size();i++){
            childIds[i] = String.valueOf(children.get(i).getUser().getUserid());
        }
        User parent = (User) usernode.getUser();
        values.put(USER_SUPERIOR, parent.getUserid());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_USERS, values, USER_ID+"=", childIds);
        db.close();
    }

    public Node populateTree(){
        getallUsers();
        long topsuperior = superiorids.indexOf((long) -2);
        if(topsuperior==-1){
            Log.d("treecreation","root parent not found");
        }
        User companyhead = new User(topsuperior);
        Profile headprofile = getProfile(topsuperior);
        companyhead.setProfile(headprofile);
        //create tree that app will use
        Node treetopopulate = new Node(companyhead);
        populate(treetopopulate);
        return treetopopulate;
    }
    private void getallUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select "+USER_ID+ ", "+USER_SUPERIOR+" FROM "+ TABLE_USERS;
        //USER_ID (0) USER_SUPERIOR (1)
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        userids.clear();
        superiorids.clear();
        int i=0;
        while (!cursor.isAfterLast()) {
            userids.add(cursor.getLong(0));
            superiorids.add(cursor.getLong(1));
            i++;
            cursor.moveToNext();
        }
        db.close();
    }
    //@todo function request new user has been set to put default superior as 1, find way to link user to placeholder user maybe when created
    public ArrayList<Long> superiorchain(long userid){
        getallUsers();
        long userindex = userids.indexOf(userid);

        ArrayList<Long> chain = new ArrayList<>();
        while(superiorids.get((int)userindex)!=-2){
            long superiorindex =superiorids.get((int)userindex);
            chain.add(superiorindex);
            userindex = userids.indexOf(superiorindex);
        }
        return chain;
    }

    private void populate(Node root){
        long rootid = ((User)root.getUser()).getUserid();
        if(superiorids.contains(rootid)){
            ArrayList<Integer> check = subordinatesearch(rootid);
            for(Integer x:check) {
                User childuser = new User(userids.get(x));
                Node child = new Node(childuser);
                root.addChild(child);
                populate(child);
            }
        }
    }
    public void deleteUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.close();
    }
    public void deleteProfiles(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, null, null);
        db.close();
    }
    public Node getUser(long userid){
        getallUsers();
        Node root = new Node(new User(userid));
        if(superiorids.contains(userid)){
            ArrayList<Integer> check = subordinatesearch(userid);
            for(Integer x:check) {
                User childuser = new User(userids.get(x));
                Node child = new Node(childuser);
                root.addChild(child);
            }
        }
        return root;
    }
    public ArrayList<Integer> subordinatesearch(long id){
        if(superiorids.contains((Long) id)) {
            ArrayList<Integer> subs = new ArrayList<>();
            for (int j = 0; j < superiorids.size(); j++) {
                if(superiorids.get(j)==id){
                    subs.add(j);
                }
            }
            return subs;
        }
        else{
            return null;
        }
    }

    public void editProfile(User user) {

        ContentValues values = new ContentValues();
        Profile userprofile = user.getProfile();
        values.put(PROFILE_NAME, userprofile.getName());
        values.put(PROFILE_EMAIL, userprofile.getEmail());
        values.put(PROFILE_TITLE, userprofile.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PROFILES, values, USER_ID+"="+user.getUserid(), null);

        db.close();
    }

    public Profile getProfile(long userid) {
        String query = "Select * FROM " + TABLE_PROFILES + " WHERE " + USER_ID + " =  \"" + userid + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String profilename;
        String profileemail;
        String profiletitle;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            profilename=cursor.getString(1);
            profileemail=cursor.getString(2);
            profiletitle=cursor.getString(3);
            cursor.close();

        } else {
            profilename="User Not Found";
            profileemail="Profile Not Found";
            profiletitle="Something Not Found";
            cursor.close();
        }

        Profile profile = new Profile(profilename,profileemail,profiletitle);

        db.close();
        return profile;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS + ", " + TABLE_PROFILES);
        onCreate(db);
    }

    public void populateDB() {
        User newuserk = new User(addUser());
        newuserk.setProfile(new Profile("Keshine O'Young","oyoungk10@gmail.com","Android Engineer"));
        editProfile(newuserk);
        User newuserko = new User(addUser());
        newuserko.setProfile(new Profile("Joe Schmoe","jschmoe@gmail.com","Android Engineer"));
        editProfile(newuserko);
        for(int i =0;i<10;i++){
            User newuser = new User(addUser());
            newuser.setProfile(new Profile("john"+i,"smith"+i+"@gmail.com","Android Engineer"));
            editProfile(newuser);
        }
    }
    public void updateDB() {
        linkSup(1,-2);
        linkSup(2,1);
        linkSup(3,1);
        linkSup(4,2);
        linkSup(5,2);
        linkSup(6,2);
        linkSup(7,3);
        linkSup(8,3);
        linkSup(9,3);
        linkSup(10,3);
        linkSup(11,4);
        linkSup(12,9);
    }
    //DB manager helper function
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

}