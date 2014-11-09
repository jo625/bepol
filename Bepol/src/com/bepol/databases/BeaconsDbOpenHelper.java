package com.bepol.databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class BeaconsDbOpenHelper {
	 
    private static final String DATABASE_NAME = "beacons.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;
    private String fileName = "beacons.csv";
 
    private class DatabaseHelper extends SQLiteOpenHelper{
 
        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Databases.CreateBeaconsDB._CREATE);
            createTableFromSource();
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+Databases.CreateBeaconsDB._TABLENAME);
            onCreate(db);
        }
    }
 
    public BeaconsDbOpenHelper(Context context){
        this.mCtx = context;
    }
 
    public BeaconsDbOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }
 
    public void close(){
        mDB.close();
    }
    
    public void createTableFromSource(){
    	try{
    		File mFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),fileName);
    		FileInputStream fIn = new FileInputStream(mFile);
    		BufferedReader mReader = new BufferedReader(new InputStreamReader(fIn, "euc-kr"));
    		String line = "";
    		String[] tokens = null;
    		while((line = mReader.readLine())!=null){
    			tokens = line.split(",");
    			if(tokens!=null && tokens.length>4){
    				insertColumn(Integer.getInteger(tokens[0]), Integer.getInteger(tokens[1]), tokens[2], Float.parseFloat(tokens[3]), Float.parseFloat(tokens[4]));
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public long insertColumn(int bmajor, int bminor, String bname, float bx, float by){
    	ContentValues values = new ContentValues();
		values.put(Databases.CreateBeaconsDB.BEACONMAJOR, bmajor);
		values.put(Databases.CreateBeaconsDB.BEACONMINOR, bminor);
		values.put(Databases.CreateBeaconsDB.BEACONNAME, bname);
		values.put(Databases.CreateBeaconsDB.BEACONX, bx);
		values.put(Databases.CreateBeaconsDB.BEACONY, by);
		return mDB.insert(Databases.CreateBeaconsDB._TABLENAME, null, values);
	}
    
    public boolean deleteColumn(String snum){
    	return mDB.delete(Databases.CreateBeaconsDB._TABLENAME, "snum='"+snum+"'", null) > 0;
    }
    
    public Cursor selectSubjects(){
    	return mDB.rawQuery("SELECT sname, prof, time1, time2, time3, snum FROM subjects", null);
    }
    
    public boolean deleteAll(){
    	return mDB.delete(Databases.CreateBeaconsDB._TABLENAME, "", null)>0;
    }
}
