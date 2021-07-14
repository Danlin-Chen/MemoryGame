package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private List<Bitmap> imgInfoList=new ArrayList<Bitmap>();
    private int currentNum=0;
    private GridView mGridView;
    private ImageAdapter gridAdapter;

    EditText mInputURLTxt;
    Button mFetchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mInputURLTxt=(EditText)findViewById(R.id.inputURL);
        mFetchBtn=(Button)findViewById(R.id.fetchBtn);
        mGridView=findViewById(R.id.grid_view);

        setDefaultImage();

        mFetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImage();
//                mFetchBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        setDefaultImage();
//                        setImage();
//                    }
//                });
            }
        });
    }

    private void setImage(){
        String inputUrl=mInputURLTxt.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageDownload imageDownload=new ImageDownload();
                String urlString=imageDownload.getUrlString(inputUrl);
                List<String> srcList = imageDownload.imgUrlList(urlString);

                for (int i=0;i<20;i++) {
                    try {
                        Thread.sleep(1500);
                        Bitmap bitmap=imageDownload.downloadImage(srcList.get(i));
                        imgInfoList.add(bitmap);
                        imgInfoList.remove(i);
                        updateGridView(i,bitmap);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void updateGridView(int i,Bitmap bitmap){

        mGridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView imageView=(ImageView) mGridView.getChildAt(i);
                imageView.setImageBitmap(bitmap);
            }
        },3000);
    }

    private void setDefaultImage(){

        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.team_image);
        for (int i=0;i<20;i++) {
            imgInfoList.add(defaultBitmap);
        }
        mGridView.setAdapter(new ImageAdapter(this,imgInfoList));
    }

}