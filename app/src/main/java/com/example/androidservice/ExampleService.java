package com.example.androidservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExampleService extends Service {
    private static final String TAG = "ExampleService";

    private int mRandomNumber;
    private AtomicBoolean mIsRandomGeneratorOn = new AtomicBoolean(false);

    public static final int MIN = 0;
    public static final int MAX = 100;

    class ExampleServiceBinder extends Binder {
        public ExampleService getService() {
            return ExampleService.this;
        }
    }

    private IBinder mBinder = new ExampleServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: thread id:" + Thread.currentThread().getId());
        mIsRandomGeneratorOn.set(true);
//        new Thread(this::startRandomNumberGenerator).start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: intent = [" + intent + "]");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind() called with: intent = [" + intent + "]");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
        stopRandomNumberGenerator();
    }

    private void startRandomNumberGenerator() {
        Log.d(TAG, "startRandomNumberGenerator: thread id: " +
                Thread.currentThread().getId());
        while (mIsRandomGeneratorOn.get()) {
            try {
                if (mIsRandomGeneratorOn.get()) {
                    mRandomNumber = RandomGenerator.getRandomNumber(MIN, MAX);
                    Log.i(TAG, "startRandomNumberGenerator: random number: " +
                            mRandomNumber);
                }
            } catch (InterruptedException e) {
                Log.w(TAG, "startRandomNumberGenerator: Thread Interruptted");
            }
        }
    }

    private void stopRandomNumberGenerator() {
        mIsRandomGeneratorOn.set(false);
    }

    public int getRandomNumber() {
        try {
            return RandomGenerator.getRandomNumber(MIN, MAX);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
