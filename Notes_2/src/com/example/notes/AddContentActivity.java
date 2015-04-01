package com.example.notes;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

public class AddContentActivity extends Activity implements OnClickListener{
	private Button savebtn, deletebtn;
	private EditText ettext;
	private ImageView c_img;
	private VideoView v_video;
	private NoteDb notesDb;
	private SQLiteDatabase dbWriter;
	private String _flag ;
	private File imageFile,videoFile;
	private int w_screen;
	private int h_screen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_content);
		savebtn = (Button) findViewById(R.id.save);
		deletebtn = (Button) findViewById(R.id.delete);
		ettext = (EditText) findViewById(R.id.ettext);
		c_img = (ImageView) findViewById(R.id.c_img);
		v_video = (VideoView) findViewById(R.id.c_video);
		_flag = getIntent().getStringExtra("flag");
		savebtn.setOnClickListener(this);
		deletebtn.setOnClickListener(this);
		notesDb = new NoteDb(this);
		DisplayMetrics dm =getResources().getDisplayMetrics();  //获取屏幕的高度和宽度。
	    w_screen = dm.widthPixels;  
	    h_screen = dm.heightPixels; 
		dbWriter = notesDb.getWritableDatabase();
		initView();
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.save:
			addData();
			finish();
		case R.id.delete:
			finish();
		}	
		
	}
	
	public void addData(){//不用再传参数，dbwriter已经为处理数据库的参数。
		ContentValues values = new ContentValues();
		String str = ettext.getText().toString();
		values.put(NoteDb.CONTENT, str);
		values.put(NoteDb.TIME, getTime());
		values.put(NoteDb.PATH, imageFile+"");
		values.put(NoteDb.VIDEO, videoFile+"");
		dbWriter.insert(NoteDb.NAME, null, values);
		dbWriter.close();
		
	}
	
	public String getTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		Date date = new Date();
		String str = format.format(date);
		return str;
	}
	
	public void initView(){
		
		if(_flag.equals("1")){
			c_img.setVisibility(View.GONE);//View.visible是可见的
			v_video.setVisibility(View.GONE);
		//	v_video.setVisibility();
		}
		if(_flag.equals("2")){
			c_img.setVisibility(View.VISIBLE);
			v_video.setVisibility(View.GONE);	
			Intent img = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//获取系统相机
			imageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getTime()+".jpg");
			//拿到存储卡的绝对路径，且以时间来命名，使其读一无二
			Log.d("file:",imageFile+"" );
			//Environment.getExternalStorageDirectory() 拿到存储卡的路径
			//System.out.println(Environment.getExternalStorageDirectory().getAbsoluteFile());
			img.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));//将照片存起来
			startActivityForResult(img, 1);
			
		}
		if(_flag.equals("3")){
			c_img.setVisibility(View.GONE);
			v_video.setVisibility(View.VISIBLE);
			Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			videoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+getTime()+".mp4");
			video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
			startActivityForResult(video, 2);
		}
		
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//拿到结果后回传。回传是1，则是图片，回传是2，则是视频
		if (requestCode == 1) {
//			Bitmap bitmap = BitmapFactory.decodeFile(imageFile
//					.getAbsolutePath());
//			bitmap.setWidth(bitmap.getWidth()/2);
//			bitmap.setHeight(bitmap.getHeight()/2);这个方法不可用
			Bitmap bitmap = getImageThumbnail(imageFile.getAbsolutePath(), w_screen, h_screen);//因为直接获取的bitmap太大，所以要经过缩放之后才拿进来
			c_img.setImageBitmap(bitmap);
		}
		if (requestCode == 2) {
			v_video.setVideoURI(Uri.fromFile(videoFile));
			v_video.start();
		}
	}
	
	
	public Bitmap getImageThumbnail(String uri,int width, int height){
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inJustDecodeBounds = true;
		options.outWidth = 50;
        options.outHeight = options.outWidth* height/width;
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
        
        bitmap = BitmapFactory.decodeFile(uri,options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
        		ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;	
	}
}

