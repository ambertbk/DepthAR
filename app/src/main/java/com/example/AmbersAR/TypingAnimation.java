package com.example.AmbersAR;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class TypingAnimation extends TextView {
    private CharSequence text;
    private int index;
    private long delay = 150; //ms

    public TypingAnimation(Context context) {
        super(context);
    }

    public TypingAnimation(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    private Handler handler = new Handler();

    private Runnable characterAdder;
    {
        characterAdder = new Runnable() {

            @Override
            public void run() {
                setText(text.subSequence(0,index++));

                if (index < text.length()){
                    handler.postDelayed(characterAdder, delay);
                }
            }
        };
    }

    public void animateText(CharSequence txt){
        text = txt;
        index = 0;

        setText("");
        handler.removeCallbacks(characterAdder);
        handler.postDelayed(characterAdder, delay);
    }

    public void setCharacterDelayed(long delayTime){
        delay = delayTime;
    }
}
