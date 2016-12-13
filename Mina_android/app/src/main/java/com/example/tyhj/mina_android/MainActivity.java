package com.example.tyhj.mina_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mina.Connect;
import object.User;

public class MainActivity extends AppCompatActivity {

    static String IP="192.168.43.18";
    static String IP2="192.168.31.215";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_sendMsg= (Button) findViewById(R.id.btn_sendMsg);
        final EditText et_msg= (EditText) findViewById(R.id.et_msg);
        final EditText et_to= (EditText) findViewById(R.id.et_to);
        new Thread(new Runnable() {
            @Override
            public void run() {

                User.init(MainActivity.this,IP2,9897);

                Log.e("注册反馈",User.signUp(MainActivity.this,"tyhj5","Tyhj5","4444")+"");

                Log.e("登录反馈",User.signIn("Tyhj5","4444",MainActivity.this)+"");

            }
        }).start();


        btn_sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_msg.getText().toString().equals("")||et_to.getText().toString().equals(""))
                    return;
                User.sendMsg(et_msg.getText().toString(),et_to.getText().toString(),0,0,MainActivity.this);
            }
        });
    }
}
