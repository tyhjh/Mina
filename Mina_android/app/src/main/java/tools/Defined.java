package tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import com.tyhj.myfist_2016_6_29.MyTime;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tyhj on 2016/12/5.
 */

public class Defined {
    //是否有网络
    public static boolean isIntenet(Context context){
        ConnectivityManager con=(ConnectivityManager)context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi||internet){
            return true;
        }else {
            return false;
        }
    }

    //设置控件轮廓
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ViewOutlineProvider getOutline(boolean b, final int pading, final int circularBead){
        if(b) {
            return  new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    final int margin = Math.min(view.getWidth(), view.getHeight()) / pading;
                    outline.setOval(margin, margin, view.getWidth() - margin, view.getHeight() - margin);
                }
            };
        }else {
            return new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    final int margin = Math.min(view.getWidth(), view.getHeight()) / pading;
                    outline.setRoundRect(margin, margin, view.getWidth() - margin, view.getHeight() - margin, circularBead);
                    //outline.setOval(margin, margin, view.getWidth() - margin, view.getHeight() - margin);
                }
            };
        }

    }

    //从uri得到path
    public static String getFilePathFromContentUri(Uri uri, ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = contentResolver.query(uri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    //图片压缩
    public static void ImgCompress(String filePath,File newFile,int IMAGE_SIZE) {
        //图片质量
        int imageMg=100;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //规定要压缩图片的分辨率
        options.inSampleSize = calculateInSampleSize(options,1080,1920);
        options.inJustDecodeBounds = false;
        Bitmap bitmap= BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, imageMg, baos);
        //如果文件大于100KB就进行质量压缩，每次压缩比例增加百分之五
        while (baos.toByteArray().length / 1024 > IMAGE_SIZE&&imageMg>60){
            baos.reset();
            imageMg-=5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, imageMg, baos);
        }
        //然后输出到指定的文件中
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(newFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //图片压缩
    public static void ImgCompress(String filePath,File newFile,int x,int y,int size) {
        int imageMg=100;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //规定要压缩图片的分辨率
        options.inSampleSize = calculateInSampleSize(options,x,y);
        options.inJustDecodeBounds = false;
        Bitmap bitmap= BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, imageMg, baos);
        //如果文件大于100KB就进行质量压缩，每次压缩比例增加百分之五
        while (baos.toByteArray().length / 1024 > size&&imageMg>50){
            baos.reset();
            imageMg-=5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, imageMg, baos);
        }
        //然后输出到指定的文件中
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(newFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //文件复制
    public static void copyFile(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    //判断 服务 是否运行
    public static boolean isServiceRun(Context mContext, String className) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(40);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

    //手机号是否正确
    public static boolean isMobileNO(String mobiles,Context context) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        boolean is=m.matches();
        if(!is)
            Toast.makeText(context,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
        return is;
    }

    //邮箱是否正确
    public static boolean isEmail(String email,Context context) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        boolean is=m.matches();
        if(!is)
            Toast.makeText(context,"请输入正确的Email地址",Toast.LENGTH_SHORT).show();
        return is;
    }

    //访问URL并获取返回信息
    public static JSONObject getJson(String data, String url, String way) {
        HttpURLConnection conn = null;
        URL mURL = null;
        if (way.equals("GET")) {
            try {
                if(data==null)
                    mURL = new URL(url);
                else
                    mURL = new URL(url + "?" + data);
                conn = (HttpURLConnection) mURL.openConnection();
                //conn.addRequestProperty("头","这里是添加头");
                conn.setRequestMethod("GET");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(30000);
                InputStream is = conn.getInputStream();
                String state = For2mat.getInstance().inputStream2String(is);
                //Log.e("Tag", state);
                JSONObject jsonObject = new JSONObject(state);
                if (jsonObject != null)
                    return jsonObject;
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (way.equals("POST")) {
            try {
                mURL = new URL(url);
                conn = (HttpURLConnection) mURL.openConnection();
                //conn.addRequestProperty("头","我就是头");
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(30000);
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes());
                out.flush();
                out.close();
                int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方
                if (responseCode == 200) {
                    InputStream is = conn.getInputStream();
                    String state = For2mat.getInstance().inputStream2String(is);
                    Log.e("Tag",state);
                    JSONObject jsonObject = new JSONObject(state);
                    if (jsonObject != null)
                        return jsonObject;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }

    //获取验证码
    public static String getCode(int length){
        String code="";
        Random random=new Random(System.currentTimeMillis());
        for(int i=0;i<length;i++){
            code=code+random.nextInt(10);
        }
        return code;
    }

    //获取时间
    public static String getTime(int time){
        int ca=getTime()-time;
        if(ca<10){
            return null;
        }else if(ca>10){
            return "刚刚";
        }
        return null;
    }

    public static String getTime2(int time){
        int ca=getTime()-time;
        int ca2=(getTime()/10000)-(time/10000);
        String str=time+"";
        if(ca2==1){
            return ("昨天 • "+str.substring(4,6)+":"+str.substring(6,8));
        }else if(ca2>1){
            return (str.substring(0,2)+"月"+str.substring(2,4)+"日 "+str.substring(4,6)+":"+str.substring(6,8));
        }else if(ca<=10){
            return ("刚刚");
        }else if(ca<500){
            return (str.substring(4,6)+":"+str.substring(6,8));
        }else {
            return ("今天 • "+str.substring(4,6)+":"+str.substring(6,8));
        }
    }

    public static int getTime(){
        MyTime myTime=new MyTime();
        return Integer.parseInt(myTime.getMonth()+myTime.getDays()+myTime.getHour()+myTime.getMinute());
    }
}
