package com.example.tyhj.mina_android;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;
import com.tyhj.myfist_2016_6_29.MyTime;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import adpter.MessageAdpter;
import adpter.PictureAdpter;
import mina.getJson;
import myinterface.ExpendImage;
import myinterface.sendPicture;
import object.LinkMan;
import object.Messge;
import object.Picture;
import object.User;
import service.MinaSocket;
import tools.AudioRecoderUtils;
import tools.Defined;
import tools.PopupWindowFactory;
import tools.SavaDate;
import tools.StatusBarUtil;

import static android.R.attr.y;
import static android.content.Intent.ACTION_GET_CONTENT;
import static fragement.MyMenuFragment.CROP_PHOTO;
import static fragement.MyMenuFragment.PICK_PHOTO;
import static fragement.MyMenuFragment.TAKE_PHOTO;

@EActivity(R.layout.activity_send_message)
public class SendMessage extends AppCompatActivity implements sendPicture, ExpendImage {

    LinkMan linkMan;
    List<Picture> pictures;
    List<Picture> sendPicture;
    List<Messge> messges;
    PictureAdpter pictureAdpter;
    MessageAdpter messageAdpter;
    InputMethodManager imm;
    boolean change;
    Animation big, small, addpicture, overadd, expend, noexpend, saveup, savedown;
    String date;
    String path = Environment.getExternalStorageDirectory() + "/ASchollMsg";
    boolean isBigPicture = false;
    Uri imageUri;
    ContentResolver contentResolver;
    float y=0;
    int time_Long;
    PopupWindowFactory mPop;
    AudioRecoderUtils mAudioRecoderUtils;
    long time=0;
    ImageView mImageView;
    TextView mTextView;

    MsgBoradCastReceiver msgBoradCastReceiver;

    @ViewById
    LinearLayout ll_add;

    @ViewById
    Button btn_album, btn_camera, btn_send_picture;

    @ViewById
    RecyclerView rcly_picture, lv_msg;

    @ViewById
    CheckBox ckb_big;

    @ViewById
    LinearLayout ll_bg;

    @ViewById
    ImageView iv_back, iv_heagImage, iv_add;

    @ViewById
    TextView tv_name;

    @ViewById
    ImageView iv_sound;

    @ViewById
    EditText et_text_send;

    @ViewById
    CardView cd_send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getContentResolver();
        getWindow().setBackgroundDrawableResource(R.mipmap.chat_bg);
        linkMan = (LinkMan) this.getIntent().getSerializableExtra("man");
        messges = linkMan.getMessges();

        StatusBarUtil.setColor(this, Color.parseColor("#00000000"));
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initAnimation();
    }

    //初始化动画
    private void initAnimation() {
        addpicture = AnimationUtils.loadAnimation(this, R.anim.addpicture);
        overadd = AnimationUtils.loadAnimation(this, R.anim.addpictureover);
        big = AnimationUtils.loadAnimation(this, R.anim.change_big);
        small = AnimationUtils.loadAnimation(this, R.anim.change_small);
        expend = AnimationUtils.loadAnimation(this, R.anim.expend);
        noexpend = AnimationUtils.loadAnimation(this, R.anim.noexpend);
        saveup = AnimationUtils.loadAnimation(this, R.anim.saveimage_up);
        savedown = AnimationUtils.loadAnimation(this, R.anim.saveimage_down);
        small.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (ll_add.getVisibility() == View.VISIBLE)
                    return;
                if (change) {
                    iv_sound.setImageResource(R.drawable.ic_mic_gray_24dp);
                } else {
                    iv_sound.setImageResource(R.drawable.ic_send_24dp);
                }
                change = !change;
                iv_sound.startAnimation(big);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @AfterViews
    void AfterView() {
        initView();
        signBroadCast();
        sendVioce();
    }

    @Click(R.id.btn_send_picture)
    void sendPictures(){
        if(sendPicture!=null&&sendPicture.size()>0){
            for(int i=0;i<sendPicture.size();i++) {
                getDate();
                JSONObject json = null;
                try {
                    json = new JSONObject(getJson.getMsg("null", 1, 0, MinaSocket.actionSignIn, linkMan.getId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Messge messge = new Messge(json);
                //设置本地图片位置
                messge.setImagePath(sendPicture.get(i).getPath_origin());
                updateView(messge);
                //保存图片到服务器
                savaImage(sendPicture.get(i).getPath_origin(), messges.size());
            }
        }
    }

    //发送语音
    private void sendVioce() {
        final View view = View.inflate(this, R.layout.dialog_recordbutton_alert_dialog, null);
        mImageView = (ImageView) view.findViewById(R.id.zeffect_recordbutton_dialog_imageview);
        mTextView = (TextView) view.findViewById(R.id.zeffect_recordbutton_dialog_time_tv);
        mPop = new PopupWindowFactory(this,view);
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动，下面有讲解
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(Defined.getDate(time));
                time_Long=(int)(time/1000);
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                //发送语音
                JSONObject json = null;
                Defined.muteAudioFocus(SendMessage.this,false);
                try {
                    json = new JSONObject(getJson.getMsg("null", 2, time_Long, getString(R.string.singleTalk), linkMan.getId()));
                    Messge messge = new Messge(json);
                    messge.setSoundPath(filePath);
                    updateView(messge);
                    sendVoice(filePath,messges.size()-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mTextView.setText("00:00");
            }
        });

        iv_sound.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                String text=et_text_send.getText().toString();
                if(change){
                    JSONObject json = null;
                    if(text.equals(""))
                        return false;
                    try {
                        json = new JSONObject(getJson.getMsg(text, 0, 0, MinaSocket.actionSignIn, linkMan.getId()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Messge messge = new Messge(json);
                    updateView(messge);
                    sendMsg(json.toString());
                    et_text_send.setText("");
                    return false;
                } else {//录音
                    Defined.muteAudioFocus(SendMessage.this,true);
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            y=event.getRawY();
                            time = System.currentTimeMillis();
                            mPop.showAtLocation(ll_bg, Gravity.CENTER,0,0);
                            try {
                                mAudioRecoderUtils.startRecord();
                            }catch (RuntimeException e){
                                e.printStackTrace();
                                Snackbar.make(iv_sound,"先允许调用系统录音权限",Snackbar.LENGTH_SHORT).show();
                            }
                            iv_sound.startAnimation(expend);
                            break;
                        case MotionEvent.ACTION_UP:
                            if(System.currentTimeMillis()-time<1000){
                                Snackbar.make(iv_sound,"录音时间过短，请重试",Snackbar.LENGTH_SHORT).show();
                                mAudioRecoderUtils.cancelRecord();
                                mPop.dismiss();
                                iv_sound.startAnimation(noexpend);
                                break;
                            }else if(y-event.getRawY()>300){
                                Snackbar.make(iv_sound,"已取消发送语音",Snackbar.LENGTH_SHORT).show();
                                mAudioRecoderUtils.cancelRecord();
                                mPop.dismiss();
                                iv_sound.startAnimation(noexpend);
                                break;
                            }else {
                                try{
                                    mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Snackbar.make(iv_sound,"先允许调用系统录音权限",Snackbar.LENGTH_SHORT).show();
                                }
                                mPop.dismiss();
                                iv_sound.startAnimation(noexpend);
                                break;
                            }
                        case MotionEvent.ACTION_CANCEL:
                            mAudioRecoderUtils.cancelRecord(); //取消录音（不保存录音文件）
                            mPop.dismiss();
                            iv_sound.startAnimation(noexpend);
                            break;
                    }

                }
                return false;
            }
        });
    }

    @Click(R.id.iv_back)
    void back() {
        this.finish();
    }

    //添加图片
    @Click(R.id.iv_add)
    void add() {
        imm.hideSoftInputFromWindow(et_text_send.getWindowToken(), 0);
        if (ll_add.getVisibility() != View.VISIBLE) {
            et_text_send.setInputType(InputType.TYPE_NULL);
            ll_add.setVisibility(View.VISIBLE);
            iv_add.startAnimation(addpicture);
        } else {
            et_text_send.setInputType(InputType.TYPE_CLASS_TEXT);
            ll_add.setVisibility(View.GONE);
            iv_add.startAnimation(overadd);
        }

    }

    //相册
    @Click(R.id.btn_album)
    void album() {
        getDate();
        File file = new File(path, date);
        Intent intent = new Intent(ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, PICK_PHOTO);
    }

    //相机
    @Click(R.id.btn_camera)
    void camera() {
        getDate();
        File file = new File(path, date);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, TAKE_PHOTO);
    }

    //图片大小
    @CheckedChange(R.id.ckb_big)
    void check(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            isBigPicture = true;
        else
            isBigPicture = false;
        Log.e("Sendmessage", "Checkable改变了");
    }

    //随机获取文件名字
    public void getDate() {
        MyTime myTime = new MyTime();
        date = myTime.getYear() + myTime.getMonth_() + myTime.getDays() +
                myTime.getWeek_() + myTime.getHour() + myTime.getMinute() +
                myTime.getSecond() + linkMan.getId() + ".JPEG";
    }

    @TextChange(R.id.et_text_send)
    void AfterTextChanged(TextView hello) {
        if (!hello.getText().toString().trim().equals("") && !change) {
            iv_sound.startAnimation(small);
        } else if (hello.getText().toString().trim().equals("") && change) {
            iv_sound.startAnimation(small);
        }
    }

    //初始化布局
    private void initView() {

        //联系人头像，名字
        tv_name.setText(linkMan.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv_heagImage.setClipToOutline(true);
            iv_heagImage.setOutlineProvider(Defined.getOutline(true, 10, 0));
        }
        Picasso.with(this).load(linkMan.getHeadImage()).error(R.mipmap.defult).into(iv_heagImage);

        //初始化图片列表
        pictures = User.getPhoto();
        pictureAdpter = new PictureAdpter(this, pictures);
        rcly_picture.setAdapter(pictureAdpter);
        rcly_picture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcly_picture.setItemAnimator(new DefaultItemAnimator());
        pictureAdpter.setInterface(this);

        //初始化消息列表
        messageAdpter = new MessageAdpter(messges, this, linkMan.getHeadImage());
        messageAdpter.setExpendImage(this);
        lv_msg.setAdapter(messageAdpter);
        lv_msg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lv_msg.setItemAnimator(new DefaultItemAnimator());

    }

    //查看并保存图片
    @Override
    public void callBack(final Messge msg) {
        Dialog dialog = new Dialog(this, R.style.Dialog_Fullscreen);
        dialog.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialogessayimage, null);
        dialog.setContentView(view);
        ImageView ivEssayExpend = (ImageView) view.findViewById(R.id.ivEssayExpend);
        final Button saveImage = (Button) view.findViewById(R.id.btSavaEssayIv);
        saveImage.setVisibility(View.INVISIBLE);
        Picasso.with(this).load(msg.getContent()).into(ivEssayExpend);
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        dialog.getWindow().setAttributes(p);     //设置生效
        dialog.show();

        ivEssayExpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveImage.getVisibility() == View.GONE || saveImage.getVisibility() == View.INVISIBLE)
                    saveImage.startAnimation(saveup);
                else
                    saveImage.startAnimation(savedown);
            }
        });
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Defined.isIntenet(SendMessage.this))
                    return;
                if(msg.getType()==1){
                    Toast.makeText(SendMessage.this,"已保存到"+getString(R.string.savaphotopath),Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Defined.savaFile(msg.getContent(), User.userInfo.getName() + Defined.getTime() + getString(R.string.imageFormat), handler, SendMessage.this);
                    }
                }).start();
            }
        });
    }

    //获取选取的图片
    @Override
    public void sendPicture(List<Picture> pictures) {
        if (pictures.size() > 0) {
            btn_send_picture.setTextColor(Color.parseColor("#2ca6cb"));
            btn_send_picture.setClickable(true);
        } else {
            btn_send_picture.setTextColor(Color.GRAY);
            btn_send_picture.setClickable(false);
        }
        sendPicture = pictures;
        Log.e("SendMessage选取图片", sendPicture.size() + "");
    }

    //剪裁图片
    public void cropPhoto(Uri imageUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_PHOTO);
    }

    //获取本地图片并更新界面
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //这是从相机返回的数据
            case TAKE_PHOTO:
                if (resultCode == this.RESULT_OK) {
                    File file = new File(path, date);
                    cropPhoto(Uri.fromFile(file));
                }
                break;
            //这是从相册返回的数据
            case PICK_PHOTO:
                if (resultCode == this.RESULT_OK) {
                    if (data != null) {
                        imageUri = data.getData();
                    }
                    String path_pre = Defined.getFilePathFromContentUri(imageUri, contentResolver);
                    File newFile = new File(path, date);
                    Log.e("SendMesseg", path_pre);
                    try {
                        //复制图片
                        Defined.copyFile(new File(path_pre), newFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File file = new File(path, date);
                    cropPhoto(Uri.fromFile(file));
                }
                break;
            //剪裁图片返回数据,就是原来的文件
            case CROP_PHOTO:
                //获取到的就是new File或fileName
                if (resultCode == this.RESULT_OK) {
                    final String fileName = path + "/" + date;
                    File newFile = new File(path, date);
                    if (!isBigPicture) {
                        Defined.ImgCompress(fileName, newFile, 200);
                        Log.e("SendMessge", "剪裁了图片");
                    }
                    try {
                        JSONObject json = new JSONObject(getJson.getMsg("null", 1, 0, MinaSocket.actionSignIn, linkMan.getId()));
                        Messge messge = new Messge(json);
                        //设置本地图片位置
                        messge.setImagePath(fileName);
                        updateView(messge);
                        //保存图片到服务器
                        savaImage(fileName,messges.size());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    //保存图片到服务器
    private void savaImage(final String fileName, final int size) {
        try {
            if (!Defined.isIntenet(SendMessage.this))
                return;
            AVObject avObject = new AVObject("Image");
            final AVFile file = AVFile.withAbsoluteLocalPath("chat.JPEG", fileName);
            avObject.put("image", file);
            avObject.put("name", linkMan.getId() + date);
            avObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        getImageUrl(linkMan.getId() + date,size);
                    } else {
                        Toast.makeText(SendMessage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取图片URl并发送消息
    public void getImageUrl(String name, final int size) {
        AVQuery<AVObject> query = new AVQuery<>("Image");
        query.whereEqualTo("name", name);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // object 就是符合条件的第一个 AVObject
                if (e != null || avObject == null)
                    return;
                String chatImageUrl = avObject.getAVFile("image").getUrl();
                //发送消息
                if (chatImageUrl != null) {
                    try {
                        messges.get(size-1).update(chatImageUrl);
                        sendMsg(messges.get(size-1).getMsg().toString());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    //上传发送语音
    private void sendVoice(final String path, final int size) {
        try {
            final String RECORD_NAME=Defined.getTimeName();
            AVFile avFile=AVFile.withAbsoluteLocalPath("record.amr",path);
            AVObject avObject = new AVObject("Record");
            avObject.put("record", avFile);
            avObject.put("user",User.userInfo.getId());
            avObject.put("name",RECORD_NAME);
            avObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        File file1=new File(path);
                        if(file1.exists())
                            file1.delete();
                        getRecordUrl(RECORD_NAME,size);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获语音片URl并发送消息
    private void getRecordUrl(String name, final int size) {
        AVQuery<AVObject> query = new AVQuery<>("Record");
        query.whereEqualTo("name", name);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // object 就是符合条件的第一个 AVObject
                if(e!=null||avObject==null)
                    return;
                String RecordUrl = avObject.getAVFile("record").getUrl();
                if (RecordUrl != null){
                    messges.get(size).update(RecordUrl);
                    sendMsg(messges.get(size).getMsg().toString());
                }
            }
        });
    }

    //更新界面
    public void updateView(Messge messge) {
        messges.add(messge);
        messageAdpter.notifyItemInserted(messges.size() - 1);
    }

    //发送消息
    @Background
    void sendMsg(String str) {
        toLasted();
        User.sendMsg(str, SendMessage.this);
        }

    @UiThread
    void toLasted(){
        lv_msg.scrollToPosition(messges.size()-1);
    }

    //广播
    class MsgBoradCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Messge messge= (Messge) intent.getSerializableExtra("msg_single");
            if(messge.getFrom().equals(linkMan.getId())){
                abortBroadcast();
                updateView(messge);
            }
        }
    }

    //注册广播
    private void signBroadCast() {
        msgBoradCastReceiver=new MsgBoradCastReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("boradcast.action.GETMESSAGE");
        intentFilter.setPriority(1000);
        registerReceiver(msgBoradCastReceiver,intentFilter);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(SendMessage.this, "已保存至"+getString(R.string.savaphotopath), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        new SavaDate(this).saveMsg(messges,linkMan.getId());
        unregisterReceiver(msgBoradCastReceiver);
        super.onDestroy();
    }

}
