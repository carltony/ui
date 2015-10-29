package im.yangqiang.android.ui.widget.animation.sprite;

import android.graphics.Canvas;
import android.util.AttributeSet;

import im.yangqiang.android.ui.widget.animation.AnimationView;

/**
 * 精灵视图，和AnimationView配合能够组合成一个连续的动画效果
 * Created by Carlton on 10/28/15.
 */
public abstract class SpriteView
{
    private final AnimationView mAnimationView;

    public SpriteView(AnimationView animationView)
    {
        mAnimationView = animationView;
    }
    public AnimationView getView()
    {
        return mAnimationView;
    }
    public abstract void init(AnimationView animationView, AttributeSet attrs, int defStyleAttr, int defStyleRes);

    public abstract void onDraw(Canvas canvas);

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

    }
}
