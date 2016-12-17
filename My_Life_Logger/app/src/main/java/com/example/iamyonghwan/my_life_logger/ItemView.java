package com.example.iamyonghwan.my_life_logger;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by iamyonghwan on 2016-12-17.
 */

public class ItemView {
    public String item;
    public String time;
    public String category;

    public ItemView(){}

    public ItemView(String time , String item , String category){
        this.time = time;
        this. category = category;
        this.item = item;
    }
}
