package mina;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tyhj on 2016/12/12.
 */

public class GetCode {
    public static void getCode(JSONObject jsonObject){
        //登录返回值
        int code;
        try {
            switch (jsonObject.getString("action")){
                case "signIn":
                case "signUp":
                     code=jsonObject.getInt("code");
                    if(code==200)
                        Connect.setReternMsg("200");
                    else
                        Connect.setReternMsg(jsonObject.getString("msg"));
                    break;

                case "getFriends":
                    code=jsonObject.getInt("code");
                    if(code==200)
                        Connect.setReternMsg(jsonObject.getJSONObject("msg").toString());
                    else
                        Connect.setReternMsg(null);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
