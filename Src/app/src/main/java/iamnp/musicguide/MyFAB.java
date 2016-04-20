package iamnp.musicguide;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * FloatingActionButton with AnimationEnabled property.
 */
public class MyFAB extends FloatingActionButton {
    private boolean mAnimationEnabled = true;

    public MyFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean getAnimationEnabled() {
        return mAnimationEnabled;
    }

    public void setAnimationEnabled(boolean enabled) {
        mAnimationEnabled = enabled;
    }
}