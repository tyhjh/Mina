package object;

import android.content.Context;

import com.example.tyhj.mina_android.R;

import mina.Connect;
import tools.Defined;

/**
 * Created by Tyhj on 2016/12/11.
 */

public class User {

    private static String RETERN_MSG=null;

    private static Connect connect=null;

    //登录
    public static String signIn(String id, String pwd, Context context){
        setReternMsg(null);
        if(!Defined.isIntenet(context))
            RETERN_MSG=context.getString(R.string.warn_no_internet);
        else {
            connect = Connect.getInstance(id, pwd);
            setReternMsg(connect.getReternMsg());
        }
        return RETERN_MSG;
    }

    //创建用户
    public static String signUp(Context context,String name,String eamil,String pwd){
        setReternMsg(null);
        if(!Defined.isIntenet(context))
            RETERN_MSG=context.getString(R.string.warn_no_internet);
        else
            connect.signUp(name,eamil,pwd);
        return RETERN_MSG;
    }

    //发送消息
    public static String sendMsg(String msg,String to,int type,Context context){
        setReternMsg(null);
        if(!Defined.isIntenet(context))
            RETERN_MSG=context.getString(R.string.warn_no_internet);
        connect.sendMsg("singleTalk","",to,msg,type);
        return RETERN_MSG;
    }



    private static String getReternMsg() {
        return RETERN_MSG;
    }

    private static void setReternMsg(String reternMsg) {
        RETERN_MSG = reternMsg;
    }

}
