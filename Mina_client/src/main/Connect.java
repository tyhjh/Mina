package main;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.xml.internal.ws.resources.SenderMessages;

import json.getJson;
import mina.MinaClientHandler;

public class Connect {
	
	private static IoSession session;
	
	private static NioSocketConnector connector;

	private static volatile Connect connect = null;
	
	private String ip=null;
	
	private int port;
	
	private static String u_id;
	
	private Connect(String ip,int port,String id){
		
		this.u_id=id;
		this.ip=ip;
		this.port=port;
		
		//Create TCP/IP connection     
		connector=new NioSocketConnector();     
             
        //创建接受数据的过滤器     
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();     
             
        //设定这个过滤器将一行一行(/r/n)的读取数据     
        chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));     
             
        MinaClientHandler minaClientHandler=new MinaClientHandler();
        minaClientHandler.setId(id);
        		
        //客户端的消息处理器：一个SamplMinaServerHander对象     
        connector.setHandler(minaClientHandler);     
             
        //set connect timeout     
        connector.setConnectTimeout(30);     
             
        //连接到服务器：     
        ConnectFuture cf = connector.connect(new InetSocketAddress(ip,port));     
            
        
        //Wait for the connection attempt to be finished.     
        cf.awaitUninterruptibly();     
        
        session=cf.getSession();
	}

	public static Connect getInstance(String ip,int port,String id) {
        // if already inited, no need to get lock everytime
        if (connect == null) {
            synchronized (Connect.class) {
                if (connect == null) {
                	connect = new Connect(ip,port,id);
                }
            }
        }
 
        return connect;
    }
	
	public static void SendMsg(String to,String msg,String type){
		if(session==null)
			return;
		session.write(getJson.getMsg("singleTalk"," ",u_id, to, msg, type));
	}
	
	public static void LogOut(){
		if(connector==null)
			return;
		connector.dispose();
	}
	
}
