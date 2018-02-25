package com.itsh.dailywrite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyContent extends Activity {

    private DBConnect dbConnect;
    private EditText et_title,et_content;
    private Button btn_save,btn_back;
    private String oldTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_content);

        et_content = (EditText) findViewById(R.id.et_content);
        et_title = (EditText)findViewById(R.id.et_title);
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_save = (Button)findViewById(R.id.btn_save);
        getDBContent();

        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ModifyContent.this.finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String mTitle = et_title.getText().toString();
                String mContent = et_content.getText().toString();
                long mTime = (System.currentTimeMillis())/1000L;

                ContentValues cv = new ContentValues();
                cv.put("title", mTitle);
                cv.put("time", mTime);
                cv.put("content", mContent);
                DBConnect dbConnect = new DBConnect(ModifyContent.this,"DailyWrite.db",null,1);
                SQLiteDatabase db = dbConnect.getReadableDatabase();
                db.update("note",cv,"title=?",new String[] {oldTitle});
                Toast.makeText(getApplicationContext(),"保存成功！",Toast.LENGTH_LONG).show();
                dbConnect.close();
                ModifyContent.this.finish();

            }
        });
    }

    //读取并显示当前内容
    public void getDBContent() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String SQL = "select * from note where title='"+title+"'";
        dbConnect = new DBConnect(this,"DailyWrite.db",null,1);
        SQLiteDatabase db = dbConnect.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL,null);
        while (cursor.moveToNext()){
            String mTitle = cursor.getString(cursor.getColumnIndex("title"));
            String mContent = cursor.getString(cursor.getColumnIndex("content"));
            et_title.setText(mTitle);
            et_content.setText(mContent);
        }
        cursor.close();
        oldTitle = title;
    }

}
