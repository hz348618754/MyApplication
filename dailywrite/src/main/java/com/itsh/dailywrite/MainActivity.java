package com.itsh.dailywrite;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

	private ListView listView;
	private Button btn_add;
	private DBConnect dbConnect;
	private SimpleAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.listview);
		btn_add = (Button) findViewById(R.id.btn_add);
		dbConnect = new DBConnect(MainActivity.this,"DailyWrite.db",null,1);

		showList();

		btn_add.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,Write.class);
				startActivity(intent);
			}
		});
	}

	//显示首页列表
	private void showList() {
		SQLiteDatabase sdb = dbConnect.getReadableDatabase();
		Cursor cursor = sdb.rawQuery("select _id,title,time,content from note",null);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		while (cursor.moveToNext()){
			Map<String,Object> map = new HashMap<>();
			String title = cursor.getString(cursor.getColumnIndex("title"));
			long time = cursor.getLong(cursor.getColumnIndex("time"));
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String content = cursor.getString(cursor.getColumnIndex("content"));
			map.put("title",title);
			map.put("time",dateFormat.format(time));
			list.add(map);
		}
		cursor.close();

		adapter = new SimpleAdapter(MainActivity.this,list,R.layout.items,new String[] {"title","time"},new int[] {R.id.title,R.id.content});
		listView.setAdapter(adapter);
	}
}
