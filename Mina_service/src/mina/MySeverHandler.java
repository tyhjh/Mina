package mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MySeverHandler extends IoHandlerAdapter{

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		System.out.println("exceptionCaught");
	}
	//收到消息
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		System.out.println("messageReceived："+(String)message);
		//返回给客户端
		session.write("server replay:哈哈"+"\n");
	}
	//发消息时候
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("messageSent");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("sessionClosed");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("sessionCreated");
	}
	//客户端空闲时候
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("sessionIdle");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpened");
	}

}
