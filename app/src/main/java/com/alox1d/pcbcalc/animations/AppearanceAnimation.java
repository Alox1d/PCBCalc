package com.alox1d.pcbcalc.animations;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by Alox1d on 13.04.2018.
 */

public class AppearanceAnimation {
    private int countOfTimes;

    public AppearanceAnimation(int countOfTimes){
        this.countOfTimes = countOfTimes;
    }

    public Animation animate(final float fromAlpha, final float toAlpha, final View view) {
        AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
        if (countOfTimes == 0) return animation;
        animation.setStartOffset(0);
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                countOfTimes--;
                animate(1 - fromAlpha, 1 - toAlpha, view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(animation);
        return animation;
    }
}
