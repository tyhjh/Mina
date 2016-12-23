package object;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.example.tyhj.mina_android.R;
import com.nostra13.universalimageloader.utils.L;

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

    public static int TIME_OUT=5000;

    public static UserInfo userInfo;

    private static Connect connect=null;

    private static List<Picture> photo;

    static List<LinkMan> linkMens;

    public static boolean isUpdate;

    //初始化
    public static String init(Context context, String ip, int port, SendBordCast sendBordCast){
        Connect.setReternMsg(null,"init");
        if(!Defined.isIntenet(context))
            return context.getString(R.string.warn_no_internet);
        else
            connect = Connect.getInstance(ip,port,sendBordCast);
        return null;
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
                Log.e("User",action+"time："+(System.currentTimeMillis()-time));
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

    //获取本地好友
    public static List<LinkMan> getFriendHost(Context context){

        List<LinkMan> linkMen=new ArrayList<LinkMan>();
        linkMen=new SavaDate(context).getLinkMan();
        if(linkMen!=null&&linkMen.size()>0) {
            User.linkMens = linkMen;
            return linkMen;
        }
        return linkMen;
    }

    //保存更新消息
    public static void upDateView(LinkMan linkMan){
        User.linkMens.remove(linkMan);
        User.linkMens.add(0,linkMan);
    }

    //获取更新消息
    public static List<LinkMan> getUpdate(){
        return User.linkMens;
    }

    //获取好友
    public static List<LinkMan> getFriends(Context context){
        List<LinkMan> linkMen=getFriendHost(context);
        if(linkMen!=null&&linkMen.size()>0) {
            User.linkMens=linkMen;
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
                        new ArrayList<Messge>()
                        ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        User.linkMens = linkMen;
        if(User.linkMens==null)
            User.linkMens=new ArrayList<LinkMan>();
        return linkMen;
    }

    //获取未读聊天信息
    public static void getMsgLog(){
        Connect.setReternMsg(null,"getNewMsg");
        if(connect==null)
            return;
        connect.getMsg();
        String msg=getReternMsg("getNewMsg");
        if(msg!=null){
            Log.e("User","getNewMsg：收到了未读消息");
            try {
                JSONObject jsonObject=new JSONObject(msg);
                JSONArray jsonArray=jsonObject.getJSONArray("msg");
                for(int i=0;i<jsonArray.length();i++){
                    Messge messge=new Messge(jsonArray.getJSONObject(i));
                    savaNewMsg(messge);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //获取最近图片
    public static List<Picture> getPhoto() {
        return photo;
    }

    //保存最近图片列表
    public static void setPhoto(List<Picture> photo) {
        User.photo = photo;
    }

    //保存消息
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

    //保存一条信息，后台收到消息的时候
    public static void upDateView(final Context context, final Messge messge){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SavaDate(context).saveOnemessge(messge);
            }
        }).start();
    }

    //保存一条信息，前台收到一条消息的时候
    public static void savaNewMsg(Messge messge){
        if(User.linkMens!=null)
            for(int i=0;i<User.linkMens.size();i++){
                if(User.linkMens.get(i).getId().equals(messge.getFrom())){
                    User.linkMens.get(i).getMessges().add(messge);
                    break;
                }
            }
    }
}
