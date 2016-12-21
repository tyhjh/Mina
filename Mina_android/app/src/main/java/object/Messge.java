package object;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Tyhj on 2016/12/11.
 */

public class Messge implements Serializable{
    int type;
    JSONObject msg;
    boolean isRead;
    String imagePath=null;
    String soundPath=null;

    public Messge(JSONObject msg) {
        this.msg = msg;
    }

    public int getContentType(){
        try {
            return msg.getInt("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getContent(){
        if(imagePath!=null) {
            String imageUrl = ImageDownloader.Scheme.FILE.wrap(imagePath);
            return imageUrl;
        }
        if(soundPath!=null)
            return soundPath;
        try {
            return msg.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getVoiceLength(){
        try {
            return msg.getInt("length")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getType() {
        if(type==2)
            return 2;
        try {
            if(msg.getString("from").equals(User.userInfo.getId()))
                return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JSONObject getMsg() {
        return msg;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public  void update(String url){
        try {
            msg.put("msg",url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }
}
