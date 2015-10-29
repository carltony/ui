package im.yangqiang.android.ui.widget.animation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayDeque;

import im.yangqiang.android.ui.widget.animation.sprite.SpriteView;

/**
 * 能够组合各种SpriteView动画控件
 * Created by Carlton on 10/28/15.
 */
public class AnimationView extends View
{
    private ArrayDeque<SpriteView> mSpriteViews = new ArrayDeque<>();

    public AnimationView(Context context)
    {
        super(context);
        init(context, null, 0, 0);
    }

    public AnimationView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void addSprite(SpriteView view)
    {
        mSpriteViews.add(view);
    }

    /**
     * 初始化数据
     *
     * @param context      Context
     * @param attrs        属性
     * @param defStyleAttr 风格属性
     * @param defStyleRes  风格资源
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        onCreate();
        for (SpriteView view : mSpriteViews)
        {
            view.init(this, attrs, defStyleAttr, defStyleRes);
        }
    }

    protected void onCreate()
    {

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        for (SpriteView view : mSpriteViews)
        {
            if (view.isOnDraw())
            {
                view.onDraw(canvas);
            }
        }
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
        if (min == 0)
        {
            min = 60;
        }
        setMeasuredDimension(min, min);

        for (SpriteView view : mSpriteViews)
        {
            view.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
