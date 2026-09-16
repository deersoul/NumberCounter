package com.todaysoft.ymshin.numbercounter;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.todaysoft.ymshin.numbercounter.component.BasicActivity;
import com.todaysoft.ymshin.numbercounter.component.Preferences;
import com.todaysoft.ymshin.numbercounter.util.CommonUtil;

/**
 * 인트로 액티비티
 * @author ymshin
 * @Date 2019-02-07
 */
public class IntroActivity extends BasicActivity {

    private static final String TAG = IntroActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_ALL = 100;
    private long delayMillis = 1000;
    private boolean mFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        requestPermissions();
    }

    /**
     * 퍼미션 요청
     */
    private void requestPermissions() {
        Log.e(TAG, "requestPermissions");
        if (CommonUtil.needRequestPermission(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                })) {
            // 전체 권한 요청
            ActivityCompat.requestPermissions(IntroActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_PERMISSIONS_ALL);
        } else {
            initialize();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_ALL:
                initialize();
                break;
        }
    }

    private void initialize() {

        //lastVersionCheck(); //앱 버전이 최신인지 체크
        Log.e(TAG, "initialize");
        //처음 실행한거라면
        if (!Preferences.getBoolean(IntroActivity.this,  "IS_NOT_FIRST")) {
            //누르면 진동하게 기본 세팅
            Preferences.setValue(IntroActivity.this, Constant.IS_VIBE, true);
            //배경누르면 숫자가 증가하게 기본 세팅
            Preferences.setValue(IntroActivity.this, Constant.IS_INCREASE, true);
            //앞으로는 처음 실행이 아님
            Preferences.setValue(IntroActivity.this, "IS_NOT_FIRST", true);
        }
        finishIntroDelayed(delayMillis);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new InitializeTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new InitializeTask().execute();
        }
    }

    /**
     * 비동기 앱 환경 설정
     */
    class InitializeTask extends AsyncTask<Integer, Integer, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(Integer... params) {
            //TODO 앱 버전 가져오기, DB에 기본 설정값 가지고있는지 등등..

            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if(mFinish) {
                startApp();
            } else {
                mFinish = true;
            }
        }
    }

    private void finishIntroDelayed(long delayMillis) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e(TAG, "finishIntroDelayed = " + mFinish + " msg = " + msg);
                startApp();
            }
        };
        handler.sendEmptyMessageDelayed(0, delayMillis);
    }

    /**
     * 앱 시작
     */
    private void startApp() {
        startActivity(MainActivity.class);
        finish();
    }
}
