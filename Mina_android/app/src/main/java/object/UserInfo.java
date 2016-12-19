package object;

import java.io.Serializable;

/**
 * Created by Tyhj on 2016/12/18.
 */

public class UserInfo implements Serializable{
     String userId;
     String userName;
     String pwd;

    public UserInfo(String userId,String pwd) {
        this.userId=userId;
        this.pwd=pwd;
    }

    public  String getId() {
        return userId;
    }

    public  void setId(String userId) {
        this.userId = userId;
    }

    public  String getName() {
        return userName;
    }

    public  void setName(String userName) {
        this.userName = userName;
    }

    public  String getPwd() {
        return pwd;
    }

    public  void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
