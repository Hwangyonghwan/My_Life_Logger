package com.example.iamyonghwan.my_life_logger;

/**
 * Created by iamyonghwan on 2016-11-24.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoriesActivity extends AppCompatActivity {

    MySQLiteOpenHelper helper;
    Eventadapter adapter;
    ArrayList<ItemView> ViewArray2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histories);

        helper = new MySQLiteOpenHelper(HistoriesActivity.this,
                "customer_1.db",
                null,
                1);
        ViewArray2 = helper.getView();

        adapter = new Eventadapter(this, R.layout.bordlist_content, ViewArray2);
        ListView listview2 = (ListView) findViewById(R.id.listView) ;
        listview2.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview2.setAdapter(adapter);

    }

}

class Eventadapter extends BaseAdapter {
    Context con;
    LayoutInflater inflacter;
    ArrayList<ItemView> arD;
    int layout;

    public Eventadapter(Context context, int alayout, ArrayList<ItemView> aard){
        con = context;
        layout = alayout;
        arD = aard;
        inflacter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arD.size();
    }

    @Override
    public Object getItem(int i) {
        return arD.get(i).time;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            convertView = inflacter.inflate(layout, parent, false);
        }
        TextView type = (TextView) convertView.findViewById(R.id.textView7);
        type.setText(arD.get(position).item);

        TextView time= (TextView) convertView.findViewById(R.id.textView6);
        time.setText(arD.get(position).time);

        TextView event = (TextView) convertView.findViewById(R.id.textView8);
        long outTime = Long.parseLong(arD.get(position).category);
        String Time = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
        event.setText(Time);

        return convertView;
    }
}

