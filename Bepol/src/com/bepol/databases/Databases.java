package com.bepol.databases;

import android.provider.BaseColumns;

/* DB 쿼리  */

public final class Databases {
	public static final class CreatePoisDB implements BaseColumns{
		public static final String POINAME = "pname";
//		public static final String ID = "id";
        public static final String POIX = "px";
        public static final String POIY = "py";
        public static final String _TABLENAME = "POIS";
        public static final String _CREATE = 
            "create table "+_TABLENAME+"("
                    +_ID+" integer primary key autoincrement, "
                    +POINAME+" text not null unique, " 
                    +POIX+" real not null , "
                    +POIY+" real not null );";
    }
	
	public static final class CreateBeaconsDB implements BaseColumns{
        public static final String BEACONMAJOR = "bmajor";
        public static final String BEACONMINOR = "bminor";
        public static final String BEACONNAME = "bname";
        public static final String BEACONX = "bx";
        public static final String BEACONY = "by";
        public static final String _TABLENAME = "BEACONS";
        public static final String _CREATE = 
            "create table "+_TABLENAME+"("
                    +_ID+" integer primary key autoincrement, "
                    +BEACONMAJOR+" integer not null ,"
                    +BEACONMINOR+" integer not null ,"
                    +BEACONNAME+" text not null , "
                    +BEACONX+" real not null , "
                    +BEACONY+" real not null );";
    }
	
	public static final class CreateEdgesDB implements BaseColumns{
        public static final String EDGENAME = "Ename";
        public static final String STARTNUM = "Startnum";
        public static final String STOPNUM = "Stopnum";
        public static final String WEIGHT = "weight";
        public static final String _TABLENAME = "EDGES";
        public static final String _CREATE = 
            "create table "+_TABLENAME+"("
                    +_ID+" integer primary key autoincrement, "
                    +EDGENAME+" text not null unique , "
                    +STARTNUM+" real not null , "
                    +STOPNUM+" real not null , "
                    +WEIGHT+" real not null );";
    }
}
