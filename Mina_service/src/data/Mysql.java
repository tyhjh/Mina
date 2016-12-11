package data;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class Mysql {

	static String url = "jdbc:mysql://192.168.31.215/mina?useUnicode=true&characterEncoding=utf8";
	static Connection conn;
	static Statement statement;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("成功加载MySQL驱动！");
		} catch (Exception e) {
			System.out.println("找不到MySQL驱动!");
			e.printStackTrace();
		}
		try {
			conn = (Connection) DriverManager.getConnection(url, "tyhj", "4444");
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
	public static void saveMsgSingle(JSONObject jsonObject) throws JSONException, SQLException {
		String m_id,c,m_date,isReceive,m_msg;
		String from,to,belong;
		
		m_id=jsonObject.getString("id");
		m_msg=jsonObject.toString();
		m_date=jsonObject.getString("date");
		
		from=jsonObject.getString("from");
		to=jsonObject.getString("to");
		
		if(from.compareTo(to)>0)
			belong=from+to;
		else
			belong=to+from;
		
		String sql = "insert into msg values('" + m_id + "','" + belong + "','" + m_msg + "','" + to + "','" + m_date + "','" + 0 + "')";
		statement.executeUpdate(sql);
	}

	
	//设置消息为已收到
	public static void setMsgSent(String msg_id){
		int status=1;
		String sql="update msg set m_isReceive='" + status + "' where m_id='" + msg_id + "'";
		//System.out.println("开始变");
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//创建用户
	public static int createUser(JSONObject jsonObject){
		try {
			JSONObject object=jsonObject.getJSONObject("msg");
			String u_name=object.getString("name");
			String u_pwd=object.getString("pwd");
			String u_email=object.getString("email");
			if(u_name==null)
				return 201;
			if(u_pwd==null)
				return 202;
			if(u_email==null)
				return 203;
			
			String id=u_email+new Timestamp(System.currentTimeMillis()).toString();
			String sql = "select * from user where u_email='" + u_email + "'";
			ResultSet rs = null;
			rs = (ResultSet) statement.executeQuery(sql);
			if (rs.next()) {
				return 204;
			}
			sql="insert into msg values('" + id + "','" + u_name + "','" + u_pwd + "','" + u_email + "','" + "" + "','" +""+ "','" + "" + "','" +""+ "')";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 200;
	}
	
	// 保存群聊天记录到数据库
	public static void saveMsgGroup() {

	}

	// 保存通知到数据库
	public static void saveMsgInform() {

	}

}
