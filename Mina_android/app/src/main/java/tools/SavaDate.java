package tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import object.LinkMan;
import object.Messge;
import object.UserInfo;


public  class SavaDate {

    static Context context;

    public SavaDate(Context context){
        this.context=context;
    }

    //保存用户信息
    public static void saveUserInfo(UserInfo object) {
        SharedPreferences shared = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("user", oAuth_Base64);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取用户信息
    public static UserInfo getUserInfo() {
        SharedPreferences shared = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        UserInfo object= null;
        String productBase64 = shared.getString("user", null);
        if(productBase64==null) {
            return null;
        }
        // 读取字节  
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        // 封装到字节流  
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {  
            // 再次封装  
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象  
            object = (UserInfo) bis.readObject();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }

    /*//保存聊天记录
    public static void saveMsg(List<Messge> object,String id) {
        SharedPreferences shared = context.getSharedPreferences("msg", Context.MODE_PRIVATE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            SharedPreferences.Editor editor = shared.edit();
            editor.putString(id, oAuth_Base64);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取聊天记录
    public static List<Messge> getMsg(String id) {

        SharedPreferences shared = context.getSharedPreferences("msg", Context.MODE_PRIVATE);
        List<Messge> object= new ArrayList<Messge>();
        String productBase64 = shared.getString(id, null);
        if(productBase64==null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象
            object = (List<Messge>) bis.readObject();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }

    //保存一条信息
    public static void saveOnemessge(Messge messge){
        List<Messge> messges=getMsg(messge.getFrom());
        if(messges==null)
            messges=new ArrayList<Messge>();
        messges.add(messge);
        saveMsg(messges,messge.getFrom());
    }*/

    //删除用户信息
    public static void deleteData(){
        SharedPreferences shared = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();
        shared= context.getSharedPreferences("msg", Context.MODE_PRIVATE);
        editor = shared.edit();
        editor.clear();
        editor.commit();
    }


    //保存联系人
    public static void saveLinkMan(List<LinkMan> linkMen){
        SharedPreferences shared = context.getSharedPreferences("linkMan", Context.MODE_PRIVATE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(linkMen);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("linkMan", oAuth_Base64);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取联系人
    public static List<LinkMan> getLinkMan(){
        SharedPreferences shared = context.getSharedPreferences("linkMan", Context.MODE_PRIVATE);
        List<LinkMan> linkMan= null;
        String productBase64 = shared.getString("linkMan", null);
        if(productBase64==null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象
            linkMan = (List<LinkMan>) bis.readObject();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("SavaDate","保存的联系人："+linkMan.size());
        return linkMan;
    }

    //保存或更新一个联系人
    public static boolean saveOneMan(LinkMan linkMan){
        List<LinkMan> linkMens=getLinkMan();
        if(linkMens==null)
            linkMens=new ArrayList<LinkMan>();
        if(linkMens.contains(linkMan))
            linkMens.remove(linkMan);
        linkMens.add(linkMan);
        saveLinkMan(linkMens);
        Log.e("SavaDate","保存的联系人：执行完了："+linkMens.get(0).getMessges().size());
        return true;
    }



}  