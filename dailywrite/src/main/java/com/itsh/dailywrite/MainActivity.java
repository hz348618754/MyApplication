package com.itsh.dailywrite;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
	final int MODIFY = 0;
	final int DELETE = 1;

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
		listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("请选择操作");
				menu.add(0,MODIFY,0,"修改");
				menu.add(0,DELETE,0,"删除");
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				String title = ((TextView)view.findViewById(R.id.title)).getText().toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String gettime = ((TextView)view.findViewById(R.id.time)).getText().toString();
				long time  = 0;
				try {
					time = sdf.parse(gettime).getTime()/1000;
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(MainActivity.this,ShowContent.class);
				intent.putExtra("title",title);
				intent.putExtra("time",time);
				startActivity(intent);
			}
		});

	}

	public boolean onContextItemSelected(MenuItem item){
		String title;
		AdapterView.AdapterContextMenuInfo menuInfo;
		switch (item.getItemId()){
			case MODIFY:
				menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				title = ((TextView)menuInfo.targetView.findViewById(R.id.title)).getText().toString();
				Intent intent = new Intent(MainActivity.this,ShowContent.class);
				intent.putExtra("title",title);
				startActivity(intent);
				return true;
			case DELETE:
				menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
				title = ((TextView)menuInfo.targetView.findViewById(R.id.title)).getText().toString();
				String SQL = "delete from note where title='"+title+"'";
				SQLiteDatabase sdb = dbConnect.getReadableDatabase();
				sdb.execSQL(SQL);
				reFresh();
				sdb.close();
				Toast.makeText(getApplicationContext(),"删除成功！",Toast.LENGTH_SHORT).show();
				return true;
		}
		return false;
	}

	//显示首页列表
	private void showList() {
		SQLiteDatabase sdb = dbConnect.getReadableDatabase();
		Cursor cursor = sdb.rawQuery("select _id,title,time,content from note",null);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		while (cursor.moveToNext()){
			Map<String,Object> map = new HashMap<>();
			String title = cursor.getString(cursor.getColumnIndex("title"));
			long time = cursor.getLong(cursor.getColumnIndex("time"))*1000;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("title",title);
			map.put("time",dateFormat.format(time));
			list.add(map);
		}
		sdb.close();

		adapter = new SimpleAdapter(MainActivity.this,list,R.layout.items,new String[] {"title","time"},new int[] {R.id.title,R.id.time});
		listView.setAdapter(adapter);
	}

	//刷新页面内容
	public void reFresh(){
		onCreate(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		reFresh();
	}
}
