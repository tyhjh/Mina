package mina;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import com.sun.org.apache.bcel.internal.generic.NEW;

import json.getJson;

/** 
 * 客户端业务处理逻辑 
 
 */  
public class MinaClientHandler extends IoHandlerAdapter {  
    // 当客户端连接进入时  
    @Override  
    public void sessionOpened(IoSession session) throws Exception {  
        System.out.println("incomming 客户端: " + session.getRemoteAddress());  
    }  
  
    @Override  
    public void exceptionCaught(IoSession session, Throwable cause)  
            throws Exception {  
        System.out.println("客户端发送信息异常....");  
    }  
  
    // 当客户端发送消息到达时  
    @Override  
    public void messageReceived(IoSession session, Object message)  
            throws Exception {  
    	JSONObject jsonObject=new JSONObject(message.toString());
        System.out.println(jsonObject.getString("from")+":" + jsonObject.getString("msg"));  
    }  
  
    @Override  
    public void sessionClosed(IoSession session) throws Exception {  
        System.out.println("客户端与服务端断开连接.....");  
    }  
  
    @Override  
    public void sessionCreated(IoSession session) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println("已建立连接" + session.getRemoteAddress());
        session.write(getJson.getMsg("signIn", id, null, null,null));  
    }  
    
    public void setId(String id){
    	this.id=id;
    }
    
  private String id=null;
}  