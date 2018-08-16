package com.fih.idx.deriklibrary.utils.click;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by derik on 18-3-21.
 * Email: weilai0314@163.com
 */

public class MultiClickButton extends android.support.v7.widget.AppCompatButton {
    private static long millis = 700;//毫秒

    public void setOnDoubleClickListener(OnDoubleClickListener listener) {
        setOnClickListener(listener);
    }

    public void setOnThreeClickListener(OnThreeClickListener listener) {
        setOnClickListener(listener);
    }

    public void setOnFourClickListener(OnFourClickListener listener) {
        setOnClickListener(listener);
    }

    public void setOnFiveClickListener(OnFiveClickListener listener) {
        setOnClickListener(listener);
    }

    public MultiClickButton(Context context) {
        this(context, null);
    }

    public MultiClickButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiClickButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static abstract class OnDoubleClickListener implements View.OnClickListener {
        long[] hits = new long[2];

        @Override
        public void onClick(View v) {
            System.arraycopy(hits, 1, hits, 0, hits.length - 1);
            hits[hits.length - 1] = SystemClock.uptimeMillis();

            if (millis >= hits[hits.length - 1] - hits[0]) {
                onDoubleClickListener(v);
            }
        }

        public abstract void onDoubleClickListener(View v);
    }

    public static abstract class OnThreeClickListener implements View.OnClickListener {
        long[] hits = new long[3];

        @Override
        public void onClick(View v) {
            System.arraycopy(hits, 1, hits, 0, hits.length - 1);
            hits[hits.length - 1] = SystemClock.uptimeMillis();

            if (millis >= hits[hits.length - 1] - hits[0]) {
                onThreeClickListener(v);
            }
        }

        public abstract void onThreeClickListener(View v);
    }

    public static abstract class OnFourClickListener implements View.OnClickListener {
        long[] hits = new long[4];

        @Override
        public void onClick(View v) {
            System.arraycopy(hits, 1, hits, 0, hits.length - 1);
            hits[hits.length - 1] = SystemClock.uptimeMillis();

            if (millis >= hits[hits.length - 1] - hits[0]) {
                onFourClickListener(v);
            }
        }

        public abstract void onFourClickListener(View v);
    }

    public static abstract class OnFiveClickListener implements View.OnClickListener {
        long[] hits = new long[5];

        @Override
        public void onClick(View v) {
            System.arraycopy(hits, 1, hits, 0, hits.length - 1);
            hits[hits.length - 1] = SystemClock.uptimeMillis();

            if (millis >= hits[hits.length - 1] - hits[0]) {
                onFiveClickListener(v);
            }
        }

        public abstract void onFiveClickListener(View v);
    }

}
