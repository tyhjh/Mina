package object;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.example.tyhj.mina_android.R;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mina.Connect;
import mina.getJson;
import tools.Defined;
import tools.SavaDate;

import static android.content.ContentValues.TAG;

/**
 * Created by Tyhj on 2016/12/11.
 */

public class User {

    public static UserInfo userInfo;

    private static Connect connect=null;

    private static List<Picture> photo;

    //初始化
    public static String init(Context context, String ip, int port){
        Connect.setReternMsg(null);
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else
            connect = Connect.getInstance(ip,port);
        return getReternMsg();
    }

    //登陆
    public static String signIn(String email,String pwd,Context context){
        Connect.setReternMsg(null);
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

    //登陆
    public static String signIn(String email,String pwd){
        Connect.setReternMsg(null);
            try {
                connect.signIn(email,pwd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return getReternMsg();
    }

    //创建用户
    public static String signUp(Context context,String name,String eamil,String pwd){
        Connect.setReternMsg(null);
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else
            connect.signUp(name,eamil,pwd);
        return getReternMsg();
    }

    //发送消息
    public static String sendMsg(String msg,Context context){
        Connect.setReternMsg(null);
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);

        connect.sendMsg(msg);
        return getReternMsg();
    }

    //退出
    public static void logOut(Context context){
        connect.logOut();
        new SavaDate(context).deleteData();
    }

    //获取返回值b
    private static String getReternMsg() {
        long time=System.currentTimeMillis();
        while (Connect.getReternMsg()==null) {
            if(System.currentTimeMillis()-time>2000)
                return null;
        }
        return Connect.getReternMsg();
    }

    //判断是否连接了
    public static boolean isConnect(){
        if(connect==null)
            return false;
        IoSession session=connect.getSession();
        if(session==null)
            return false;
        else
            return session.isConnected();
    }

    //获取连接
    public static Connect getConnect(){
        return connect;
    }

    //获取好友
    public static List<LinkMan> getFriends(Context context){
        List<LinkMan> linkMen=new ArrayList<LinkMan>();
        Connect.setReternMsg(null);
        long time=System.currentTimeMillis();
        while (connect==null){
            if(System.currentTimeMillis()-time>3000)
                return null;
        }
        connect.getFriends();
        try {
            String msg=getReternMsg();
            Log.e(TAG,msg+"");
            if(msg==null)
                return null;
            JSONObject jsonObject=new JSONObject(msg);
            JSONArray array=jsonObject.getJSONArray("friends");
            for(int i=0;i<array.length();i++){
                jsonObject=array.getJSONObject(i);
                linkMen.add(new LinkMan(jsonObject.getString("head_image"),
                        jsonObject.getString("u_id"),
                        jsonObject.getString("u_name"),
                        User.getMsgLog(jsonObject.getString("u_id"),context)
                        ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return linkMen;
    }

    //获取相应的聊天信息
    public static List<Messge> getMsgLog(String id,Context context){
        List<Messge> messges=new ArrayList<Messge>();
        messges=new SavaDate(context).getMsg(id);
        return messges;
    }

    //获取图片
    public static List<Picture> getPhoto() {
        return photo;
    }

    public static void setPhoto(List<Picture> photo) {
        User.photo = photo;
    }
}
