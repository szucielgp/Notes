package com.example.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDb extends SQLiteOpenHelper{
    
	public static final String ID = "_id";//定义为static final的其他的类可以使用且不能变动
	public static final String NAME = "note";//考虑表名跟表列
	public static final String TIME = "time";
	public static final String PATH = "path";
	public static final String VIDEO = "video";
	public static final String CONTENT = "content";
	//public String IMAGE = "image";
	public NoteDb(Context context) {
		super(context, "note", null, 1);
	}

//	@Override
//	public void onCreate(SQLiteDatabase db) {
//           String create = "create table "+ NAME+" ("+ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
//	             CONTENT + " text not null,"+ TIME + " text not null, " + PATH_IMG + " text not null,"+
//        		   PATH_VIDEO +"  text not null )";
//		  db.execSQL(create);
//		  //create table 不是create a table!
//	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + NAME + " (" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT
				+ " ," + PATH +"," + VIDEO+" ,"+ TIME + " TEXT NOT NULL)");
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
      
}
