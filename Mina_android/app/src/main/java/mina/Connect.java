package mina;

import android.util.Log;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONException;
import org.json.JSONObject;

import mina.MinaClientHandler;
import object.User;

public class Connect {

    private static String u_id =null;

    private static String RETERN_MSG=null;

    private static IoSession session;

    private static NioSocketConnector connector;

    private static volatile Connect connect = null;

    //初始化
    private Connect(String u_id, String pwd) {

        this.u_id = u_id;

        try {
            //Create TCP/IP connection
            connector = new NioSocketConnector();

            //创建接受数据的过滤器
            DefaultIoFilterChainBuilder chain = connector.getFilterChain();

            //设定这个过滤器将一行一行(/r/n)的读取数据
            chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));

            MinaClientHandler minaClientHandler = new MinaClientHandler(this);
            minaClientHandler.setId(u_id);
            minaClientHandler.setPwd(pwd);

            //客户端的消息处理器：一个SamplMinaServerHander对象
            connector.setHandler(minaClientHandler);

            //set connect timeout
            connector.setConnectTimeout(30);

            if(Mina.getIpAddress()==null||Mina.getIpPort()==0)
                return;

            //连接到服务器：
            ConnectFuture cf = connector.connect(new InetSocketAddress(Mina.getIpAddress(), Mina.getIpPort()));

            //Wait for the connection attempt to be finished.
            cf.awaitUninterruptibly();

            session = cf.getSession();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取实例
    public static Connect getInstance(String u_id, String pwd) {
        // if already inited, no need to get lock everytime
        if (connect == null) {
            synchronized (Connect.class) {
                if (connect == null) {
                    connect = new Connect(u_id, pwd);
                    if (session == null)
                        connect = null;
                }
            }
        }
        if (session != null)
            return connect;
        setReternMsg("服务器出错");
        return null;
    }

    //发送消息
    public static void sendMsg(String action, String key, String to, String msg, int type) {
        if (session == null) {
            setReternMsg("服务器出错");
            return;
        }
        session.write(getJson.getMsg(action, key, u_id, to, msg, type + ""));
    }

    //退出
    public static void LogOut() {
        if (connector == null)
            return;
        connector.dispose();
    }

    //创建用户
    public static void signUp(String name,String email,String pwd ){
        String msg;
        try {
            msg=new JSONObject().put("name",name)
                    .put("email",email)
                    .put("pwd",pwd)
                    .toString();
            if (session == null) {
                setReternMsg("服务器出错");
                return;
            }
            session.write(getJson.getMsg("signUp", "", u_id, "", msg, 0+ ""));
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static String getReternMsg() {
        return RETERN_MSG;
    }

    public static void setReternMsg(String reternMsg) {
        RETERN_MSG = reternMsg;
    }
}
