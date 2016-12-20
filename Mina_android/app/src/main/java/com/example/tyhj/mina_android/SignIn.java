package com.example.tyhj.mina_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roger.match.library.MatchButton;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import myviews.OutlineBtn;
import object.User;
import object.UserInfo;
import service.MinaSocket;
import service.MinaSocket_;
import tools.Defined;
import tools.SavaDate;
import tools.SendEmail;

@EActivity(R.layout.activity_sign_in)
public class SignIn extends AppCompatActivity {

    boolean finish,finish_set,canCode;

    int count=30;

    String email, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取信息
        UserInfo user=new SavaDate(this).getUserInfo();
        //登录
        if(user!=null){
            Log.e("SignIn:","获取到保存的用户数据");
            User.userInfo=user;
            MinaSocket.signIn=true;
            startService(new Intent(this, MinaSocket_.class));
            startActivity(new Intent(SignIn.this, MainActivity_.class));
            this.finish();
        }else
            startService(new Intent(this, MinaSocket_.class));
        canCode=true;

    }

    @ViewById
    MatchButton mbtn,mbtn_set;

    @ViewById
    EditText et_psd,et_id,et_psd_set,et_id_set,et_code_set;

    @ViewById
    LinearLayout ll_signUp,ll_signIn;

    @ViewById
    OutlineBtn btn_getcode;


    @AfterViews
    void afterView(){
        mbtn_set.setText("Sign In");
        mbtn.setText("Sign Up");
        mbtn.setProgress(0.3f);
        mbtn_set.setProgress(0.3f);
    }

    @AfterTextChange({R.id.et_psd_set,R.id.et_id_set,R.id.et_code_set})
    void textChange_set(){
       if(et_psd_set.getText().toString().length()>=4&&et_id_set.getText().toString().length()>=4&&et_code_set.getText().toString().length()>=4){
           finish_set=true;
           mbtn_set.setText("Sign Up");
       }else {
           mbtn_set.setText("Sign In");
           finish_set=false;
       }
    }


    @AfterTextChange({R.id.et_psd,R.id.et_id})
    void textChange(){
        if(et_psd.getText().toString().length()>=4&&et_id.getText().toString().length()>=4){
            mbtn.setText("Sign In");
            finish=true;
        }else {
            mbtn.setText("Sign Up");
            finish=false;
        }
    }

    //获取验证码
    @Click(R.id.btn_getcode)
    void getCode(){
        if(!Defined.isEmail(et_id_set.getText().toString(),this)||!Defined.isIntenet(this)||!canCode){
            if(!Defined.isIntenet(this))
                Toast.makeText(this, "网络已断开", Toast.LENGTH_SHORT).show();
            return;
        }
        code=Defined.getCode(4);
        email=et_id_set.getText().toString();
        sendEmail();
        Toast.makeText(this, "验证码已发送至邮箱", Toast.LENGTH_SHORT).show();
        canCode=false;
        setCount();
    }

    @Click(R.id.mbtn)
    void sign(){
        if(finish) {
            signIn();
        }else {
            ll_signUp.setVisibility(View.VISIBLE);
            ll_signIn.setVisibility(View.GONE);
        }
    }


    @Click(R.id.mbtn_set)
    void signup(){
        if(finish_set) {
            if(et_id_set.getText().toString().equals(email)&&et_code_set.getText().toString().equals(code))
                signUp(email,et_psd_set.getText().toString());
            else
                Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
        }else {
            ll_signUp.setVisibility(View.GONE);
            ll_signIn.setVisibility(View.VISIBLE);
        }
    }

    //注册
    @Background
    void signUp(String email,String pwd){
        String code=User.signUp(this,email,email,pwd);
        if(code!=null&&code.equals("200")) {
            succes();
            toast("注册成功");
        }
        else
            toast(code);
    }

    //登录
    @Background
    void signIn(){
        String code=User.signIn(et_id.getText().toString(),et_psd.getText().toString(),this);
        if(code!=null&&code.equals("200"))
            sucess(et_id.getText().toString(),et_psd.getText().toString());
        else
            toast(code);
    }

    //发送邮件
    @Background
    void sendEmail(){
        SendEmail.sendEmail(email,getString(R.string.signup)+code);
    }

    //Toast
    @UiThread
    void toast(String str){
        if(str!=null)
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    //获取验证码时间限制
    @Background
    void setCount(){
        while (!canCode){
            try {
                change();
                Thread.sleep(1000);
                count--;
                if(count==0){
                    canCode=true;
                    count=30;
                    review();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //登录成功
    @UiThread
    void sucess(String id,String pwd){
        new SavaDate(this).saveUserInfo(new UserInfo(id,pwd));
        User.userInfo=new UserInfo(id,pwd);
        startActivity(new Intent(SignIn.this, MainActivity_.class));
        this.finish();
    }

    //注册成功
    @UiThread
    void succes(){
        ll_signUp.setVisibility(View.GONE);
        ll_signIn.setVisibility(View.VISIBLE);
        et_id.setText(email);
    }

    @UiThread
    void change(){
        btn_getcode.setText(""+count);
    }

    @UiThread
    void review(){
        btn_getcode.setText("获取");
    }

    @Override
    public void onBackPressed() {
        if(ll_signUp.getVisibility()==View.VISIBLE){
            ll_signUp.setVisibility(View.GONE);
            ll_signIn.setVisibility(View.VISIBLE);
        }else
        super.onBackPressed();
    }
}
