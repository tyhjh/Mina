package myviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Tyhj on 2016/12/18.
 */

public class OutlineBtn extends Button {
    Paint paint=new Paint();
    float width,height;
    int border=18;
    int color=Color.WHITE;
    public OutlineBtn(Context context) {
        super(context);
    }

    public OutlineBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutlineBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=MeasureSpec.getSize(widthMeasureSpec);
        height=MeasureSpec.getSize(heightMeasureSpec);
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float border;
        border=Math.min(width,height)/this.border;
        canvas.drawRect(0,0,width,border,paint);
        canvas.drawRect(0,0,border,height,paint);
        canvas.drawRect(width-border,0,width,height,paint);
        canvas.drawRect(0,height-border,width,height,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paint.setColor(Color.YELLOW);
                break;
            case MotionEvent.ACTION_UP:
                paint.setColor(color);
                break;
        }
        postInvalidate();
        return true;
    }
}
