package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
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
                    Bitmap bitmap=imageDownload.downloadImage(srcList.get(i));
                    updateGridView(i,bitmap);
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
        },5000);
    }

    private void setDefaultImage(){
        String defaultUrl="https://cdn.stocksnap.io/img-thumbs/280h/tilesshapes-texture_6U6EAPKKD7.jpg";
        ImageDownload imageDownload=new ImageDownload();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap defaultBitmap=imageDownload.downloadImage(defaultUrl);
                for (int i=0;i<20;i++) {
                    imgInfoList.add(defaultBitmap);
                }
            }
        }).start();

        mGridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setDefaultGridView();
            }
        },3000);
    }

    private void setDefaultGridView(){
        mGridView.setAdapter(new ImageAdapter(this,imgInfoList));
    }

    private void removeDefaultImage(){
        Iterator<Bitmap> iterator=imgInfoList.iterator();
        while (iterator.hasNext()){
            iterator.remove();
        }
    }
}