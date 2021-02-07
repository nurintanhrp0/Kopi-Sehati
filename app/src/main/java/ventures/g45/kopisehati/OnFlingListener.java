package ventures.g45.kopisehati;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

public abstract class OnFlingListener implements View.OnTouchListener {

    Context context;

    public OnFlingListener(Context context) {
        this.context = context;
    }

    private final GestureDetectorCompat gdt = new GestureDetectorCompat(context, new GestureListener());

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public abstract void onBottomToTop();

    public abstract void onTopToBottom();

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int MIN_DISTANCE = 100;
        private static final int SWIPE_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e1.getX() - e2.getX() > MIN_DISTANCE && Math.abs(velocityX) > SWIPE_VELOCITY) {
                onRightToLeft();
                return true;
            } else if (e2.getX() - e1.getX() > MIN_DISTANCE && Math.abs(velocityX) > SWIPE_VELOCITY) {
                onLeftToRight();
                return true;
            }
            if (e1.getY() - e2.getY() > MIN_DISTANCE && Math.abs(velocityY) > SWIPE_VELOCITY) {
                onBottomToTop();
                return true;
            } else if (e2.getY() - e1.getY() > MIN_DISTANCE && Math.abs(velocityY) > SWIPE_VELOCITY) {
                onTopToBottom();
                return true;
            }

            return false;
        }
    }

    public abstract void onLeftToRight();

    public abstract void onRightToLeft();


}
