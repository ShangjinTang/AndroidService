package com.example.androidservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Intent mServiceIntent;
    private boolean mStopLoop;
    private ExampleService mExampleService;
    private boolean mIsServiceBound;
    private ServiceConnection mServiceConnection;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: thread id: " + Thread.currentThread().getId());
        mServiceIntent = new Intent(getApplicationContext(), ExampleService.class);

        mTextView = findViewById(R.id.textView);
    }

    public void startService(View view) {
        Log.d(TAG, "startService() called");
        mStopLoop = false;
        startService(mServiceIntent);
    }

    public void stopService(View view) {
        Log.d(TAG, "stopService() called");
        stopService(mServiceIntent);
    }

    public void bindService(View view) {
        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    ExampleService.ExampleServiceBinder exampleServiceBinder = (ExampleService.ExampleServiceBinder) iBinder;
                    mExampleService = exampleServiceBinder.getService();
                    mIsServiceBound = true;
                    Log.d(TAG, "Service Connected");
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mIsServiceBound = false;
                    Log.d(TAG, "Service Disconnected");
                }
            };
        }

        bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(View view) {
        Log.d(TAG, "unbindService() called with: view = [" + view + "]");
        if (mIsServiceBound) {
            unbindService(mServiceConnection);
            mIsServiceBound = false;
            Log.d(TAG, "unbindService: succeeded");
        }
    }

    public void getRandomNumber(View view) {
        String result;
        if (mIsServiceBound) {
            result = "Random number: " + mExampleService.getRandomNumber();
        } else {
            result = "Service not bound, exit...";
        }
        mTextView.setText(result);
    }
}