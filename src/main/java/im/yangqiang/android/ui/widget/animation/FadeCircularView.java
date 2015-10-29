package im.yangqiang.android.ui.widget.animation;

import android.content.Context;
import android.util.AttributeSet;

import im.yangqiang.android.ui.widget.animation.sprite.FullCircularSprite;

/**
 * 自动填充和取消填充的圆
 */
public class FadeCircularView extends AnimationView
{
    public FadeCircularView(Context context)
    {
        super(context);
    }

    public FadeCircularView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FadeCircularView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public FadeCircularView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private FullCircularSprite mFullCircularView;

    @Override
    protected void onCreate()
    {
        super.onCreate();
        mFullCircularView = new FullCircularSprite(this);
        addSprite(mFullCircularView);
    }

    public boolean isAnimationFinish()
    {
        return mFullCircularView.isAnimationFinish();
    }

    public void close()
    {
        mFullCircularView.close();
    }

    public void toggleOpen()
    {
        mFullCircularView.toggleOpen();
    }

    public void open()
    {
        mFullCircularView.open();
    }
}
