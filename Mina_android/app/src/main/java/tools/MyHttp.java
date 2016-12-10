package tools;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Tyhj on 2016/12/5.
 */

public class MyHttp {

    static String URL="http://192.168.43.18:8080";

    private static final String CHARSET = "utf-8"; // 设置编码


    public static String test1(){
        String url=URL+"/Talk/talk/sign_up";
        JSONObject jsonObject=Defined.getJson("username=嗨&password=你好",url,"POST");
        try {
            String code=jsonObject.getString("code");
            String name=jsonObject.getString("name");
            String pas=jsonObject.getString("pas");
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String test2(){
        String url=URL+"/Talk/talk/setHeadImage";
        File file= new File(Environment.getExternalStorageDirectory()+"/ASchollMsg/ppt.JPEG");
        if(!file.exists())
            return "没有此文件";
        String file2Str=For2mat.getInstance().file2String(file);
        JSONObject jsonObject=Defined.getJson("username=ppt.JPEG&headImage="+file2Str,url,"POST");
        if(jsonObject!=null)
            return jsonObject.toString();
        return null;
    }


    //上传文件
    public static String upLoad(String url, File file) {
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        HttpURLConnection conn = null;
        java.net.URL mURL = null;
            try {
                mURL = new URL(url);
                conn = (HttpURLConnection) mURL.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(10000);
                conn.setDoInput(true); // 允许输入流
                conn.setDoOutput(true); // 允许输出流
                conn.setUseCaches(false); // 不允许使用缓存
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);
                OutputStream out = conn.getOutputStream();


                DataOutputStream dos = new DataOutputStream(out);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);


                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */

                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getName() + "\"" + LINE_END);

                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);

                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();


                int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方
                if (responseCode == 200) {
                    InputStream input = conn.getInputStream();
                    String state = For2mat.getInstance().inputStream2String(input);
                    //Log.e("Tag",state);
                    return state;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
    }

}
