package mina;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import object.User;

public class getJson {
	public static String getMsg(String msg2,int type,int length,String action,String to){
		String from= User.userInfo.getId();
		JSONObject jsonObject=new JSONObject();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		try {
			jsonObject.put("id",now+from+to);
			jsonObject.put("action",action);
			jsonObject.put("from",from);
			jsonObject.put("to",to);
			jsonObject.put("date",now.toString());
			jsonObject.put("msg",msg2);
			jsonObject.put("type",type);
			jsonObject.put("length",length);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
