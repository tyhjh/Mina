package sendMsg;

import org.json.JSONException;
import org.json.JSONObject;

public class getJson {
	public static String getMsg(String action,String key,String from,String to,String msg,String type){
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("action", action);
			jsonObject.put("key", key);
			jsonObject.put("from",from);
			jsonObject.put("to",to);
			jsonObject.put("msg",msg);
			jsonObject.put("type",type);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
		
	}
}
