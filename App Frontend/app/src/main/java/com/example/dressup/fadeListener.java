package com.example.dressup;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.Queue;

public class fadeListener implements Animation.AnimationListener {

    private Queue<View> views;
    private View currentView;

    public fadeListener(Queue views, View currentView){
        this.views = views;
        this.currentView = currentView;
    }

    private Animation createFade(){
        AlphaAnimation fadeAnimation = new AlphaAnimation(0.0f, 1.0f);
        if(views.size() > 0){
            fadeAnimation.setRepeatCount(1);
            fadeAnimation.setRepeatMode(Animation.REVERSE);
        }
        fadeAnimation.setDuration(3000);
        fadeAnimation.setAnimationListener(this);
        return fadeAnimation;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(views.size() == 0){
            return;
        }
        ((ViewManager)currentView.getParent()).removeView(currentView);
        currentView = views.remove();
        currentView.setVisibility(View.VISIBLE);
        currentView.startAnimation(createFade());
    }
}
