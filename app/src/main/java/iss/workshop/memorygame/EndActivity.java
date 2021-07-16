package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class EndActivity extends AppCompatActivity {

    private long score;
    private long best1, best2, best3;
    private String bName1, bName2, bName3, player;
    private TextView mScoreChart, mBestOne, mBestTwo, mBestThree;
    private Button mPlayAgainBtn, mHomeBtn;
    private boolean updatedFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        boolean onDestroy = false;
        if (savedInstanceState !=null){
            onDestroy = savedInstanceState.getBoolean("onDestroy");
        }

        applauseSound();
        mScoreChart = findViewById(R.id.scoreline2);
        mPlayAgainBtn = findViewById(R.id.playAgainBtn);
        mBestOne = findViewById(R.id.bestOne);
        mBestTwo = findViewById(R.id.bestTwo);
        mBestThree = findViewById(R.id.bestThree);
        mHomeBtn = findViewById(R.id.homeBtn);

        Intent intent = getIntent();

        score = intent.getLongExtra("gamePlayTimeTaken", 0);

        SharedPreferences pref = getSharedPreferences("players_information", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        best1 = pref.getLong("best1", 0);
        best2 = pref.getLong("best2", 0);
        best3 = pref.getLong("best3", 0);

        bName1 = pref.getString("bName1", null);
        bName2 = pref.getString("bName2", null);
        bName3 = pref.getString("bName3", null);
        player = pref.getString("player", null);

        mScoreChart.setText(getString(R.string.score2_vertical,conversionOfTime(score)));
        if (onDestroy){
            mScoreChart.setText(getString(R.string.score2_vertical, conversionOfTime(score)));
            if (bName1 !=null){
                mBestOne.setText(getString(R.string.leader1,bName1, conversionOfTime(best1)));
            }
            if (bName2 !=null){
                mBestTwo.setText(getString(R.string.leader1,bName2, conversionOfTime(best2)));
            }
            if (bName3 !=null){
                mBestThree.setText(getString(R.string.leader1,bName3, conversionOfTime(best3)));
            }
            return;
        }

            if(score < best1) {
                long temp = best1;
                best1 = score;
                long temp2 = best2;
                best2 = temp;
                best3 = temp2;

                String sTemp = bName1;
                bName1 = player;
                String sTemp2 = bName2;
                bName2 = sTemp;
                bName3 = sTemp2;

                editor.putLong("best3", best3);
                editor.putLong("best2", best2);
                editor.putLong("best1", best1);
                editor.putString("bName1", bName1);
                editor.putString("bName2", bName2);
                editor.putString("bName3", bName3);
                editor.commit();
                updatedFlag = true;
            }
            else if(score < best2) {
                Long temp = best2;
                best2 = score;
                best3 = temp;

                String sTemp = bName2;
                bName2 = player;
                bName3 = sTemp;

                editor.putLong("best3", best3);
                editor.putLong("best2", best2);
                editor.putString("bName2", bName2);
                editor.putString("bName3", bName3);
                editor.commit();
                updatedFlag = true;
            }
            else if(score < best3) {
                best3 = score;
                editor.putLong("best3", best3);
                editor.putString("bName3", player);
                editor.commit();
                updatedFlag = true;
            }

            if(best1 == 0 && updatedFlag == false) {
                mBestOne.setText(getString(R.string.leader1,player, conversionOfTime(score)));
                editor.putString("bName1", player);
                editor.putLong("best1", score);
                editor.commit();
            }
            else if (best2 == 0 && updatedFlag == false) {
                mBestOne.setText(getString(R.string.leader1,bName1,conversionOfTime(best1)));
                mBestTwo.setText(getString(R.string.leader1,player,conversionOfTime(score)));
                editor.putString("bName2", player);
                editor.putLong("best2", score);
                editor.commit();
            }
            else if (best3 == 0 && updatedFlag == false) {
                mBestOne.setText(getString(R.string.leader1,bName1,conversionOfTime(best1)));
                mBestTwo.setText(getString(R.string.leader1,bName2,conversionOfTime(best2)));
                mBestThree.setText(getString(R.string.leader1,player,conversionOfTime(score)));
                editor.putString("bName3", player);
                editor.putLong("best3", score);
                editor.commit();
            }
            else {
                if(best3 == 0) {
                    mBestOne.setText(getString(R.string.leader1,bName1,conversionOfTime(best1)));
                    mBestTwo.setText(getString(R.string.leader1,bName2,conversionOfTime(best2)));
                }
                else if(best2 == 0 && best3 == 0) {
                    mBestOne.setText(getString(R.string.leader1,bName1,conversionOfTime(best1)));
                }
                else {
                    mBestOne.setText(getString(R.string.leader1, bName1, conversionOfTime(best1)));
                    mBestTwo.setText(getString(R.string.leader1, bName2, conversionOfTime(best2)));
                    mBestThree.setText(getString(R.string.leader1, bName3, conversionOfTime(best3)));
                }
            }

        mPlayAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });

        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    protected String conversionOfTime(long gamePlayTimeTaken){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(gamePlayTimeTaken);
        long seconds = (TimeUnit.MILLISECONDS.toSeconds(gamePlayTimeTaken) % 60);
        String time = " " + minutes +":"+ seconds + " ";
        return time;
    }
    protected void applauseSound(){
        MediaPlayer mMediaPlayer;
        mMediaPlayer=MediaPlayer.create(this, R.raw.crowdapplause);
        mMediaPlayer.start();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EndActivity.this, ImageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean("onDestroy", true);
    }
}