package com.example.iamyonghwan.my_life_logger;

/**
 * Created by iamyonghwan on 2016-12-17.
 */

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db1) {
        // 새로운 테이블 생성
        /* 이름은 EVENTMANAGE이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, content 문자열 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db1.execSQL("CREATE TABLE EVENTMANAGE (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, content TEXT, create_at TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db1, int oldVersion, int newVersion) {

    }

    public void update(String item, String content) {
        SQLiteDatabase db1 = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db1.execSQL("UPDATE EVENTMANAGE SET content=" + content + " WHERE item='" + item + "';");
        db1.close();
    }

    public void insert(String create_at, String item, String content) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db1 = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db1.execSQL("INSERT INTO EVENTMANAGE VALUES(null, '" + item + "', '" + content + "', '" + create_at + "');");
        Log.e("insert", "성공");
        db1.close();
    }


    public ArrayList<ItemView> getView() {
        ItemView event;
        SQLiteDatabase db1 = getReadableDatabase();
        ArrayList<ItemView> array = new ArrayList<>();

        Cursor cursor = db1.rawQuery("SELECT * FROM EVENTMANAGE", null);
        while (cursor.moveToNext()) {
            event = new ItemView(cursor.getString(3), cursor.getString(1), cursor.getString(2));
            array.add(event);
        }
        return array;
    }
}
