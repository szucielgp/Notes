package com.example.notes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{//适配器中不需要调用数据库只需要传一个cursor即可。
    
	private Context context ;
	private Cursor cursor ;
	private LinearLayout Layout;
	MyAdapter(Context context,Cursor cursor){//通过构造方法在new Myadapter的时候传递context，和cursor参数。
		this.context = context;
		this.cursor = cursor;
	}
	@Override
	public int getCount() {
        
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {

		
		return cursor.getPosition();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater =  LayoutInflater.from(context);
		Layout = (LinearLayout)inflater.inflate(R.layout.cell, null);
		TextView contenttv = (TextView) Layout.findViewById(R.id.cell_text);
		TextView timetv = (TextView) Layout.findViewById(R.id.cell_time);
		ImageView imgiv = (ImageView) Layout.findViewById(R.id.cell_image);
		ImageView videoiv = (ImageView) Layout.findViewById(R.id.cell_video);
		cursor.moveToPosition(position);//转到当前的位置
		String content =cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT));
		String time = cursor.getString(cursor.getColumnIndex(NoteDb.TIME));
		String uri = cursor.getString(cursor.getColumnIndex(NoteDb.PATH));//获取这个路径的时候报错
	String url = cursor.getString(cursor.getColumnIndex(NoteDb.VIDEO));
		contenttv.setText(content);
        timetv.setText(time);
      imgiv.setImageBitmap(getImageThumbnail(uri, 70, 70));//传两百进去太大了！100是实际的尺寸
        videoiv.setImageBitmap(getVideoThumbnail(url, 70, 70,
           MediaStore.Images.Thumbnails.MICRO_KIND));
		return Layout;
	}
   
	//获取缩略图，同时避免oom的问题
	public Bitmap getImageThumbnail(String uri,int width, int height){
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(uri,options);
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight /height;
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
        Log.d("option.oWidth", String.valueOf(options.outWidth));
        bitmap = BitmapFactory.decodeFile(uri,options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
        		ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;	
	}
	
	private Bitmap getVideoThumbnail(String uri,int width,int height,int kind){
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
}
