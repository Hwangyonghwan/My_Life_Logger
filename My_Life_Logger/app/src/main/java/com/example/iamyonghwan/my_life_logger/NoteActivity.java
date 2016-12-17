package com.example.iamyonghwan.my_life_logger;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by iamyonghwan on 2016-11-24.
 */



public class NoteActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private ImageView imgview;
    TextView textView1;
    TextView textView2;
    EditText editext;
    Button buttonstart;
    Button buttonpause;
    Button buttonstop;

    RadioGroup Rg;


    final static int Init =0;
    final static int Run =1;
    final static int Pause =2;

    int cur_Status = Init; //현재의 상태를 저장할변수를 초기화함.
    int myCount=1;
    long basetime;
    long pausetime;
    long elapsedtime;
    String time;
    String start_date;
    String end_date;

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        imgview = (ImageView) findViewById(R.id.imageView);
        ImageButton buttonCamera = (ImageButton) findViewById(R.id.btn_take_camera);
        ImageButton buttonGallery = (ImageButton) findViewById(R.id.btn_select_gallery);
        Button buttonsave = (Button) findViewById(R.id.button8);
        buttonstart = (Button) findViewById(R.id.button9) ;
        buttonpause = (Button) findViewById(R.id.button10);
        buttonstop = (Button) findViewById(R.id.button11) ;

        editext = (EditText) findViewById(R.id.editText);
        textView1 = (TextView) findViewById(R.id.text3);
        textView2= (TextView) findViewById(R.id.text4);
        Rg = (RadioGroup) findViewById(R.id.radioGroup);


        helper = new MySQLiteOpenHelper(NoteActivity.this, "customer_1.db", null, 1);

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton Rd = (RadioButton) findViewById(Rg.getCheckedRadioButtonId());
                final String category = Rd.getText().toString();
                helper.insert(category, editext.getText().toString(), time);
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // 카메라 호출
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                // 이미지 잘라내기 위한 크기
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);

                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                // Gallery 호출
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // 잘라내기 셋팅
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);
                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        try {
            if (requestCode == PICK_FROM_CAMERA) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    imgview.setImageBitmap(photo);
                }
            }
            if (requestCode == PICK_FROM_GALLERY) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    imgview.setImageBitmap(photo);
                }
            }
        } catch(NullPointerException e) {
        }

    }

    public String getDateString()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }

    public void myOnClick(View v){
        start_date = getDateString();
        switch(v.getId()){
            case R.id.button9: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
                textView1.setText(start_date) ;
                switch(cur_Status){
                    case Init:
                        basetime = SystemClock.elapsedRealtime();
                        System.out.println(basetime);
                        myTimer.sendEmptyMessage(0);
                        buttonpause.setEnabled(true);
                        buttonstop.setEnabled(true);
                        buttonstart.setEnabled(false);//기록버튼 활성
                        cur_Status = Run; //현재상태를 런상태로 변경
                        break;
                    case Run: //멈춤으로 바뀐 시작버튼 눌렀을때 ->stop버튼 눌렀을때
                        myTimer.removeMessages(0); //핸들러 메세지 제거
                        pausetime = SystemClock.elapsedRealtime();
                        buttonpause.setEnabled(true);
                        buttonstop.setEnabled(true);
                        buttonstart.setEnabled(false);
                        cur_Status = Pause;
                        break;
                    case Pause:
                        end_date = getDateString();
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        basetime += (now- pausetime);
                        buttonpause.setEnabled(true);
                        buttonstop.setEnabled(true);
                        buttonstart.setEnabled(false);
                        cur_Status = Run;
                        break;
                }
                break;
            case R.id.button10:
                myTimer.removeMessages(0); //핸들러 메세지 제거
                pausetime = SystemClock.elapsedRealtime();
                buttonstart.setEnabled(true);
                cur_Status = Pause;
                break;
            case R.id.button11:
                textView2.setText(end_date);
                myTimer.removeMessages(0); //핸들러 메세지 제거
                pausetime = SystemClock.elapsedRealtime();
                textView2.setText(getDateString());
                buttonstart.setEnabled(true);
                cur_Status = Init;
                break;
        }
    }

    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            textView2.setText(getTimeOut());

            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
            myTimer.sendEmptyMessage(0);
        }
    };
    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut(){
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        long outTime = now - basetime;
        elapsedtime = outTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
        time = Long.toString(outTime);
        return easy_outTime;
    }

}



