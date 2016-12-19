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
	
	private static final String RECONNCET="reconnect";
	
	private static final String GET_FRIENDS = "getFriends";

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
			
		case RECONNCET:
			reconnect(jsonObject,session);
			break;
			
		case GET_FRIENDS:
			getFriends(jsonObject.getString("u_id"),session);
			break;
			
		default:
			break;
		}
	}

	// 登录
	private static void signIn(IoSession session, JSONObject jsonObject) throws JSONException {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String email;
				try {
					email = jsonObject.getString("email");
					String pwd=jsonObject.getString("pwd");
					int code= Mysql.signIn(email, pwd,session);
					session.write(getJson.getCode(code,"signIn")+"\n");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
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
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Mysql.setMsgSent(msg_id);
			}
		}).start();
	}
	
	//创建用户
	private static void createUser(JSONObject jsonObject,IoSession session){ 
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int code= Mysql.createUser(jsonObject);
				session.write(getJson.getCode(code,"signUp")+"\n");
			}
		}).start();
	}
	
	//重新连接
	private static void reconnect(JSONObject jsonObject,IoSession session){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String id=jsonObject.getString("id");
					removeSession(id,session);
					session.setAttribute("id",id);
					Collection<IoSession> sessions = session.getService().getManagedSessions().values();
					for (IoSession sess : sessions) {
						System.out.println(sess.getAttribute("id")+"重连");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	
	}
	
	//获取好友
	private static void getFriends(String id,IoSession session) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				session.write(Mysql.getFriends(id)+"\n");
				Collection<IoSession> sessions = session.getService().getManagedSessions().values();
				int i=1;
				for (IoSession sess : sessions) {
					System.out.println(sess.getAttribute("id")+" "+i);
					i++;
				}
			}
		}).start();
	
	}
	
	
	
	//删除已存在IoSession
	public static void removeSession(String id,IoSession session){
		Collection<IoSession> sessions = session.getService().getManagedSessions().values();
		for (IoSession sess : sessions) {
			if (sess.getAttribute("id", "xx").equals(id))
				sess.close();
		}
	}
	
}
