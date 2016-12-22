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
import myinterface.SendBordCast;
import tools.Defined;
import tools.SavaDate;

import static android.content.ContentValues.TAG;

/**
 * Created by Tyhj on 2016/12/11.
 */

public class User {

    public static int TIME_OUT=3000;

    public static UserInfo userInfo;

    private static Connect connect=null;

    private static List<Picture> photo;

    static List<LinkMan> linkMens;

    //初始化
    public static String init(Context context, String ip, int port, SendBordCast sendBordCast){
        Connect.setReternMsg(null,"init");
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else
            connect = Connect.getInstance(ip,port,sendBordCast);
        return getReternMsg("init");
    }

    //登陆
    public static String signIn(String email,String pwd,Context context){
        Connect.setReternMsg(null,"signIn");
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else {
            try {
                connect.signIn(email,pwd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getReternMsg("signIn");
    }

    //登陆
    public static String signIn(String email,String pwd){
        Connect.setReternMsg(null,"signIn");
            try {
                connect.signIn(email,pwd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return getReternMsg("signIn");
    }

    //创建用户
    public static String signUp(Context context,String name,String eamil,String pwd){
        Connect.setReternMsg(null,"signUp");
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else
            connect.signUp(name,eamil,pwd);
        return getReternMsg("signUp");
    }

    //发送消息
    public static String sendMsg(String msg,Context context){
        Connect.setReternMsg(null,"singleTalk");
        if(!Defined.isIntenet(context)||connect==null)
            return context.getString(R.string.warn_no_internet);
            connect.sendMsg(msg);
        return getReternMsg("singleTalk");
    }

    //退出
    public static void logOut(Context context){
        connect.logOut();
        new SavaDate(context).deleteData();
    }

    //获取返回值b
    private static String getReternMsg(String action) {
        long time=System.currentTimeMillis();
        while (Connect.getReternMsg(action)==null) {
            if(System.currentTimeMillis()-time>TIME_OUT) {
                Log.e("User","time："+(System.currentTimeMillis()));
                return null;
            }
        }
        return Connect.getReternMsg(action);
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
        if(linkMens!=null)
            return linkMens;
        List<LinkMan> linkMen;
        linkMen=new SavaDate(context).getLinkMan();
        if(linkMen!=null&&linkMen.size()>0) {
            User.linkMens = linkMen;
            return linkMen;
        }
        linkMen=new ArrayList<LinkMan>();
        Connect.setReternMsg(null,"getFriends");
        long time=System.currentTimeMillis();
        connect.getFriends();
        while (Connect.getReternMsg("getFriends")==null){
            if(System.currentTimeMillis()-time>TIME_OUT)
                break;
        }
        try {
            String msg=getReternMsg("getFriends");
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
                        User.getMsgLog(jsonObject.getString("u_id"))
                        ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        User.linkMens = linkMen;
        if(linkMens==null)
            linkMens=new ArrayList<LinkMan>();
        return linkMen;
    }

    //获取相应的聊天信息
    public static List<Messge> getMsgLog(String id){
        List<Messge> messges=new ArrayList<Messge>();
        return messges;
    }


    //获取图片
    public static List<Picture> getPhoto() {
        return photo;
    }

    public static void setPhoto(List<Picture> photo) {
        User.photo = photo;
    }


    public static void upDateView(final Context context, LinkMan linkMan){
        if(linkMens.contains(linkMan))
            linkMens.remove(linkMan);
        linkMens.add(0,linkMan);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SavaDate(context).saveLinkMan(linkMens);
            }
        }).start();
    }
    public static void upDateView(final Context context, List<LinkMan> linkMan){
        linkMens.clear();
        if(linkMan!=null)
            for(int i=0;i<linkMan.size();i++){
                linkMens.add(linkMan.get(i));
            }
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SavaDate(context).saveLinkMan(linkMens);
            }
        }).start();
    }

}
