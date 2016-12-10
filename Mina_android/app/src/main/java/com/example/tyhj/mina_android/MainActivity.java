package com.example.tyhj.mina_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mina.Connect;

public class MainActivity extends AppCompatActivity {
    Connect connect;
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
                //connect=Connect.getInstance("tyhj5","4444");
                //if(connect==null)
                    Log.e("失败","服务器出错2");
            }
        }).start();


        btn_sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connect==null||et_msg.getText().toString().equals("")||et_to.getText().toString().equals(""))
                    return;
                //connect.sendMsg(et_to.getText().toString(),et_msg.getText().toString(),0);
            }
        });
    }
}
