package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
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
    Chronometer gameChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent mEndActivityIntent = new Intent(this, EndActivity.class);

        resultCountTextView = (TextView) findViewById(R.id.txtGamePlayResult);
        GridView gridView = (GridView) findViewById(R.id.gameImageGridView);
        gameChronometer = (Chronometer) findViewById(R.id.gameChronometer);
        gameChronometer.setBase(SystemClock.elapsedRealtime());
        gameChronometer.start();

        initImages();

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
                            if(resultCount==6){

                                long gamePlayTimeTaken = SystemClock.elapsedRealtime() - gameChronometer.getBase();
                                gameChronometer.stop();

                                mEndActivityIntent.putExtra("gamePlayTimeTaken", gamePlayTimeTaken);
                                startActivity(mEndActivityIntent);

                            }

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