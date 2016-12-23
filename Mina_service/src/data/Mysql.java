package data;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.mina.core.session.IoSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.sun.org.apache.xpath.internal.operations.And;

import sendMsg.Msg;

public class Mysql {

	static String url = "jdbc:mysql://192.168.43.18/mina?useUnicode=true&characterEncoding=utf8";

	static String url2 = "jdbc:mysql://192.168.31.215/mina?useUnicode=true&characterEncoding=utf8";
	
	static String url3="jdbc:mysql://10.66.196.124/mina?useUnicode=true&characterEncoding=utf8";
	static Connection conn;
	static Statement statement;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("成功加载MySQL驱动！");
		} catch (Exception e) {
			System.out.println("找不到MySQL驱动!");
			e.printStackTrace();
		}
		try {
			conn = (Connection) DriverManager.getConnection(url2, "tyhj", "4444");
			// System.out.println("成功加载conn！");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("找不到conn!");
		}
		try {
			if (conn != null) {
				statement = (Statement) conn.createStatement();
			} else {

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 保存单人聊天记录到数据库
	public static synchronized void saveMsgSingle(JSONObject jsonObject) throws JSONException, SQLException {
		String m_id, c, m_date, isReceive, m_msg;
		String from, to, belong;

		m_id = jsonObject.getString("id");
		m_msg = jsonObject.toString();
		m_date = jsonObject.getString("date");

		from = jsonObject.getString("from");
		to = jsonObject.getString("to");

		if (from.compareTo(to) > 0)
			belong = from + to;
		else
			belong = to + from;

		String sql = "insert into msg values('" + m_id + "','" + belong + "','" + m_msg + "','" + to + "','" + m_date
				+ "','" + 0 + "')";
		statement.executeUpdate(sql);
	}

	// 设置消息为已收到
	public static synchronized void setMsgSent(String msg_id) {
		int status = 1;
		String sql = "update msg set m_isReceive='" + status + "' where m_id='" + msg_id + "'";
		// System.out.println("开始变");
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 创建用户
	public static synchronized int createUser(JSONObject object) {
		try {
			String u_name = object.getString("name");
			String u_pwd = object.getString("pwd");
			String u_email = object.getString("email");
			if (u_name == null)
				return 201;
			if (u_pwd == null)
				return 202;
			if (u_email == null)
				return 203;

			String id = u_email + new Timestamp(System.currentTimeMillis()).toString();
			String sql = "select * from user where u_email='" + u_email + "'";
			ResultSet rs = null;
			rs = (ResultSet) statement.executeQuery(sql);
			if (rs.next()) {
				rs.close();
				return 204;
			}
			sql = "insert into user values('" + id + "','" + u_name + "','" + u_pwd + "','" + u_email + "','" + null
					+ "','" + null + "','" + null + "','" + null + "')";
			statement.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 200;
	}

	// 登陆
	public static synchronized int signIn(String u_email, String pwd, IoSession session) {
		String sql = "select * from user where u_email='" + u_email + "'";
		ResultSet rs = null;
		try {
			rs = (ResultSet) statement.executeQuery(sql);
			if (!rs.next()) {
				return 205;
			}
			sql = "select * from user where u_email='" + u_email + "' and u_pwd='" + pwd + "'";
			rs = (ResultSet) statement.executeQuery(sql);
			if (rs.next()) {
				Msg.removeSession(u_email, session);
				session.setAttribute("id", rs.getString(4));
				rs.close();
				return 200;
			} else
				return 206;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 222;
	}

	// 保存群聊天记录到数据库
	public static synchronized void saveMsgGroup() {

	}

	// 保存通知到数据库
	public static synchronized void saveMsgInform() {

	}

	// 获取好友
	public static synchronized String getFriends(String id) {
		JSONObject object = new JSONObject();
		try {
			object.put("action", "getFriends");
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONArray array=new JSONArray();
		String friends = "";
		String sql = "select * from user";
		ResultSet rs = null;
		try {
			rs = (ResultSet) statement.executeQuery(sql);
			while (rs.next()) {
				if (!rs.getString(4).equals(id)) {
					JSONObject object2 = new JSONObject();
					try {
						object2.put("u_id", rs.getString(4));
						object2.put("u_name", rs.getString(2));
						object2.put("head_image", rs.getString(6));
						object2.put("signature", rs.getString(7));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					array.put(object2);
				}
			}
			rs.close();
			try {
				object.put("code", 200);
				JSONObject object3= new JSONObject();
				object3.put("friends", array);
				object.put("msg", object3);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object.toString();
	}
	//获取新消息
	public synchronized static void getNewMsg(IoSession session,String to) {
		JSONObject jsonObject=new JSONObject();
		int status = 0;
		String sql = "select * from msg where m_to= '" + to + "' and m_isReceive='" + status + "'";
		ResultSet rs = null;
			try {
				rs = (ResultSet) statement.executeQuery(sql);
				JSONArray jsonArray=new JSONArray();
				while (rs.next()) {
					try {
						JSONObject object=new JSONObject(rs.getString(3));
						jsonArray.put(object);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					jsonObject.put("action", "getNewMsg");
					jsonObject.put("code", 200);
					jsonObject.put("msg", jsonArray);
					session.write(jsonObject.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
}
