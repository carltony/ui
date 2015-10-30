package im.yangqiang.android.ui.widget.animation.sprite;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import java.util.Timer;
import java.util.TimerTask;

import im.yangqiang.android.ui.R;
import im.yangqiang.android.ui.widget.animation.AnimationView;

/**
 * 逆时针进度条
 * Created by Carlton on 10/28/15.
 */
public class ProgressCircularSprite extends SpriteView
{
    /**
     * 边框的宽度
     */
    private float mBorderSize;
    /**
     * 颜色
     */
    private int   color;
    /**
     * 内部的颜色
     */
    private int   innerColor;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 改变中角度
     */
    private float mChangeAngle = 0;
    /**
     * 起始角度
     */
    private float mStartAngle  = 0;
    /**
     * 结束角度
     */
    private float mEndAngle    = 0;
    /**
     * 内部圆最小半径
     */
    private float mMinRadius   = 30;
    /**
     * 时间周期
     */
    private long  startPeriod  = 2;

    public ProgressCircularSprite(AnimationView animationView)
    {
        super(animationView);
    }

    @Override
    public void init(AnimationView animationView, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        TypedArray a = animationView.getContext().obtainStyledAttributes(attrs, R.styleable.AnimationView, defStyleAttr, 0);
        mBorderSize = a.getDimension(R.styleable.AnimationView_borderSize, 10);
        color = a.getColor(R.styleable.AnimationView_backgroundColor, Color.RED);
        innerColor = a.getColor(R.styleable.AnimationView_innerColor, Color.WHITE);
        mMinRadius = a.getDimension(R.styleable.AnimationView_innerMinRadius, 0);
        mStartAngle = a.getFloat(R.styleable.AnimationView_startAngle, 0);
        mEndAngle = a.getFloat(R.styleable.AnimationView_endAngle, 360);
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        int paddingLeft = getView().getPaddingLeft();
        int paddingTop = getView().getPaddingTop();
        int paddingRight = getView().getPaddingRight();
        int paddingBottom = getView().getPaddingBottom();

        int contentWidth = getView().getWidth() - paddingLeft - paddingRight;
        int contentHeight = getView().getHeight() - paddingTop - paddingBottom;
        int centerX = paddingLeft + contentWidth / 2;
        int centerY = paddingTop + contentHeight / 2;
        mPaint.setColor(color);
        canvas.drawArc(new RectF(paddingLeft, paddingTop, paddingLeft + contentWidth, paddingTop + contentHeight), mStartAngle, mChangeAngle, true, mPaint);
        mPaint.setColor(innerColor);
        canvas.drawCircle(centerX, centerY, mMinRadius, mPaint);
    }

    private boolean isAnimationFinish = true;

    /**
     * 开始动画
     */
    public void start()
    {
        if (!isAnimationFinish)
        {
            return;
        }
        isAnimationFinish = false;
        mChangeAngle = 0;
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (mChangeAngle >= mEndAngle)
                {
                    cancel();
                    isAnimationFinish = true;
                }
                mChangeAngle += 0.5;
                getView().postInvalidate();
            }
        }, 0, startPeriod);
    }

    public void finish()
    {
        setDrawEnable(true);
        mChangeAngle = 360;
        getView().postInvalidate();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public void setInnerColor(int innerColor)
    {
        this.innerColor = innerColor;
    }

    public void setStartPeriod(long startPeriod)
    {
        this.startPeriod = startPeriod;
    }
}
