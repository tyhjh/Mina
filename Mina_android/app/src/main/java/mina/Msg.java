package mina;

import org.json.JSONObject;

/**
 * Created by Tyhj on 2016/12/11.
 */

public abstract class Msg {
    public abstract void manageMsg(JSONObject jsonObject);
}
