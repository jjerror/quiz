package com.example.joshua.quiz;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Android version of the Otto Event bus
 * <p/>
 * Added postOnMain for posting to default bus when not in main thread
 */
public class AndroidBus extends Bus {
    // make sure only posting event to main thread for the default bus
    private static final AndroidBus BUS = new AndroidBus(ThreadEnforcer.MAIN);

    private final Handler mainThread = new Handler(Looper.getMainLooper());

    private AndroidBus(ThreadEnforcer enforcer) {
        super(enforcer);
    }

    /**
     * Factory function to get instance
     *
     * @return the instance of the event bus
     */
    public static AndroidBus getInstance() {
        return BUS;
    }

    /**
     * Posts an event to all registered handlers.  This method will post the event in main thread
     *
     * @param event event to post.
     */
    public void postOnMain(final Object event) {
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                post(event);
            }
        });
    }
}