package myviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by Tyhj on 2016/12/16.
 */

public class Snack extends View{
    int width=0;
    int height=0;
    int gap=3;
    int length=13;
    int speedx=0;
    int speedy=3;
    int speed=8;
    int x;
    int y;
    Random random;
    boolean newpoint=true;
    Myrect myrect;

    int controlx=0;
    int controly=0;
    Paint paint;
    List<Myrect> myrects;

    public Snack(Context context) {
        super(context);
    }

    public Snack(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Snack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取布局大小
        width=MeasureSpec.getSize(widthMeasureSpec);
        height=MeasureSpec.getSize(heightMeasureSpec);
        initialize();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(controlx,controly,controlx+80,controly+100,paint);
        canvas.drawRect(controlx-100,controly+100,controlx,controly+180,paint);
        canvas.drawRect(controlx+80,controly+100,controlx+180,controly+180,paint);
        canvas.drawRect(controlx,controly+180,controlx+80,controly+280,paint);

        if(myrects.get(0).equals(myrect)){
            newpoint=true;

            for(int i=0;i<myrects.size();i++){
                Log.e("size：",myrects.size()+"："+myrects.get(i).x+"："+myrects.get(i).y);
            }
        }


        for(int i=myrects.size()-1;i>=0;i--){
            if(newpoint){
                myrects.add(0,new Myrect(myrects.get(0).x + speedx,myrects.get(0).y + speedy));
                break;
            }
            if(i==0) {
                myrects.get(0).x = myrects.get(0).x + speedx;
                myrects.get(0).y = myrects.get(0).y + speedy;
            }else {
                paint.setColor(Color.BLACK);
                myrects.get(i).x=myrects.get(i-1).x;
                myrects.get(i).y=myrects.get(i-1).y;
                myrects.get(i).setSelf();
            }
        }

        for(int i=0;i<myrects.size();i++){
            if(i==0)
                paint.setColor(Color.RED);
            else
                paint.setColor(Color.BLACK);
            canvas.drawRect(myrects.get(i).x,myrects.get(i).y,myrects.get(i).x+length,myrects.get(i).y+length,paint);
        }

        canvas.drawRect(myrect.x,myrect.y,myrect.x+length,myrect.y+length,paint);


        if(newpoint){
            myrect=new Myrect(random.nextInt(width),random.nextInt(height/5*4));
            newpoint=false;
        }

        if(myrects.get(0).x>width||myrects.get(0).x<0){
            speedx=-speedx;
            newpoint=false;
            Myrect myrect=myrects.get(0);
            myrects.clear();
            myrects.add(myrect);
        }
        if(myrects.get(0).y>height||myrects.get(0).y<0){
            speedy=-speedy;
            newpoint=false;
            Myrect myrect=myrects.get(0);
            myrects.clear();
            myrects.add(myrect);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x=(int) event.getX();
        int y=(int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(x>controlx&&x<controlx+80&&y>controly&&y<controly+100) {
                    speedy = -speed;
                    speedx=0;
                }else if(x>controlx-100&&x<controlx&&y>controly+100&&y<controly+180) {
                    speedy = 0;
                    speedx=-speed;
                }if(x>controlx+80&&x<controlx+180&&y>controly+100&&y<controly+180) {
                speedy = 0;
                speedx=speed;
            }if(x>controlx&&x<controlx+80&&y>controly+180&&y<controly+280) {
                speedy =speed;
                speedx=0;
            }

                break;
            case MotionEvent.ACTION_MOVE:
                if(x>controlx&&x<controlx+80&&y>controly&&y<controly+100) {
                    speedy = -speed;
                    speedx=0;
                }else if(x>controlx-100&&x<controlx&&y>controly+100&&y<controly+180) {
                    speedy = 0;
                    speedx=-speed;
                }if(x>controlx+80&&x<controlx+180&&y>controly+100&&y<controly+180) {
                speedy = 0;
                speedx=speed;
            }if(x>controlx&&x<controlx+80&&y>controly+180&&y<controly+280) {
                speedy =speed;
                speedx=0;
            }
                break;
        }
        return true;
    }

    //初始化
    public void initialize(){
        length=width/30;
        speed=length/2;
        random = new Random(System.currentTimeMillis());
        myrect=new Myrect(((int)random.nextInt(width)/length)*length,((int)random.nextInt(height)/length)*length);
        x=(random.nextInt(width-200)+100)/length*length;
        y=(random.nextInt(height/5*4-200)+100)/length*length;
        controlx=width/2;
        controly=8*height/10;
        paint=new Paint();
        //去锯齿
        paint.setAntiAlias(true);
        //设置颜色
        paint.setColor(Color.BLACK);
        myrects=new ArrayList<Myrect>();
        myrects.add(new Myrect(x,y));
        myrects.add(new Myrect(x+length,y));
    }


    class Myrect{
        int x;
        int y;

        public Myrect(int addx, int addy) {
            this.x =addx;
            this.y=addy;
        }

        public void setSelf(){
            if(myrects!=null&&myrects.size()>2&&!this.equals(myrects.get(0))){

                    if (y > myrects.get(0).y)
                        y=gap+y;
                    else if(y < myrects.get(0).y)
                        y=-gap+y;
                    if(x>myrects.get(0).x)
                        x=gap+x;
                    else if(x<myrects.get(0).x)
                        x=-gap+x;

            }

        }

        @Override
        public boolean equals(Object obj) {
            Myrect myrect1=(Myrect) obj;
            if((abs(myrect1.x-this.x)<length/3*2&& abs(myrect1.y-this.y)<length/3*2)||(abs(myrect1.y-this.y)<length/3*2&& abs(myrect1.x-this.x)<length/3*2))
                return true;
            else
                return false;
        }
    }

}
