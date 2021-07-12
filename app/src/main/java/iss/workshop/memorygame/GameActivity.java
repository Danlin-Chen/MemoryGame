package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private Integer[] mbaseImages = new Integer[12];
    private List<Integer> gameImages = new ArrayList<Integer>();
    private List<Integer> imagesFound = new ArrayList<Integer>();
    private int previousImage = -1;
    private int resultCount = 0;

    TextView resultCountTextView;
    TextView timerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        resultCountTextView = (TextView) findViewById(R.id.txtGamePlayResult);
        timerTextView = (TextView) findViewById(R.id.txtGamePlayTimer);

        GridView gridView = (GridView) findViewById(R.id.gameImageGridView);

        initImages();
        runCountDownTimer();
        gridView.setAdapter(new GamePlayImageAdapter(this, mbaseImages));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view;

                if((!imagesFound.contains(position)) && position!=previousImage){
                    if(previousImage == -1){
                        previousImage = position;
                        imageView.setImageResource(gameImages.get(position));
                    }else{
                        if(gameImages.get(previousImage)==gameImages.get(position)){
                            imagesFound.add(position);
                            imagesFound.add(previousImage);
                            previousImage = -1;
                            imageView.setImageResource(gameImages.get(position));
                            resultCountTextView.setText(++resultCount + " of 6 matches");

                        }else{
                            imageView.setImageResource(gameImages.get(position));
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView prevView = (ImageView) gridView.getChildAt(previousImage);
                                    prevView.setImageResource(R.drawable.img_0);
                                    imageView.setImageResource(R.drawable.img_0);
                                    previousImage = -1;
                                }
                            }, 1000);
                        }
                    }
                }

            }
        });
    }

    protected void runCountDownTimer(){
        new CountDownTimer(180000, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                timerTextView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            public void onFinish() {
                timerTextView.setText("00:00:00");
            }
        }.start();
    }

    protected void initImages(){
        Arrays.fill(mbaseImages, R.drawable.img_0);
        gameImages.add(R.drawable.img_1);
        gameImages.add(R.drawable.img_1);
        gameImages.add(R.drawable.img_2);
        gameImages.add(R.drawable.img_2);
        gameImages.add(R.drawable.img_3);
        gameImages.add(R.drawable.img_3);
        gameImages.add(R.drawable.img_4);
        gameImages.add(R.drawable.img_4);
        gameImages.add(R.drawable.img_5);
        gameImages.add(R.drawable.img_5);
        gameImages.add(R.drawable.img_6);
        gameImages.add(R.drawable.img_6);
        //Collections.shuffle(gameImages);
    }
}