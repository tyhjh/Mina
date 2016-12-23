package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.tyhj.mina_android.MainActivity_;
import com.example.tyhj.mina_android.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tyhj.myfist_2016_6_29.MyTime;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.UiThread;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mina.Connect;
import mina.GetCode;
import myinterface.SendBordCast;
import object.Messge;
import object.Picture;
import object.User;
import object.UserInfo;
import tools.ActivityCollector;
import tools.Defined;
import tools.SavaDate;

@EService
public class MinaSocket extends Service implements SendBordCast{

    static String IP2="192.168.43.18";
    static String IP= "192.168.31.215";

    public static String actionSignIn="singleTalk";

    static NotificationManager mNotificationManager;

    boolean isReconnect=true;

    String TAG="MinaSocket";

    public MinaSocket() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isReconnect=false;
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(getApplicationContext());
        ImageLoader.getInstance().init(configuration);
        Log.e("MinaSocekt","onCreate执行");
        connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand","onStartCommand执行了");
        if(!User.isConnect()&&isReconnect)
            connect();
        else
            isReconnect=true;
        setPhoto();
        return super.onStartCommand(intent, flags, startId);
    }

    //建立连接
    @Background
    void connect() {
        String code = User.init(getApplicationContext(), IP, 9897,this);
        toast(code);
        if (code == null&&User.userInfo!=null) {
            if (Defined.isIntenet(getApplicationContext()))
                User.signIn(User.userInfo.getId(), User.userInfo.getPwd());
        }
    }

    //反馈
    @UiThread
    void toast(String string) {
        if (string != null)
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Background
    void setPhoto(){
        ArrayList<HashMap<String, String>> photos=Defined.getAllPictures(getApplicationContext());
        List<Picture> pictures=new ArrayList<Picture>();
        if(photos!=null) {
            for (int i = photos.size()-1; i >=0; i--) {
                String little = photos.get(i).get("little");
                String origin = photos.get(i).get("origin");
                if (little != null && origin.length()>10) {
                    //Log.e(TAG+"setPhoto","little:"+little+"origin"+origin);
                    pictures.add(new Picture(origin, little));
                }
            }
        }
        User.setPhoto(pictures);
    }

    //发送广播
    @Override
    public void sendBordcast(Messge messge) {
        Intent intent = new Intent("boradcast.action.GETMESSAGE");
        intent.putExtra("msg_single", messge);
        sendOrderedBroadcast(intent,null);
        //后台时候
        if(!ActivityCollector.isActivityExist(MainActivity_.class)){
            notificationBar(messge.getFrom(),messge.getContent(),1);
            User.upDateView(getApplicationContext(),messge);
        }
    }

    //通知栏配置
    private void notificationBar(String name, String text, int count) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notify);
        mRemoteViews.setImageViewResource(R.id.iv_headImage, R.mipmap.defult);
        mRemoteViews.setTextViewText(R.id.from, "\t\tFrom：" + name);
        mRemoteViews.setTextViewText(R.id.text, "\t\t"+text);
        mRemoteViews.setTextViewText(R.id.time, new MyTime().getHour() + ":" + new MyTime().getSecond());
        mRemoteViews.setTextViewText(R.id.count, count + "");
        Intent intent = new Intent(getApplicationContext(),MainActivity_.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        /*mBuilder.setContentTitle(name);
        mBuilder.setContentText(text);
        mBuilder.setTicker("收到来自"+name+"的消息");
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.defult));*/
        mBuilder.setContent(mRemoteViews);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_circle_24dp);
        Notification notification = mBuilder.build();
        mNotificationManager.notify(1, notification);
    }



}
