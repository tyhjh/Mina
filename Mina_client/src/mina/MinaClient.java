package mina;
import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONObject;

import com.sun.org.apache.bcel.internal.generic.NEW;

import json.getJson;
import main.Connect;

/**   
 * mina客户端   
 */    
public class MinaClient {     
	static String ip="192.168.43.18";
	static String ip2="192.168.31.215";
    public static void main(String []args)throws Exception{     
    
    	Connect connect=Connect.getInstance(ip, 9897,"tyhj5");
        
        while(true){
        	Scanner in = new Scanner(System.in);
        	
        	System.out.print("输入接受者：");
        	String toWho=in.next();
        	
        	System.out.print("输入信息：");
        	String msg=in.next();
        	
        	connect.SendMsg(toWho, msg, 0+"");
        	
        	//connect.LogOut();
        }
        
    }    
}  