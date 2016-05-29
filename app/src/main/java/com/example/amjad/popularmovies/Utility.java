package com.example.amjad.popularmovies;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by amjad on 29/5/16.
 */
public class Utility {

    public static void enableScrolling(ListView lv) {
        // To get the video list view to scroll. Thanks StackOverflow
        lv.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        // End scroll hack
    }
}
