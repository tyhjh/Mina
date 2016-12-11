package mina;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tyhj on 2016/12/12.
 */

public class GetCode {
    public static void getCode(JSONObject jsonObject,Connect connect){
        //登录返回值
        try {

            switch (jsonObject.getString("action")){
                case "signIn":
                case "signUp":
                    connect.setReternMsg(jsonObject.getString("msg"));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
