package mina;

import android.util.Log;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import json.getJson;
import mina.MinaClientHandler;

public class Connect {

	static String ip2="192.168.43.18";
	static String ip="192.168.31.215";
	private static int port=9897;
	private static String u_id="tyhj";

	private static IoSession session;
	
	private static NioSocketConnector connector;

	private static volatile Connect connect = null;

	//初始化
	private Connect(String u_id){

		this.u_id=u_id;

		try {
			//Create TCP/IP connection
			connector = new NioSocketConnector();

			//创建接受数据的过滤器
			DefaultIoFilterChainBuilder chain = connector.getFilterChain();

			//设定这个过滤器将一行一行(/r/n)的读取数据
			chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));

			MinaClientHandler minaClientHandler = new MinaClientHandler();
			minaClientHandler.setId(u_id);

			//客户端的消息处理器：一个SamplMinaServerHander对象
			connector.setHandler(minaClientHandler);

			//set connect timeout
			connector.setConnectTimeout(30);

			//连接到服务器：
			ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));


			//Wait for the connection attempt to be finished.
			cf.awaitUninterruptibly();

			session = cf.getSession();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	//获取实例
	public static Connect getInstance(String u_id) {
        // if already inited, no need to get lock everytime
        if (connect == null) {
            synchronized (Connect.class) {
                if (connect == null) {
                	connect = new Connect(u_id);
                }
            }
        }
 		if(session!=null)
        	return connect;
		return null;
    }

	//发送消息
	public static void sendMsg(String to,String msg,int type){
		if(session==null)
			return;
		session.write(getJson.getMsg(u_id, to, msg, type+""));
	}

	//退出
	public static void LogOut(){
		if(connector==null)
			return;
		connector.dispose();
	}

	public static void setIp(String ip) {
		Connect.ip = ip;
	}

	public static void setIp2(String ip2) {
		Connect.ip2 = ip2;
	}

	public static void setPort(int port) {
		Connect.port = port;
	}



}
