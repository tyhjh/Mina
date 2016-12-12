package sendMsg;

import org.json.JSONException;
import org.json.JSONObject;

import code.GetCode;

public class getJson {
	public static String getMsg(String action,String from,String to,String msg,String type){
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("action", action);
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
	
	
	public static String getCode(int code,String action){
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("code", code);
			jsonObject.put("action", action);
			jsonObject.put("msg", GetCode.getCode(code));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
