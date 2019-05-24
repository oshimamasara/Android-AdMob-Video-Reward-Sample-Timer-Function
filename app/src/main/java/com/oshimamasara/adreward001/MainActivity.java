// アプリがフォアグランド時のみタイマー稼働
// 一定時間になると動画広告
// 動画広告視聴で、次回動画広告まで +Time、 動画広告消した場合は -Time（ただし Time > 20 のみ）

package com.oshimamasara.adreward001;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private static final String TAG = "MainActivity";
    private CountDownTimer mCountDownTimer;
    private static long SET_TIME = 10;
    private long timeRemaining;
    TextView textView;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";///
    private static final String APP_ID = "ca-app-pub-3940256099942544~3347511713";
    private RewardedVideoAd rewardedVideoAd;
    boolean watchedVideo;  //false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"★onCreate");

        textView = (TextView)findViewById(R.id.textView);

        MobileAds.initialize(this, APP_ID);
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);///
        rewardedVideoAd.setRewardedVideoAdListener(this);///
        loadRewardedVideoAd();

        startTimer(SET_TIME);

    }

    private void loadRewardedVideoAd() {
        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    private void showRewardedVideo() { ///
        //textView.setVisibility(View.INVISIBLE);
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        }
    }

    private void startTimer(long time){
        watchedVideo = false;

        mCountDownTimer = new CountDownTimer(time*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished/1000;
                //textView.setText("seconds remaining: " + millisUntilFinished / 1000);
                textView.setText("動画広告まで: " + timeRemaining + "s");
            }

            public void onFinish() {
                textView.setText("AD TIME!");
                // 広告表示
                showRewardedVideo();
            }
        }.start();

    }

    @Override
    protected void onStart(){
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"★onStart");


    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"★onResume");


    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"★onPause");

    }

    @Override
    protected void onStop(){
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"★onStop");

        mCountDownTimer.cancel();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"★onDestroy");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"★onRestart");

        startTimer(timeRemaining);
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        // Preload the next video ad.
        loadRewardedVideoAd();
        Log.i(TAG,"広告閉じた時："+ watchedVideo);  //true

        if(watchedVideo){
            Toast.makeText(this, "広告視聴ありがとうございました", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"if 1："+ watchedVideo);
            startTimer(SET_TIME);
        } else if(watchedVideo==false && SET_TIME < 16){
            Toast.makeText(this, "広告視聴で閲覧時間UP", Toast.LENGTH_SHORT).show();
            startTimer(SET_TIME);
        } else {
            Toast.makeText(this, "広告視聴で閲覧時間UP", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"if 2："+ watchedVideo);
            SET_TIME = SET_TIME - 10;
            startTimer(SET_TIME);
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "YES! I GOT TIME", Toast.LENGTH_SHORT).show();
        watchedVideo = true;
        SET_TIME = SET_TIME + 5; //5sずつ加算
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }
}
