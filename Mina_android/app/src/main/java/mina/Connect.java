package mina;

import android.util.Log;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONException;
import org.json.JSONObject;

import mina.MinaClientHandler;
import object.LinkMan;
import object.User;
import tools.Defined;

public class Connect {

    private static String u_id =null;

    private static String RETERN_MSG=null;

    private static IoSession session;

    private static NioSocketConnector connector;

    private static volatile Connect connect = null;

    IoHandlerAdapter minaClientHandler;

    private static String ip;

    private static int port;
    //初始化
    private Connect(String ip,int port ) {
        this.ip=ip;
        this.port=port;
        try {
            //Create TCP/IP connection
            if(session==null||!session.isConnected()||connector==null) {
                connector = new NioSocketConnector();

                //创建接受数据的过滤器
                DefaultIoFilterChainBuilder chain = connector.getFilterChain();

                //设定这个过滤器将一行一行(/r/n)的读取数据
                chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));

                minaClientHandler = new MinaClientHandler(this);


                //客户端的消息处理器：一个SamplMinaServerHander对象
                connector.setHandler(minaClientHandler);

                //set connect timeout
                connector.setConnectTimeout(5);
            }
            //连接到服务器：
            ConnectFuture cf = connector.connect(new InetSocketAddress(ip,port));

            //Wait for the connection attempt to be finished.
            cf.awaitUninterruptibly();
            try {
                session = cf.getSession();
            }catch (Exception e){
                e.printStackTrace();
                Connect.setReternMsg("服务器出错");
                System.out.println("Connect+服务器无响应");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取实例
    public static Connect getInstance(String ip, int port) {
        // if already inited, no need to get lock everytime
        if (connect == null||session==null||!session.isConnected()) {
            synchronized (Connect.class) {
                if (connect == null ||session==null|| !session.isConnected()) {
                    connect = new Connect(ip, port);
                }
            }
        }
        if (session != null)
            return connect;
        Connect.setReternMsg("服务器出错");
        return null;
    }

    //发送消息
    public  void sendMsg(String action, String to, String msg, int type,int length) {
        if (session == null) {
            setReternMsg("服务器出错");
            return;
        }
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("msg",msg);
            jsonObject.put("type",type);
            jsonObject.put("length",length);
            session.write(getJson.getMsg(action, u_id, to, jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //登陆
    public  void signIn(String email,String pwd){
        JSONObject jsonObject=new JSONObject();
        if(session==null) {
            setReternMsg("服务器出错");
            return;
        }
        try {
            jsonObject.put("action","signIn");
            jsonObject.put("email",email);
            jsonObject.put("pwd",pwd);
            session.write(jsonObject.toString());
            u_id=email;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //退出
    public  void logOut() {
        if (connector == null)
            return;
        session.close();
        connector.dispose();
    }

    //创建用户
    public  void signUp(String name,String email,String pwd ){
        JSONObject msg;
        if (session == null) {
            setReternMsg("服务器出错");
            return;
        }
        try {
            msg=new JSONObject()
                    .put("action","signUp")
                    .put("name",name)
                    .put("email",email)
                    .put("pwd",pwd);
            session.write(msg.toString());
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    //重新连接
    public  IoSession reconnect(){

        getInstance(ip,port);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("action","reconnect");
            jsonObject.put("id",u_id);
            session.write(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return session;
    }

    //获取好友
    public void getFriends(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("action","getFriends");
            jsonObject.put("u_id",User.userInfo.getId());
            session.write(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public  String getU_id() {
        return u_id;
    }

    public  static String getReternMsg() {
        return RETERN_MSG;
    }

    public static void setReternMsg(String reternMsg) {
        RETERN_MSG = reternMsg;
    }

    public  IoSession getSession(){
        return session;
    }
}
