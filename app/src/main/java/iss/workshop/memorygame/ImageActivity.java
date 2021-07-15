package iss.workshop.memorygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private List<Bitmap> imgInfoList = new ArrayList<>();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mTextview;
    EditText mInputURLTxt;
    Button mFetchBtn;
    Thread bkgdThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mInputURLTxt = findViewById(R.id.inputURL);
        mFetchBtn = findViewById(R.id.fetchBtn);
        mGridView = findViewById(R.id.grid_view);
        mProgressBar = findViewById(R.id.progressBar);
        mTextview = findViewById(R.id.tv_downloading);

        setDefaultImage();

        mFetchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mTextview.setVisibility(View.INVISIBLE);
                if (bkgdThread != null)
                    bkgdThread.interrupt();

                bkgdThread = new MyThread();
                bkgdThread.start();
            }
        });
    }

    class MyThread extends Thread {
        ImageDownload imageDownload = new ImageDownload();

        @Override
        public void run() {

            String inputUrl = mInputURLTxt.getText().toString();
            String urlString = imageDownload.getUrlString(inputUrl);
            if (urlString == null)
                return;

            List<String> srcList = imageDownload.imgUrlList(urlString);
            for (int i = 0; i < 20; i++) {
                Bitmap bitmap = imageDownload.downloadImage(srcList.get(i));
                updateGridView(i, bitmap);

                if (interrupted()) {
                    runOnUiThread(ImageActivity.this::setDefaultImage);
                    return;
                }
            }
        }
    }

    private void updateGridView(int i, Bitmap bitmap) {
        ImageView imageView = (ImageView) mGridView.getChildAt(i);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
                mProgressBar.setProgress(i + 1);
                mTextview.setText(getString(R.string.downloading, i+1));
                mProgressBar.setVisibility(View.VISIBLE);
                mTextview.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setDefaultImage() {
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.team_image);
        for (int i = 0; i < 20; i++) {
            imgInfoList.add(defaultBitmap);
        }
        mGridView.setAdapter(new ImageAdapter(this, imgInfoList));
    }


}