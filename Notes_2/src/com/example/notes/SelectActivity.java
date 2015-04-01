package com.example.notes;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class SelectActivity extends Activity implements OnClickListener{

	private Button delete,back;
	private TextView s_text,s_time;
	private ImageView s_image;
	private VideoView s_video;
	private NoteDb noteDbHelper;
	private SQLiteDatabase db;
	private int id;
	private String content,path,video,time;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		intiView();
	}
	@Override
	public void onClick(View v) {
        switch(v.getId()){
        case R.id.delete:
        	deleteData(id);
        	finish();
        	break;
        case R.id.back:
        	finish();
            break;
        }
	}
	
	public void deleteData(int id){
		db.delete(NoteDb.NAME, "_id = ?", new String[]{String.valueOf(id)});
		db.close();
	}
	
	public void intiView(){
		delete = (Button)findViewById(R.id.delete);
		back = (Button)findViewById(R.id.back);
		s_text = (TextView)findViewById(R.id.s_text);
		s_time = (TextView)findViewById(R.id.s_time);
		s_image = (ImageView)findViewById(R.id.s_img);
		s_video = (VideoView)findViewById(R.id.s_video);
		delete.setOnClickListener(this);
		back.setOnClickListener(this);
		noteDbHelper = new NoteDb(this);
		db = noteDbHelper.getWritableDatabase();//拿到数据库对象
		DisplayMetrics dm =getResources().getDisplayMetrics();  //获取屏幕的高度和宽度。
	   int   w_screen = dm.widthPixels;  
	   int  h_screen = dm.heightPixels; 
		Intent intent = getIntent();
		id = intent.getIntExtra(NoteDb.ID, 0);
		content = intent.getStringExtra(NoteDb.CONTENT);
		path = intent.getStringExtra(NoteDb.PATH);
		video = intent.getStringExtra(NoteDb.VIDEO);
		time = intent.getStringExtra(NoteDb.TIME);//获取从mainactivity得到的数据
		if(path.equals("null") && !s_video.equals("null")){
			s_video.setVisibility(View.VISIBLE);
			s_video.setVideoURI(Uri
					.parse(video));
			s_video.start();
		}
		if(video.equals("null")){
			s_image.setVisibility(View.VISIBLE);
		}
		if(video.equals("null") && path.equals("null")){
			s_video.setVisibility(View.GONE);
			s_image.setVisibility(View.GONE);
		}
		s_text.setText(content);
		s_time.setText(time);
		
		bitmap = getImageThumbnail(path,w_screen,h_screen);
		//bitmap = getImageThumbnail(path, 100, 100);
	//	bitmap = BitmapFactory.decodeFile(path);
		s_image.setImageBitmap(bitmap);
		
	}
	
	public Bitmap getImageThumbnail(String uri,int width, int height){
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(uri,options);
		
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight/height;
        options.inJustDecodeBounds= false;
        int be = 1;
        if(beWidth<beHeight){
        	be  = beWidth;
        }
        else{
        	be = beHeight;
        	
        }
        if(be<=0){
        	be=1;
        }
        options.inSampleSize = be;//节约内存
        options.outWidth = 200;
        options.outHeight = options.outWidth* height/width;
        bitmap = BitmapFactory.decodeFile(uri,options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
        		ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;	
	}
}
