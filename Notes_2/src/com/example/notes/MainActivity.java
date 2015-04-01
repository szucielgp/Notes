package com.example.notes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnClickListener {//������Ϊÿ�����ü����¼�
     
	public NoteDb dbHelper;
	public SQLiteDatabase db;
	public Button text, image, video;
    public ListView listView;
    public MyAdapter adapter;
    public Cursor cursor;
    public Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		
	}

	
	public void  initView(){  //��ʼ����������Ϊ�����ܽ��г�ʼ���������г�ʼ��
		dbHelper = new NoteDb(this); 
		db = dbHelper.getReadableDatabase();
		text = (Button)findViewById(R.id.text);
		image = (Button)findViewById(R.id.img); //������ڸ�xml����Ķ�Ӧ���ˣ�����˵ȡ���ֵ�ʱ��һ��Ҫע���Ӧ
		video = (Button)findViewById(R.id.video);
		listView = (ListView)findViewById(R.id.list);
		
		text.setOnClickListener(this);
		image.setOnClickListener(this);
		video.setOnClickListener(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				cursor.moveToPosition(position);
				Intent intent = new Intent(MainActivity.this,SelectActivity.class);
				intent.putExtra(NoteDb.ID, cursor.getInt(cursor.getColumnIndex(NoteDb.ID)));
				intent.putExtra(NoteDb.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
				intent.putExtra(NoteDb.PATH, cursor.getString(cursor.getColumnIndex(NoteDb.PATH)));
				intent.putExtra(NoteDb.VIDEO, cursor.getString(cursor.getColumnIndex(NoteDb.VIDEO)));
				intent.putExtra(NoteDb.TIME, cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
				startActivity(intent);		
			}
		});
	}
	
	@Override
		public void onClick(View v) {
		   intent = new Intent(this,AddContentActivity.class);
			switch(v.getId()){
			case R.id.text:
				intent.putExtra("flag", "1");//
				startActivity(intent);
				break;

			case R.id.img:
				intent.putExtra("flag", "2");//
				startActivity(intent);
				break;

			case R.id.video:
				intent.putExtra("flag", "3");//
				startActivity(intent);
				break;
			}
			
		}


	@Override
	protected void onResume() {//�ָ�activity��ʱ�򱻻ص�����ѯ���еĽ��Ϊ��������������
		super.onResume();
		selectDb();
	}
	
	public void selectDb(){
		cursor = db.query(NoteDb.NAME, null, null, null, null, null, null);
	//	db.close();//ÿ���������ݿ�Ҫ�ر���
		//cursor.moveToFirst();
		adapter = new MyAdapter(this,cursor);
		listView.setAdapter(adapter);
	}
}
