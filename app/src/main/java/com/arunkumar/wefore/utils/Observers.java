package com.arunkumar.wefore.utils;

import android.util.Log;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class Observers {
    public static SingleObserver withSingleLogger() {
        return new SingleObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(this.getClass().getCanonicalName(), " SUBSCRIBED");
            }

            @Override
            public void onSuccess(Object o) {
                Log.d(this.getClass().getCanonicalName(), " EVENT " + o.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(this.getClass().getCanonicalName(), " ERROR " + e.toString());
            }
        };
    }
}
