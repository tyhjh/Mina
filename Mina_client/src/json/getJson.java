package json;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

public class getJson {
	public static String getMsg(String action,String key,String from,String to,String msg,String type){
		JSONObject jsonObject=new JSONObject();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		try {
			jsonObject.put("id",now.toString()+from+to);
			jsonObject.put("action", action);
			jsonObject.put("key",key);
			jsonObject.put("from",from);
			jsonObject.put("to",to);
			jsonObject.put("msg",msg);
			jsonObject.put("type",type);
			jsonObject.put("date",now.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
		
	}
}
