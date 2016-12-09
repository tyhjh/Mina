package sendMsg;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.PortableServer.ServantActivator;

import data.DataBase;

public class Msg {
	
	// 单人聊天，
	public static void send2One(IoSession session, JSONObject jsonObject) throws JSONException {
		if(jsonObject.getString("to")==null)
			return;
		Collection<IoSession> sessions = session.getService().getManagedSessions().values();
		System.out.println("长度"+sessions.size());
		for (IoSession sess : sessions) {
			if (sess.getAttribute("from", "xx").equals(jsonObject.getString("to")))
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
	public static void send2Groups(IoSession session, JSONObject jsonObject) {

	}

	// 发送消息给每个人
	public static void send2everyOne(IoSession session, JSONObject jsonObject) {

	}

	// 发送消息给一个指定的人
	public static void send2Point(IoSession session, JSONObject jsonObject) {

	}

	// 发送消息给一个标签的人
	public static void send2Tags(IoSession session, JSONObject jsonObject) throws JSONException {
		Collection<IoSession> sessions = session.getService().getManagedSessions().values();
		for (IoSession sess : sessions) {
			if (sess.getAttribute("from", "xx").equals(jsonObject.getString("to")))
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
