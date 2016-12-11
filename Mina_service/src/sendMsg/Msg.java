package sendMsg;

import java.sql.SQLException;
import java.util.Collection;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.PortableServer.ServantActivator;

import data.Mysql;

public class Msg {

	
	private static final String SIGN_IN = "signIn";

	private static final String SINGLE_TALK = "singleTalk";
	
	private static final String MSG_SENT = "msgSent";
	
	private static final String SIGN_UP = "signUp";

	public static void msgManage(IoSession session, JSONObject jsonObject) throws JSONException {
		switch (jsonObject.getString("action")) {

		case SIGN_IN:
			signIn(session, jsonObject);
			break;

		case SINGLE_TALK:
			singleTalk(session, jsonObject);
			break;
			
		case MSG_SENT:
			msgSent(jsonObject.getString("id"));
			break;

		case SIGN_UP:
			createUser(jsonObject,session);
			break;
		default:
			break;
		}
	}

	// 登录
	private static void signIn(IoSession session, JSONObject jsonObject) throws JSONException {
		session.setAttribute("id", jsonObject.getString("from"));

	}

	// 单人聊天，
	private static void singleTalk(IoSession session, JSONObject jsonObject) throws JSONException {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Collection<IoSession> sessions = session.getService().getManagedSessions().values();
					for (IoSession sess : sessions) {
						if (sess.getAttribute("id", "null").equals(jsonObject.getString("to"))){
							sess.write(jsonObject.toString() + "\n");
							break;
						}
					}
					
					Mysql.saveMsgSingle(jsonObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	//标记信息为已接收
	private static void msgSent(String msg_id){
		Mysql.setMsgSent(msg_id);
	}
	
	//创建用户
	private static void createUser(JSONObject jsonObject,IoSession session){ 
		session.write(getJson.getCode(Mysql.createUser(jsonObject),"signUp"));
	}
}
