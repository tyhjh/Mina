package object;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Tyhj on 2016/12/11.
 */

public class Messge implements Serializable{
    String sendTo;
    String from;
    String type;
    JSONObject msg;
    boolean isRead;
    public Messge(String from,String sendTo, JSONObject msg) {
        this.sendTo = sendTo;
        this.msg = msg;
        this.from=from;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getMsg() {
        return msg;
    }

    public void setMsg(JSONObject msg) {
        this.msg = msg;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
