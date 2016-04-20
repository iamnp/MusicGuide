package iamnp.musicguide;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * FloatingActionButton with AnimationEnabled property.
 */
public class MyFloatingActionButton extends FloatingActionButton {
    public boolean AnimationEnabled = true;

    public MyFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}