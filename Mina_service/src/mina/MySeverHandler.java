package mina;

import java.util.Collection;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import sendMsg.Msg;

public class MySeverHandler extends IoHandlerAdapter {

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println("exceptionCaught" + cause.getMessage());
	}

	// 收到消息
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		JSONObject jsonObject = new JSONObject((String) message);
		// 处理消息
		Msg.msgManage(session, jsonObject);

		System.out.println("messageReceived："+(String)message+session.getAttribute("id","xx"));

	}

	// 发送消息成功的时候
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		JSONObject jsonObject = new JSONObject((String) message);
		jsonObject.put("action", "msgSent");
		// 处理消息
		Msg.msgManage(session, jsonObject);
		System.out.println("messageSent"+session.getAttribute("id")+jsonObject.toString());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.close();
		Collection<IoSession> sessions = session.getService().getManagedSessions().values();
		for (IoSession sess : sessions) {
			System.out.println(sess.getAttribute("id"));
		}
		System.out.println("sessionClosed");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("sessionCreated");
	}

	// 客户端空闲时候
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// System.out.println("sessionIdle");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpened");
	}

}
