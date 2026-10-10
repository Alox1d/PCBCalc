package com.alox1d.pcbcalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alox1d.pcbcalc.utils.PreferencesHelper;

import static com.alox1d.pcbcalc.utils.PreferencesHelper.getDefaults;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    private final String DISABLE_SPLASH = PreferencesHelper.DISABLE_SPLASH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        initializeAnim();

        initializeMainActivity();


    }

    private void initializeMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        boolean isSplashDisabled = getDefaults(DISABLE_SPLASH, this);
        if (!isSplashDisabled) {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(getResources().getInteger(R.integer.anim_duration_long)); // todo 5000
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.start();
        } else {
            startActivity(intent);
            finish();
        }

    }

    private void initializeAnim() {
        textView = (TextView) findViewById(R.id.textViewSplash);
        imageView = (ImageView) findViewById(R.id.imageViewSplash);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);

        textView.startAnimation(anim);
        imageView.startAnimation(anim);
    }
}
