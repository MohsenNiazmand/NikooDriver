package com.akaf.nikoodriver.feature.main.home;

import android.annotation.SuppressLint;

import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MethodCallsLogger;

@SuppressLint("RestrictedApi")
public class HomeActivity_LifecycleAdapter implements GeneratedAdapter {

    final HomeActivity mReceiver;
    public HomeActivity_LifecycleAdapter(HomeActivity mReceiver) {
        this.mReceiver = mReceiver;
    }

    @Override
    public void callMethods(LifecycleOwner source, Lifecycle.Event event, boolean onAny, MethodCallsLogger logger) {
        boolean hasLogger = logger != null;
        if (onAny) {
            return;
        }
        if (event == Lifecycle.Event.ON_DESTROY) {
            if (!hasLogger || logger.approveCall("onOfferDestroyed", 1)) {
//                mReceiver.onOfferDestroyed();
            }
            return;
        }

    }
}
