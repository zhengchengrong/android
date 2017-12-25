package com.zcr.anim01;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_hello;
    private TextView tv_hello2;
    private TextView tv_hello3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // ---------------- ValueAnimator基本应用 ----------------------

        ValueAnimator anim = ValueAnimator.ofFloat(0f,5f,3f,10f);
        anim.setDuration(1000);
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue=(float)animation.getAnimatedValue();
                Log.d("TAG","current value is"+currentValue);
            }
        });

        // ---------------- ObjectAnimator基本应用 ----------------------

        tv_hello = this.findViewById(R.id.tv_hello);
        ObjectAnimator animator = ObjectAnimator.ofFloat(tv_hello,"alpha",1f,0f,1f);
        animator.setDuration(3000);
        animator.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(tv_hello,"scaleY",1f,3f,1f);
        animator2.setDuration(3000);
        animator2.start();

        // ---------------- 组合动画基本应用 ----------------------
        tv_hello2 = this.findViewById(R.id.tv_hello2);
        ObjectAnimator moveIn = ObjectAnimator.ofFloat(tv_hello2, "translationX", -500f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(tv_hello2, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(tv_hello2, "alpha", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(fadeInOut).after(moveIn);
        animSet.setDuration(5000);
        animSet.start();

        // ---------------- xml动画基本应用 ----------------------
        tv_hello3 = this.findViewById(R.id.tv_hello3);
        Animator animatorXml = AnimatorInflater.loadAnimator(this,R.animator.anim_file);
        animatorXml.setTarget(tv_hello3);
        animatorXml.start();
    }
}
