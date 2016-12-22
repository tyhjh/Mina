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
    String msg;
    boolean isRead;
    String imagePath=null;
    String soundPath=null;

    public Messge(JSONObject msg) {
        this.msg = msg.toString();
    }

    public int getContentType(){
        try {
            return new JSONObject(msg).getInt("type");
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
            return new JSONObject(msg).getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getVoiceLength(){
        try {
            return new JSONObject(msg).getInt("length")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getType() {
        if(type==2)
            return 2;
        try {
            if(new JSONObject(msg).getString("from").equals(User.userInfo.getId()))
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
        try {
            return new JSONObject(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
            JSONObject jsonObject=new JSONObject(msg);
            jsonObject.put("msg",url);
            msg=jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFrom(){
        try {
            return new JSONObject(msg).getString("from");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }
}
