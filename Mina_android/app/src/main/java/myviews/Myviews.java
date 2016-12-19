package myviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tyhj on 2016/12/15.
 */

public class Myviews extends View {

    float touchX=0;
    float touchY=0;
    boolean isTouch;
    Paint paint;
    int width;
    int height;
    List<Mycircle> mycircleList;
    List<Myrect> myrects;
    int distance=100;
    float speed=1.7f;
    int count=55;
    float radius=3.5f;
    int gap=1;
    int length=8;
    public Myviews(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public Myviews(Context context) {
        super(context);
    }
    public Myviews(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        paint=new Paint();
        paint.setAntiAlias(true);
        //设置颜色
        paint.setColor(Color.WHITE);
        //设置背景
        //canvas.drawColor(Color.WHITE);
        for(int i=0;i<mycircleList.size();i++){
            if(mycircleList.get(i).startX>width||mycircleList.get(i).startX<0) {
                mycircleList.get(i).speedX = -mycircleList.get(i).speedX;
            }
            if(mycircleList.get(i).startY>height||mycircleList.get(i).startY<0) {
                mycircleList.get(i).speedY = -mycircleList.get(i).speedY;
            }
            mycircleList.get(i).startX=mycircleList.get(i).startX+mycircleList.get(i).speedX;
            mycircleList.get(i).startY=mycircleList.get(i).startY+mycircleList.get(i).speedY;
            //画圆
            canvas.drawCircle(mycircleList.get(i).startX,mycircleList.get(i).startY,mycircleList.get(i).radius,mycircleList.get(i).paint);
        }
        //画连接线
        for(int i=0;i<mycircleList.size()-1;i++){
            for(int j=i+1;j<mycircleList.size();j++){
                float x=mycircleList.get(i).startX-mycircleList.get(j).startX;
                float y=mycircleList.get(i).startY-mycircleList.get(j).startY;
                if(x*x+y*y<mycircleList.get(i).distance*distance*distance){
                    canvas.drawLine(mycircleList.get(i).startX,mycircleList.get(i).startY,
                            mycircleList.get(j).startX,mycircleList.get(j).startY,mycircleList.get(i).paint);
                }
            }
        }
        //点击时候处理
        if(isTouch){
            canvas.drawCircle(touchX,touchY,5,paint);
            for(int i=0;i<mycircleList.size();i++){
                float x=mycircleList.get(i).startX-touchX;
                float y=mycircleList.get(i).startY-touchY;
                if(x*x+y*y<12*distance*distance){
                    canvas.drawLine(mycircleList.get(i).startX,mycircleList.get(i).startY,
                            touchX,touchY,mycircleList.get(i).paint);
                }
            }
        }
        int x=width/4;
        int y=height/4;
        for(int i=0;i<myrects.size();i++){
            canvas.drawRect(x+myrects.get(i).addx,y+myrects.get(i).addy,x+myrects.get(i).addx+length,y+myrects.get(i).addy+length,paint);
        }



        //刷新会重新执行这个方法，所以一直执行下去------重点
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouch=true;
                touchX=event.getX();
                touchY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                isTouch=true;
                touchX=event.getX();
                touchY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                isTouch=false;
                touchX=-1;
                touchY=-1;
                break;
        }
        return true;
    }

    private void initialize() {
        myrects=new ArrayList<Myrect>();
        mycircleList=new ArrayList<Mycircle>();

        addRect(0,0);
        addRect(20,0);
        addRect(40,0);
        addRect(60,0);
        addRect(80,0);

        addRect(80,20);

        addRect(0,20);
        addRect(0,40);
        addRect(0,60);

        addRect(20,60);
        addRect(40,60);
        addRect(60,60);
        addRect(80,60);

        addRect(80,80);
        addRect(80,100);
        addRect(80,120);

        addRect(60,120);
        addRect(40,120);
        addRect(20,120);
        addRect(0,120);

        addRect(0,100);


        addRect(170,0);
        addRect(170,20);
        addRect(170,40);
        addRect(170,60);
        addRect(170,80);
        addRect(170,100);
        addRect(170,120);


        addRect(190,0);

        addRect(200,20);
        addRect(210,40);
        addRect(220,60);

        addRect(240,60);

        addRect(250,40);
        addRect(260,20);
        addRect(270,0);

        addRect(290,0);
        addRect(290,20);
        addRect(290,40);
        addRect(290,60);
        addRect(290,80);
        addRect(290,100);
        addRect(290,120);

        addRect(380,0);
        addRect(380,20);
        addRect(380,40);
        addRect(380,60);
        addRect(380,80);
        addRect(380,100);
        addRect(380,120);

        addRect(400,0);
        addRect(420,0);
        addRect(440,0);
        addRect(460,0);
        addRect(480,0);

        addRect(400,120);
        addRect(420,120);
        addRect(440,120);
        addRect(460,120);
        addRect(480,120);

        addRect(480,100);
        addRect(480,80);
        addRect(480,60);

        addRect(460,60);
        addRect(440,60);
        addRect(420,60);

        addRect(420,80);

        Random random = new Random(System.currentTimeMillis());
        for (int i=0;i<count;i++) {
            int distance=1;
            int alpha = random.nextInt(60)+40;
            float radius = this.radius*random.nextFloat()+1.5f;
            float startX = random.nextInt(width);
            float startY = random.nextInt(height);
            float speedX = speed - 2*speed*random.nextFloat();
            float speedY = speed - 2*speed*random.nextFloat();

            if(speedX==0)
                speedX=0.5f;
            if(speedY==0)
                speedY=-0.5f;

            if(i%50==0)
                distance=2;
            else if(i%30==0)
                distance=2;
            else if(i%20==0)
                distance=2;

            if(radius<0.7f)
                distance=0;
            Mycircle mycircle=new Mycircle(radius,startX,startY,speedX,speedY,alpha,distance);
            mycircleList.add(mycircle);
        }
    }

    public  void addRect(int x,int y){
        x = gap*x/20+x;
        y=gap*y/20+y;
        Myrect myrect=new Myrect(x,y);
        myrects.add(myrect);
        myrect=new Myrect(x,y+length);
        myrects.add(myrect);
        myrect=new Myrect(x+length,y);
        myrects.add(myrect);
        myrect=new Myrect(x+length,y+length);
        myrects.add(myrect);
    }

    class Mycircle{
        int distance;
        Paint paint;
        float radius;
        float startX;
        float startY;
        float speedX;
        float speedY;
        int alpha;
        public Mycircle(float radius, float startX, float startY, float speedX, float speedY, int alpha,int distance) {
            this.radius = radius;
            this.startX = startX;
            this.startY = startY;
            this.speedX = speedX;
            this.speedY = speedY;
            this.alpha = alpha;
            paint=new Paint();
            paint.setAntiAlias(true);
            //设置颜色
            paint.setColor(Color.WHITE);
            paint.setAlpha(alpha);
            this.distance=distance;
        }
    }

    class Myrect{
        int addx;
        int addy;

        public Myrect(int addx,int addy) {
            this.addx =addx;
            this.addy=addy;
        }
    }

}
