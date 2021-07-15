package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class EndActivity extends AppCompatActivity {

    private long score;
    private long best1, best2, best3;
    private String bName1, bName2, bName3, player;
    private TextView mScoreChart, mBestOne, mBestTwo, mBestThree;
    private Button mHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        mScoreChart = findViewById(R.id.scoreChart);
        mHomeBtn = findViewById(R.id.homeBtn);
        mBestOne = findViewById(R.id.bestOne);
        mBestTwo = findViewById(R.id.bestTwo);
        mBestThree = findViewById(R.id.bestThree);

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
        }
        else if(score < best2) {
            Long temp = best2;
            best2 = score;
            best3 = best2;

            String sTemp = bName2;
            bName2 = player;
            bName3 = sTemp;

            editor.putLong("best3", best3);
            editor.putLong("best2", best2);
            editor.putString("bName2", bName2);
            editor.putString("bName3", bName3);
            editor.commit();
        }
        else if(score < best3) {
            best3 = score;
            editor.putLong("best3", best3);
            editor.putString("bName3", player);
            editor.commit();
        }

        mScoreChart.setText(getString(R.string.score)+ " You used " + conversionOfTime(score) +
                " to complete the game.");

        if(best1 == 0) {
            mBestOne.setText(player + " " + score);
            editor.putString("bName1", player);
            editor.putLong("best1", score);
            editor.commit();
        }
        else if (best2 == 0) {
            mBestOne.setText(bName1 + " " + best1);
            mBestTwo.setText(player + " " + score);
            editor.putString("bName2", player);
            editor.putLong("best2", score);
            editor.commit();
        }
        else if (best3 == 0) {
            mBestOne.setText(bName1 + " " + best1);
            mBestTwo.setText(bName2 + " " + best2);
            mBestThree.setText(player + " " + score);
            editor.putString("bName3", player);
            editor.putLong("best3", score);
            editor.commit();
        }
        else {
            mBestOne.setText(bName1 + " " + best1);
            mBestTwo.setText(bName2 + " " + best2);
            mBestThree.setText(bName3 + " " + best3);
        }


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
        String time = minutes +" : "+ seconds;
        return time;
    }
}