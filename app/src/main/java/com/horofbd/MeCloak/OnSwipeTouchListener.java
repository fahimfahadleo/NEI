package com.horofbd.MeCloak;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;



public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    Context context;
    String action;
    View vi;

    public OnSwipeTouchListener(Context ctx,String action,View vi) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.context = ctx;
        this.action = action;
        this.vi = vi;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 1;
        private static final int SWIPE_VELOCITY_THRESHOLD = 1;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            if(action.equals("R")){

                            }
                            onSwipeRight();
                        } else {
                            if(action.equals("L")){

                            }

                            onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        if(action.equals("B")){

                        }
                        onSwipeBottom();
                    } else {
                        if(action.equals("T")){

                        }
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        Toast.makeText(context, "swipe right", Toast.LENGTH_SHORT).show();
    }

    public void onSwipeLeft() {
        Toast.makeText(context, "swipe left", Toast.LENGTH_SHORT).show();
    }

    public void onSwipeTop() {

       // Toast.makeText(context, "swipe top", Toast.LENGTH_SHORT).show();
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.goup);

        for(int index = 0; index < ((ViewGroup) vi).getChildCount(); index++) {
            View nextChild = ((ViewGroup) vi).getChildAt(index);
            nextChild.startAnimation(animation);
            nextChild.setVisibility(View.VISIBLE);
        }



    }

    public void onSwipeBottom() {
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.godown);

        for(int index = 0; index < ((ViewGroup) vi).getChildCount(); index++) {
            View nextChild = ((ViewGroup) vi).getChildAt(index);
            nextChild.startAnimation(animation);
            nextChild.setVisibility(View.INVISIBLE);
        }
    }
}