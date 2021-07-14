package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
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
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    ObjectAnimator animator1;
    ObjectAnimator animator2;
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
                        flipCard(imageView,gameImages.get(position));
                    }else{
                        if(gameImages.get(previousImage)==gameImages.get(position)){
                            imagesFound.add(position);
                            imagesFound.add(previousImage);
                            flipCard(imageView,gameImages.get(position));
                            ImageView prevView = (ImageView) gridView.getChildAt(previousImage);
                            imageView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    flashCard(imageView);
                                    flashCard(prevView);
                                    matchSound();
                                }
                            }, 400);

                            previousImage = -1;
                            resultCountTextView.setText(++resultCount + " of 6 matches");
                            if(resultCount==6){

                                long gamePlayTimeTaken = SystemClock.elapsedRealtime() - gameChronometer.getBase();
                                gameChronometer.stop();

                                mEndActivityIntent.putExtra("gamePlayTimeTaken", gamePlayTimeTaken);
                                startActivity(mEndActivityIntent);

                            }

                        }else{
                            flipCard(imageView,gameImages.get(position));
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView prevView = (ImageView) gridView.getChildAt(previousImage);
                                    flipCard(imageView,R.drawable.team_image);
                                    flipCard(prevView,R.drawable.team_image);
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
        Arrays.fill(mbaseImages, R.drawable.team_image);
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
        Collections.shuffle(gameImages);
    }

    protected void flipCard(ImageView imageView,int drawable){
        animator1 = ObjectAnimator.ofFloat(imageView, "rotationY",  0,90);
        animator2 = ObjectAnimator.ofFloat(imageView, "rotationY",  270,360);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setImageResource(drawable);
            }
        });
        AnimatorSet bouncer = new AnimatorSet();
        bouncer.play(animator1).before(animator2);
        bouncer.setDuration(200);
        bouncer.start();
    }
    protected void flashCard(ImageView imageView){
        animator1 = ObjectAnimator.ofFloat(imageView, "scaleX",  1,1.3f,1);
        animator2 = ObjectAnimator.ofFloat(imageView, "scaleY",  1,1.3f,1);
        AnimatorSet bouncer = new AnimatorSet();
        bouncer.play(animator1).with(animator2);
        bouncer.setDuration(400);
        bouncer.start();
    }

    protected void matchSound(){
        MediaPlayer mMediaPlayer;
        mMediaPlayer=MediaPlayer.create(this, R.raw.match_sound);
        mMediaPlayer.start();
    }
}