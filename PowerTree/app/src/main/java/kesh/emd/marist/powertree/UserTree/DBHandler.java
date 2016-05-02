package kesh.emd.marist.powertree.UserTree;

/**
 * Created by Keshine on 5/1/2016.
 */
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class DBHandler extends SQLiteOpenHelper {

    //database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userTree.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PROFILES = "user_profiles";

    //user attributes
    public static final String USER_ID = "user_id";
    public static final String USER_COMPANY = "company_id";

    //profile attributes
    public static final String PROFILE_NAME = "name";
    public static final String PROFILE_EMAIL = "email";
    public static final String PROFILE_TITLE = "title";
    //public static final String PROFILE_PHOTO = "photo";

    public DBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "("
                + USER_ID + " INTEGER PRIMARY KEY," + USER_COMPANY
                 + " INTEGER" + ")";
        String CREATE_PROFILES_TABLE = "CREATE TABLE " +
                TABLE_PROFILES + "("
                + USER_ID + " INTEGER PRIMARY KEY,"
                + PROFILE_NAME + " TEXT,"
                + PROFILE_EMAIL + " TEXT,"
                + PROFILE_TITLE + " TEXT,"
                //+ PROFILE_PHOTO + "TEXT, "
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PROFILES_TABLE);

    }

    public void addUser(User user) {

        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getUserid());
        values.put(USER_COMPANY, user.getCompanyid());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public void addProfile(User user) {
        Profile userprofile = user.getProfile();

        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getUserid());
        values.put(PROFILE_NAME, userprofile.getName());
        values.put(PROFILE_EMAIL, userprofile.getEmail());
        values.put(PROFILE_TITLE, userprofile.getTitle());
        //values.put(PROFILE_PHOTO, userprofile.getPhoto());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public Profile getProfile(User user) {
        String query = "Select * FROM " + TABLE_PROFILES + " WHERE " + USER_ID + " =  \"" + user.getUserid() + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String profilename;
        String profileemail;
        String profiletitle;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            profilename=cursor.getString(0);
            profileemail=cursor.getString(1);
            profiletitle=cursor.getString(2);
            cursor.close();

        } else {
            profilename="User Not Found";
            profileemail="Profile Not Found";
            profiletitle="Something Not Found";
        }

        Profile profile = new Profile(profilename,profileemail,profiletitle);

        db.close();
        return profile;
    }

    public Boolean idTaken(int userid) {
        String query = "Select * FROM " + TABLE_USERS + " WHERE " + USER_ID + " =  \"" + userid + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Boolean taken = true;
        if(cursor.moveToFirst()){
            taken = true;
        }
        else{
            taken = false;
        }
        db.close();
        return taken;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS + ", " + TABLE_PROFILES);
        onCreate(db);
    }

}