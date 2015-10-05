package im.yangqiang.android.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import im.yangqiang.android.ui.R;

/**
 * 自动填充和取消填充的圆
 */
public class FadeCircularView extends View
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
    private float mChangeRadius = 0;
    /**
     * 内部圆最小半径
     */
    private float mMinRadius    = 0;
    /**
     * 是否默认打开
     */
    private boolean isOpen;

    public FadeCircularView(Context context)
    {
        super(context);
        init(null, 0);
    }

    public FadeCircularView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, 0);
    }

    public FadeCircularView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, com.android.databinding.library.baseAdapters.R.styleable.FadeCircularView, defStyle, 0);
        mBorderSize = a.getDimension(R.styleable.FadeCircularView_borderSize, 10);
        color = a.getColor(R.styleable.FadeCircularView_backgroundColor, Color.RED);
        innerColor = a.getColor(R.styleable.FadeCircularView_innerColor, Color.WHITE);
        mMinRadius = a.getDimension(R.styleable.FadeCircularView_innerMinRadius, 0);
        isOpen = a.getBoolean(R.styleable.FadeCircularView_isOpen, false);
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        int centerX = paddingLeft + contentWidth / 2;
        int centerY = paddingTop + contentHeight / 2;
        mPaint.setColor(color);
        canvas.drawCircle(centerX, centerY, getMinWidth(contentWidth, contentHeight) / 2, mPaint);
        mPaint.setColor(innerColor);
        canvas.drawCircle(centerX, centerY, mChangeRadius, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        }
        else
        {
            width = widthSize - paddingLeft - paddingRight;
        }
        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        }
        else
        {
            height = heightSize - paddingTop - paddingBottom;
        }
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        mRawRadius = getMinWidth(contentWidth, contentHeight) / 2 - mBorderSize;
        mChangeRadius = isOpen ? mRawRadius : 0;
    }

    private float getMinWidth(float width, float height)
    {
        return Math.min(width, height);
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
                postInvalidate();
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
                postInvalidate();
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
        timer.schedule(timerTask, 0, 1);
    }

    public void open()
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
                }
                mChangeRadius += 0.5;
                postInvalidate();
            }
        });
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
