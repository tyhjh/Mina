package object;

import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

import tools.Defined;

/**
 * Created by Tyhj on 2016/12/19.
 */

public class LinkMan implements Serializable{
    String headImage,id,name;
    List<Messge> messges;
    String type;

    public LinkMan(String headImage, String id, String name, List<Messge> messges) {
        this.headImage = headImage;
        this.id = id;
        this.name = name;
        this.messges = messges;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Messge> getMessges() {
        return messges;
    }

    public void setMessges(List<Messge> messges) {
        this.messges = messges;
    }

    public String getManType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnRead(){
        if(messges!=null)
        for(int i=messges.size()-1;i>=0;i--){
            if(messges.get(i).isRead()||messges.get(i).getFrom().equals(User.userInfo.getId()))
                return messges.size()-1-i;
        }
        return 0;
    }

    public int getType(){
        Messge messge=null;
        if(messges!=null) {
            messge = messges.get(messges.size() - 1);
            try {
               return messge.getMsg().getJSONObject("msg").getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public String getTime(){
        Messge messge=null;
        if(messges!=null) {
            messge = messges.get(messges.size() - 1);
            try {
                String date=messge.getMsg().getString("date");
                return Defined.getTime2(Integer.parseInt(date.substring(0,4)+date.substring(5,7)+date.substring(8,10)+date.substring(11,13)+date.substring(14,16)+date.substring(17,19)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getWho() {
        Messge messge=null;
        if(messges!=null) {
            messge = messges.get(messges.size() - 1);
            if(messge.getFrom().equals(User.userInfo.getId()))
                return "你：";
        }
        return "";
    }
}
