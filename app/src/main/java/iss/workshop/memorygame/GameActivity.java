package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
    private Bitmap[] mbaseImages = new Bitmap[12];
    private List<Bitmap> gameImages = new ArrayList<Bitmap>();
    private List<Integer> imagesFound = new ArrayList<Integer>();
    private int previousImage = -1;
    private int resultCount = 0;
    private boolean itemClickFlag = true;

    TextView resultCountTextView;
    Chronometer gameChronometer;
    Bitmap teamImage;

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

        teamImage = BitmapFactory.decodeResource(getResources(), R.drawable.team_image);

        initImages();

        gridView.setAdapter(new GamePlayImageAdapter(this, mbaseImages));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view;

                if((!imagesFound.contains(position)) && position!=previousImage && itemClickFlag==true){
                    itemClickFlag = false;
                    if(previousImage == -1){
                        previousImage = position;
                        flipCard(imageView,gameImages.get(position));
                        itemClickFlag = true;
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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mEndActivityIntent.putExtra("gamePlayTimeTaken", gamePlayTimeTaken);
                                        startActivity(mEndActivityIntent);
                                    }
                                }, 1000);

                            }

                            itemClickFlag = true;

                        }else{
                            flipCard(imageView,gameImages.get(position));
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView prevView = (ImageView) gridView.getChildAt(previousImage);

                                    flipCard(imageView, teamImage);
                                    flipCard(prevView, teamImage);
                                    previousImage = -1;
                                    itemClickFlag = true;
                                }
                            }, 1000);
                        }
                    }
                }
            }
        });
    }

    protected void initImages(){
        Arrays.fill(mbaseImages, teamImage);
        Intent intent = getIntent();

        for(int i=0; i<6; i++){
            byte[] imageByteArray = intent.getByteArrayExtra("img" + i);
            Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            gameImages.add(image);
            gameImages.add(image);
        }
        
        Collections.shuffle(gameImages);
    }

    protected void flipCard(ImageView imageView,Bitmap image){
        animator1 = ObjectAnimator.ofFloat(imageView, "rotationY",  0,90);
        animator2 = ObjectAnimator.ofFloat(imageView, "rotationY",  270,360);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setImageBitmap(image);
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