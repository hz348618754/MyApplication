package com.itsh.dailywrite;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ShowContent extends Activity {
	private DBConnect dbConnect;
	private EditText et_title,et_content;
	private Button btn_back;

	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);

		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_back = (Button)findViewById(R.id.btn_back);

		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		String SQL = "select * from note where title='"+title+"'";
		dbConnect = new DBConnect(ShowContent.this,"DailyWrite.db",null,1);
		SQLiteDatabase db = dbConnect.getReadableDatabase();
		Cursor cursor = db.rawQuery(SQL,null);
		while (cursor.moveToNext()){
			String show_title = cursor.getString(cursor.getColumnIndex("title"));
			String show_content = cursor.getString(cursor.getColumnIndex("content"));
			et_title.setText(show_title);
			et_content.setText(show_content);
		}
		db.close();

		btn_back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent1 = new Intent(ShowContent.this,MainActivity.class);
				startActivity(intent1);
				ShowContent.this.finish();
			}
		});
	}

}
