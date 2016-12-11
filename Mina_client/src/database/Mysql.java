package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mysql {
	String url = "jdbc:mysql://192.168.31.215/mina?useUnicode=true&characterEncoding=utf8";
	Connection conn;
	Statement statement;

	public Mysql() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("成功加载MySQL驱动！");
		} catch (Exception e) {
			System.out.println("找不到MySQL驱动!");
			e.printStackTrace();
		}
		try {
			conn = (Connection) DriverManager.getConnection(url, "tyhj", "4444");
			System.out.println("成功加载conn！");
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

	// 返回Connction
	public Connection getConnection() {
		return conn;
	}

	// 关闭
	public void close() {
		try {
			if (conn != null)
				conn.close();
			if (statement != null)
				statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getData() {
		String sql = "select * from msg";
		ResultSet rs = null;
		try {
			rs = (ResultSet) statement.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString(1) + "xxx" + rs.getString(2)+"xxx"+rs.getString(3)+"xxx"+rs.getString(4)+"xxx"+rs.getInt(5));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void insertData() throws SQLException{
		Timestamp time1 = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time2=df.format(new Date());
		String sql = "insert into time values('" + time1.toString() + "','" + time2 + "')";
		statement.executeUpdate(sql);
	}
	
	
	
	

	// 查询用户是否已经存在
	public boolean isUserHad(String str) {
		String sql = "select * from user where id='" + str + "'";
		ResultSet rs = null;
		try {
			rs = (ResultSet) statement.executeQuery(sql);
			if (rs.next()) {
				return true;
			} else {
				rs.close();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 查询邮箱是否被注册
	public boolean isEmailHad(String str) {
		String sql = "select * from user where email='" + str + "'";
		ResultSet rs = null;
		try {
			rs = (ResultSet) statement.executeQuery(sql);
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 新建用户
	public void addUser(String id, String pas, String url, String name, String email, String signature, String place,
			String snumber) {
		String sql = "insert into user values('" + id + "','" + pas + "','" + url + "','" + name + "','" + email + "','"
				+ signature + "','" + place + "','" + snumber + "')";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 修改密码
	public void changePas(String id, String pas) {

	}

	// 修改信息
	public void changeMsg(String id, String pas, String email, String name, String signature, String snumber,
			String place) {
	}

	// 获取头像
	public String getHeadImageUrl(String str) {
		String sql = "select * from user where id='" + str + "'";
		String url = null;
		ResultSet rs = null;
		try {
			rs = (ResultSet) statement.executeQuery(sql);
			if (rs.next()) {
				url = rs.getString(3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	// 修改头像
	public void setHeadImageUrl(String headUrl, String id) {
		String sql = "update user set headImage ='" + headUrl + "' where id ='" + id + "'";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
