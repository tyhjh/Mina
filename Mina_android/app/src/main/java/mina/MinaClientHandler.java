package mina;

import android.content.Context;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;


import myinterface.SendBordCast;
import object.User;
import tools.Defined;

/**
 * 客户端业务处理逻辑
 */
public class MinaClientHandler extends IoHandlerAdapter {

    Connect connect;
    SendBordCast sendBordCast;

    public MinaClientHandler(Connect connect, SendBordCast sendBordCast){
        this.connect=connect;
        this.sendBordCast=sendBordCast;
    }


    // 当客户端连接进入时  
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        //System.out.println("incomming 客户端: " + session.getRemoteAddress());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {

        System.out.println("客户端发送信息异常...."+cause.toString());
    }

    // 收到消息
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        System.out.println("收到消息了"+message.toString());
        JSONObject jsonObject = new JSONObject(message.toString());
        GetCode.getCode(jsonObject,sendBordCast);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("messageSent....."+message.toString());
    }

    @Override
    public void sessionClosed(final IoSession session) throws Exception {
        System.out.println("sessionClosed+客户端与服务端断开连接.....");
        if(User.userInfo!=null) {
            System.out.println("sessionClosed+重新连接执行....."+User.userInfo.getId()+User.userInfo.getPwd());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!connect.reconnect().isConnected()) {
                        try {
                            Thread.sleep(2000);
                            System.out.println("正在尝试重新连接.....");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        }



    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO Auto-generated method stub  
        System.out.println("已建立连接" + session.getRemoteAddress());
    }
}