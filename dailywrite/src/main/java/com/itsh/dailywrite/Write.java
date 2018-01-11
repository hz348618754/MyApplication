package com.itsh.dailywrite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class Write extends Activity {
	private EditText et_title,et_content;
	private Button btn_clear,btn_save;

	public void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);

		et_content = (EditText) findViewById(R.id.et_content);
		et_title = (EditText)findViewById(R.id.et_title);
		btn_clear = (Button)findViewById(R.id.btn_clear);
		btn_save = (Button)findViewById(R.id.btn_save);

		//设置清除按钮事件
		btn_clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				et_title.setText("");
				et_content.setText("");
				et_title.setFocusable(true);
			}
		});

		btn_save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String title = et_title.getText().toString();
				String content = et_content.getText().toString();
				long time = (System.currentTimeMillis())/1000L;

					ContentValues cv = new ContentValues();
					cv.put("title", title);
					cv.put("time", time);
					cv.put("content", content);
					DBConnect dbConnect = new DBConnect(Write.this,"DailyWrite.db",null,1);
					SQLiteDatabase sd = dbConnect.getReadableDatabase();
					String SQL = "insert OR IGNORE into note(title,time,content) values(?,?,?)";
					sd.execSQL(SQL,new Object[] {title,time,content});
					Toast.makeText(getApplicationContext(),"保存成功！",Toast.LENGTH_SHORT).show();
					sd.close();
//					Intent intent = new Intent(Write.this,MainActivity.class);
//					startActivity(intent);
					Write.this.finish();
				}

		});


	}

}
