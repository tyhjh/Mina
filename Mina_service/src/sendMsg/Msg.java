package sendMsg;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.PortableServer.ServantActivator;

import data.DataBase;

public class Msg {
	
	private static final String SIGN_IN="signIn";
	
	private static final String SINGLE_TALK="singleTalk";
	
	

	public static void msgManage(IoSession session, JSONObject jsonObject) throws JSONException{
		switch (jsonObject.getString("action")) {
		
		case SIGN_IN:
			signIn(session,jsonObject);
			break;
			
		case SINGLE_TALK:
			singleTalk(session,jsonObject);
			break;

		default:
			break;
		}
	}
	
	//登录
	private static void signIn(IoSession session, JSONObject jsonObject) throws JSONException{
		session.setAttribute("from",jsonObject.getString("id"));
		
	}
	
	
	
	// 单人聊天，
	private static void singleTalk(IoSession session, JSONObject jsonObject) throws JSONException {
		Collection<IoSession> sessions = session.getService().getManagedSessions().values();
		for (IoSession sess : sessions) {
			if (sess.getAttribute("id", "null").equals(jsonObject.getString("to")))
				sess.write(jsonObject.toString()+"\n");
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DataBase.saveMsgOne();
			}
		}).start();
	}

	// 群聊，发送消息给一个群的人
	private static void send2Groups(IoSession session, JSONObject jsonObject) {

	}

	// 发送消息给每个人
	private static void send2everyOne(IoSession session, JSONObject jsonObject) {

	}

	// 发送消息给一个指定的人
	private static void send2Point(IoSession session, JSONObject jsonObject) {

	}

	// 发送消息给一个标签的人
	private static void send2Tags(IoSession session, JSONObject jsonObject) throws JSONException {
		Collection<IoSession> sessions = session.getService().getManagedSessions().values();
		for (IoSession sess : sessions) {
			if (sess.getAttribute("id", "xx").equals(jsonObject.getString("to")))
				sess.write(jsonObject.getString("msg") + "\n");
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		}).start();
	}

}
