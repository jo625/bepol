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
import android.util.Log;

public class PoisDbOpenHelper {
	 
    private static final String DATABASE_NAME = "pois.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private PoisDatabaseHelper mDBHelper;
    private Context mCtx;
    private String fileName = "pois.csv";
    
   private class PoisDatabaseHelper extends SQLiteOpenHelper{
       
    	public PoisDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
    		super(context, name, factory, version);
    	}
 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	db.execSQL(Databases.CreatePoisDB._CREATE);
	    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+Databases.CreatePoisDB._TABLENAME);
            onCreate(db);
        }
	}
   
   public PoisDbOpenHelper(Context context){
	   	this.mCtx = context;
   	}
	public PoisDbOpenHelper open() throws SQLException{
        mDBHelper = new PoisDatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        createTableFromSource();
        return this;
    }
     
    public void close(){
        mDB.close();
    }
    
    public void createTableFromSource(){
    	try{
    		//File mFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),fileName);
    		File mFile = new File("/sdcard/"+fileName);
    		
    		FileInputStream fIn = new FileInputStream(mFile);
    		BufferedReader mReader = new BufferedReader(new InputStreamReader(fIn, "euc-kr"));
    		String line = "";
    		String[] tokens = null;
    		while((line = mReader.readLine())!=null){
    			tokens = line.split(",");
    			if(tokens!=null && tokens.length>2){
    				insertColumn(tokens[0],  Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
    				Log.d("pois insert", "pois");
    			}
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public long insertColumn(String name, float px, float py){
    	//Log.d("되냐?", name);
		ContentValues values = new ContentValues();
		values.put(Databases.CreatePoisDB.POINAME, name);
		//values.put(Databases.CreatePoisDB._ID);
		values.put(Databases.CreatePoisDB.POIX, px);
		values.put(Databases.CreatePoisDB.POIY, py);
		return mDB.insert(Databases.CreatePoisDB._TABLENAME, null, values);
	}
    
    public boolean deleteColumn(String num){
    	return mDB.delete(Databases.CreatePoisDB._TABLENAME, "num="+num, null) > 0;
    }
    
    public Cursor searchColumn(){
    	return mDB.rawQuery("SELECT pname, px, py, _ID FROM pois", null);
    	//return mDB.rawQuery("SELECT * FROM pois", null);
        
    }
    
    public Cursor searchPoi(String name){
    	return mDB.rawQuery("SELECT pname, px, py, _ID FROM pois WHERE pname LIKE '%"+name+"%'", null);
    }
    
    public boolean isLogined(){
    	
   		Cursor uResult = searchColumn();
   		int count = 0;
   		uResult.moveToFirst();
   		while (!uResult.isAfterLast()){
   		   count++;
   		   uResult.moveToNext();
   		}
   		uResult.close();
   		
    	return (count>0);
    }
    
    public boolean deleteAll(){
    	return mDB.delete(Databases.CreatePoisDB._TABLENAME, "", null)>0;
    }
}
