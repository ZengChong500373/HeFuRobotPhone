package com.hefu.robotphone.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.hefu.robotphone.R;

import hefu.robotphone.sdk.utlis.SystemInfoUtil;


/**
 * Created by zc on 2018/4/9.
 */

public class PlayMediaActivity extends AppCompatActivity {
    String base = "speakTTS" + " " + SystemInfoUtil.getMac() + " 4" + " "+ " " + "888"+ " "  ;
    EditText ed;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);
        ed=findViewById(R.id.ed);
    }
    public void send(View view){
        String str=base+ed.getText().toString();
        MainActivity.socket.robotCmd(str);
    }

    public void one(View view) {
        MainActivity.socket.robotCmd(base+"第一段1");
    }

    public void two(View view) {
        MainActivity.socket.robotCmd(base+"第一段2");
    }

    public void three(View view) {
        MainActivity.socket.robotCmd(base+"第一段3");
    }

    public void four(View view) {
        MainActivity.socket.robotCmd(base+"第二段1");
    }

    public void five(View view) {
        MainActivity.socket.robotCmd(base+"第二段2");
    }

    public void stop(View view) {
        MainActivity.socket.robotCmd(base+"over");
    }
}
