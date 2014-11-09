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

/* edges 파일 읽어내기 */

public class EdgesDbOpenHelper {
	 
    private static final String DATABASE_NAME = "edges.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private EdgesDatabaseHelper mDBHelper;
    private Context mCtx;
    private String fileName = "edges.csv";
    
   private class EdgesDatabaseHelper extends SQLiteOpenHelper{
       
    	public EdgesDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
    		super(context, name, factory, version);
    	}
 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	db.execSQL(Databases.CreateEdgesDB._CREATE);
	    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+Databases.CreateEdgesDB._TABLENAME);
            onCreate(db);
        }
	}
   
   public EdgesDbOpenHelper(Context context){
	   	this.mCtx = context;
   	}
	public EdgesDbOpenHelper open() throws SQLException{
        mDBHelper = new EdgesDatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
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
    		//File mFile = new File("")
    		FileInputStream fIn = new FileInputStream(mFile);
    		BufferedReader mReader = new BufferedReader(new InputStreamReader(fIn, "euc-kr"));
    		String line = "";
    		String[] tokens = null;
    		while((line = mReader.readLine())!=null){
    			tokens = line.split(",");
    			if(tokens!=null && tokens.length>3){
    				insertColumn(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Float.parseFloat(tokens[3]));
    				Log.d("insert", "insert");
    			}
    			
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public long insertColumn(String name, int start, int stop, float weight){
		ContentValues values = new ContentValues();
		values.put(Databases.CreateEdgesDB.EDGENAME, name);
		values.put(Databases.CreateEdgesDB.STARTNUM, start);
		values.put(Databases.CreateEdgesDB.STOPNUM, stop);
		values.put(Databases.CreateEdgesDB.WEIGHT, weight);
		return mDB.insert(Databases.CreateEdgesDB._TABLENAME, null, values);
	}
    
    public boolean deleteColumn(String num){
    	return mDB.delete(Databases.CreateEdgesDB._TABLENAME, "num="+num, null) > 0;
    }
    
    public Cursor searchColumn(){
    	return mDB.rawQuery("SELECT Ename, Startnum, Stopnum, weight FROM edges", null);
    }
    
    public Cursor searchEdge(String name){
    	return mDB.rawQuery("SELECT Ename, Startnum, Stopnum, weight FROM edges WHERE Ename LIKE '%"+name+"%'", null);
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
    	return mDB.delete(Databases.CreateEdgesDB._TABLENAME, "", null)>0;
    }
}
