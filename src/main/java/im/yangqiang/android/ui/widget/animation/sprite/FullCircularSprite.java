package im.yangqiang.android.ui.widget.animation.sprite;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.Timer;
import java.util.TimerTask;

import im.yangqiang.android.ui.R;
import im.yangqiang.android.ui.widget.animation.AnimationView;

/**
 * 自动填充和取消填充的圆
 * Created by Carlton on 10/28/15.
 */
public class FullCircularSprite extends SpriteView
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
     * 半径
     */
    private float mRawRadius    = -1;
    /**
     * 改变中的半径大小
     */
    private float mChangeRadius = 0;
    /**
     * 内部圆最小半径
     */
    private float mMinRadius    = 30;
    /**
     * 是否默认打开
     */
    private boolean isOpen;

    public FullCircularSprite(AnimationView animationView)
    {
        super(animationView);
    }

    @Override
    public void init(AnimationView animationView, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        final TypedArray a = animationView.getContext().obtainStyledAttributes(attrs, R.styleable.AnimationView, defStyleAttr, 0);
        mBorderSize = a.getDimension(R.styleable.AnimationView_borderSize, 10);
        color = a.getColor(R.styleable.AnimationView_backgroundColor, Color.RED);
        innerColor = a.getColor(R.styleable.AnimationView_innerColor, Color.WHITE);
        mMinRadius = a.getDimension(R.styleable.AnimationView_innerMinRadius, 0);
        mChangeRadius = mMinRadius;
        isOpen = a.getBoolean(R.styleable.AnimationView_isOpen, false);
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        // allocations per draw cycle.
        int paddingLeft = getView().getPaddingLeft();
        int paddingTop = getView().getPaddingTop();
        int paddingRight = getView().getPaddingRight();
        int paddingBottom = getView().getPaddingBottom();

        int contentWidth = getView().getWidth() - paddingLeft - paddingRight;
        int contentHeight = getView().getHeight() - paddingTop - paddingBottom;
        int centerX = paddingLeft + contentWidth / 2;
        int centerY = paddingTop + contentHeight / 2;
        mPaint.setColor(color);
        canvas.drawCircle(centerX, centerY, getMinWidth(contentWidth, contentHeight) / 2, mPaint);
        mPaint.setColor(innerColor);
        canvas.drawCircle(centerX, centerY, Math.max(mChangeRadius, mMinRadius), mPaint);
    }

    private float getMinWidth(float width, float height)
    {
        return Math.min(width, height);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int paddingLeft = getView().getPaddingLeft();
        int paddingTop = getView().getPaddingTop();
        int paddingRight = getView().getPaddingRight();
        int paddingBottom = getView().getPaddingBottom();
        int contentWidth = getView().getWidth() - paddingLeft - paddingRight;
        int contentHeight = getView().getHeight() - paddingTop - paddingBottom;
        mRawRadius = getMinWidth(contentWidth, contentHeight) / 2 - mBorderSize;
        mChangeRadius = isOpen ? mRawRadius : 0;
    }

    private boolean isAnimationFinish = true;

    public boolean isAnimationFinish()
    {
        return isAnimationFinish;
    }

    public void close()
    {
        if (!isAnimationFinish)
        {
            return;
        }
        isOpen = false;
        isAnimationFinish = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (mChangeRadius <= mMinRadius)
                {
                    cancel();
                    isAnimationFinish = true;
                }
                mChangeRadius -= 0.5;
                getView().postInvalidate();
            }
        }, 0, 2);
    }

    public void toggleOpen()
    {
        openStart(new TimerTask()
        {
            @Override
            public void run()
            {
                if (mChangeRadius > mRawRadius)
                {
                    cancel();
                    isAnimationFinish = true;
                    close();
                }
                mChangeRadius += 0.5;
                getView().postInvalidate();
            }
        });
    }

    private void openStart(TimerTask timerTask)
    {
        if (!isAnimationFinish)
        {
            return;
        }
        isOpen = true;
        isAnimationFinish = false;
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 2);
    }

    public void open()
    {
        mChangeRadius = mMinRadius;
        openStart(new TimerTask()
        {
            @Override
            public void run()
            {
                if (mChangeRadius > mRawRadius)
                {
                    cancel();
                    isAnimationFinish = true;
                }
                mChangeRadius += 0.5;
                getView().postInvalidate();
            }
        });
    }

    public void finish()
    {
        mChangeRadius = mRawRadius + 1;
        getView().postInvalidate();
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public int getInnerColor()
    {
        return innerColor;
    }

    public void setInnerColor(int innerColor)
    {
        this.innerColor = innerColor;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }
}
