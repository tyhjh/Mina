package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import object.Picture;
import object.User;
import object.UserInfo;
import tools.Defined;

@EService
public class MinaSocket extends Service {

    static String IP2= "192.168.43.18";
    static String IP = "192.168.31.215";

    public static String actionSignIn="singleTalk";

    public static boolean signIn;

    String TAG="MinaSocket";

    boolean reconnect = true;

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
        reconnect = false;
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(getApplicationContext());
        ImageLoader.getInstance().init(configuration);
        //Log.e("MinaSocekt","onCreate执行");
        connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setPhoto();
        if(reconnect)
            reconnect();
        else {
            reconnect = true;
            //Log.e("MinaSocekt", "onStartCommand没有执行");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    //建立连接
    @Background
    void connect() {
        String code = User.init(getApplicationContext(), IP, 9897);
        //toast(code);
        if (code == null && signIn) {
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

    //退出应用后重新连接
    @Background
    void reconnect() {
        if (!User.isConnect()) {
            String code = User.init(getApplicationContext(), IP, 9897);
            //toast(code);
            if (code == null&&User.userInfo!=null) {
                //Log.e("MinaSocekt", "onStartCommand重新连接执行");
                User.signIn(User.userInfo.getId(), User.userInfo.getPwd());
                //Log.e("MinaSocekt验证", User.userInfo.getId()+"");
            }
        }
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

}
