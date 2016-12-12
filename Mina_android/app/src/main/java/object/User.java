package object;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.example.tyhj.mina_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import mina.Connect;
import tools.Defined;

/**
 * Created by Tyhj on 2016/12/11.
 */

public class User {

    private static Connect connect=null;

    //初始化
    public static String init(Context context,String ip,int port){
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else
            connect = Connect.getInstance(ip,port);
        return getReternMsg();
    }

    //登陆
    public static String signIn(String email,String pwd,Context context){
        connect.setReternMsg(null);
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else {
            try {
                connect.signIn(email,pwd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getReternMsg();
    }

    //创建用户
    public static String signUp(Context context,String name,String eamil,String pwd){
        connect.setReternMsg(null);
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else
            connect.signUp(name,eamil,pwd);
        return getReternMsg();
    }

    //发送消息
    public static String sendMsg(String msg,String to,int type,Context context){
        connect.setReternMsg(null);
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        connect.sendMsg("singleTalk",to,msg,type);
        return getReternMsg();
    }



    private static String getReternMsg() {
        final int[] x = {1};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    x[0] =0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (connect.getReternMsg()==null&&x[0]==1) {

        }
        return connect.getReternMsg();
    }

}
