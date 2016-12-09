package mina;
import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**   
 * mina客户端   
 */    
public class MinaClient {     
	static String ip="192.168.43.18";
	static String ip2="192.168.31.215";
	private static IoSession session; 
    public static void main(String []args)throws Exception{     
             
        //Create TCP/IP connection     
        NioSocketConnector connector = new NioSocketConnector();     
             
        //创建接受数据的过滤器     
        DefaultIoFilterChainBuilder chain = connector.getFilterChain();     
             
        //设定这个过滤器将一行一行(/r/n)的读取数据     
        chain.addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));     
             
        //客户端的消息处理器：一个SamplMinaServerHander对象     
        connector.setHandler(new MinaClientHandler());     
             
        //set connect timeout     
        connector.setConnectTimeout(30);     
             
        //连接到服务器：     
        ConnectFuture cf = connector.connect(new InetSocketAddress(ip,9897));     
            
        
        //Wait for the connection attempt to be finished.     
        cf.awaitUninterruptibly();     
        
        session=cf.getSession();
        
        //connector.dispose();     
        
        session.write("嗨，你好呀");
        
        while(true){
        	Scanner in = new Scanner(System.in);
        	String msg=in.next();
        	session.write(msg);
        }
        
    }    
}  