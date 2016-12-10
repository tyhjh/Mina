package tools;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public  class SavaDate {

    static Context context;

    private static SavaDate savaDate=new SavaDate();

    public static SavaDate getInstance(Context context){
        SavaDate.context=context;
        if(savaDate==null)
            savaDate=new SavaDate();
        return savaDate;
    }

    public static void saveObject(Object object, String name) {
        SharedPreferences shared = context.getSharedPreferences("name", Context.MODE_PRIVATE);
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
            editor.putString(name, oAuth_Base64);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    public static Object getObject(String name) {
        SharedPreferences shared = context.getSharedPreferences("name", Context.MODE_PRIVATE);
        Object object= null;
        String productBase64 = shared.getString(name, null);
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
            object = (Object) bis.readObject();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }

}  