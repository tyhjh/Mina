package com.example.tyhj.mina_android;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import object.LinkMan;
import tools.Defined;
import tools.StatusBarUtil;

@EActivity(R.layout.activity_send_message)
public class SendMessage extends AppCompatActivity {

    LinkMan linkMan;

    InputMethodManager imm;

    boolean change;

    Animation big,small,addpicture,overadd,expend,noexpend,saveup,savedown;

    @ViewById
    LinearLayout ll_add;

    @ViewById
    Button btn_album,btn_camera,btn_send_picture;

    @ViewById
    RecyclerView rcly_picture;

    @ViewById
    CheckBox ckb_big;

    @ViewById
    ImageView iv_back,iv_heagImage,iv_add;

    @ViewById
    TextView tv_name;

    @ViewById
    ImageView iv_sound;

    @ViewById
    EditText et_text_send;

    @ViewById
    CardView cd_send;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.mipmap.chat_bg);
        linkMan= (LinkMan) this.getIntent().getSerializableExtra("man");
        StatusBarUtil.setColor(this, Color.parseColor("#00000000"));
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initAnimation();
    }

    private void initAnimation() {
        addpicture= AnimationUtils.loadAnimation(this,R.anim.addpicture);
        overadd=AnimationUtils.loadAnimation(this,R.anim.addpictureover);
        big= AnimationUtils.loadAnimation(this,R.anim.change_big);
        small=AnimationUtils.loadAnimation(this,R.anim.change_small);
        expend=AnimationUtils.loadAnimation(this,R.anim.expend);
        noexpend=AnimationUtils.loadAnimation(this,R.anim.noexpend);
        saveup=AnimationUtils.loadAnimation(this,R.anim.saveimage_up);
        savedown=AnimationUtils.loadAnimation(this,R.anim.saveimage_down);
        small.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(ll_add.getVisibility()== View.VISIBLE)
                    return;
                if(change){
                    iv_sound.setImageResource(R.drawable.ic_mic_gray_24dp);
                }else {
                    iv_sound.setImageResource(R.drawable.ic_send_24dp);
                }
                change=!change;
                iv_sound.startAnimation(big);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @AfterViews
    void AfterView(){
        initView();
    }


    @Click(R.id.iv_back)
    void back(){
        this.finish();
    }

    @Click(R.id.iv_add)
    void add(){
        imm.hideSoftInputFromWindow(et_text_send.getWindowToken(), 0);
        //isOpen若返回true，则表示输入法打开
        if(ll_add.getVisibility()!=View.VISIBLE){
            ll_add.setVisibility(View.VISIBLE);
            iv_add.startAnimation(addpicture);
            et_text_send.setInputType(InputType.TYPE_NULL);
        }else {
            ll_add.setVisibility(View.GONE);
            iv_add.startAnimation(overadd);
            et_text_send.setInputType(InputType.TYPE_CLASS_TEXT);
        }

    }

    @TextChange(R.id.et_text_send)
    void AfterTextChanged(TextView hello) {
        if(!hello.getText().toString().trim().equals("")&&!change){
            iv_sound.startAnimation(small);
        }else if(hello.getText().toString().trim().equals("")&&change) {
            iv_sound.startAnimation(small);
        }
    }

    private void initView() {
        tv_name.setText(linkMan.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv_heagImage.setClipToOutline(true);
            iv_heagImage.setOutlineProvider(Defined.getOutline(true,10,0));
        }
        Picasso.with(this).load(linkMan.getHeadImage()).error(R.mipmap.defult).into(iv_heagImage);

    }


}
