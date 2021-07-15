package iss.workshop.memorygame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private List<Bitmap> imgInfoList = new ArrayList<>();
    private List<Bitmap> imgDownloadList = new ArrayList<>();
    private List<Integer> selectedImgList = new ArrayList<>();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mTextview;
    private EditText mInputURLTxt;
    private Button mFetchBtn;
    private Thread bkgdThread;
    private ImageAdapter mImageAdapter;
    private final int semiTransparentGrey = Color.argb(155, 185, 185, 185);
    private final int numberOfImages = 6;
    private boolean downloadCompleted = false;
    private int downloadedImages;


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

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= downloadedImages || !downloadCompleted) {
                    return;
                }
                if (selectedImgList.size() < numberOfImages) {
                    ImageView imageView = (ImageView) view;
                    if (!selectedImgList.contains(i)) {
                        addEffect(imageView);
                        selectedImgList.add(i);
                        if (selectedImgList.size() == numberOfImages) {
                            startGame(selectedImgList);
                        }
                    } else if (selectedImgList.contains(i)) {
                        removeEffect(imageView);
                        selectedImgList.remove(Integer.valueOf(i));
                    }
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (selectedImgList.size() == numberOfImages) {
            mGridView.setAdapter(new ImageAdapter(this, imgDownloadList));
            resetImages();
        }
    }

    class MyThread extends Thread {
        ImageDownload imageDownload = new ImageDownload();

        @Override
        public void run() {

            String inputUrl = mInputURLTxt.getText().toString();
            String urlString = imageDownload.getUrlString(inputUrl);
            if (urlString == null)
                return;

            resetDownload();
            List<String> srcList = imageDownload.imgUrlList(urlString);
            for (int i = 0; i < 20; i++) {
                Bitmap bitmap = imageDownload.downloadImage(srcList.get(i));
                imgDownloadList.add(i, bitmap);
                downloadedImages = i + 1;
                updateGridView(i, bitmap);

                if (interrupted()) {
                    runOnUiThread(ImageActivity.this::setDefaultImage);
                    return;
                }
            }
            downloadCompleted = true;
        }
    }

    private void updateGridView(int i, Bitmap bitmap) {
        ImageView imageView = (ImageView) mGridView.getChildAt(i);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
                mProgressBar.setProgress(i + 1);
                if (i == 19) {
                    mTextview.setText(R.string.download_complete);
                    mTextview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mTextview.setVisibility(View.INVISIBLE);
                        }
                    }, 3000);
                } else {
                    mTextview.setText(getString(R.string.downloading, i + 1));
                    mProgressBar.setVisibility(View.VISIBLE);
                    mTextview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setDefaultImage() {
        imgInfoList.clear();
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.team_image);
        for (int i = 0; i < 20; i++) {
            imgInfoList.add(defaultBitmap);
        }
        mGridView.setAdapter(new ImageAdapter(this, imgInfoList));
    }

    public void resetDownload() {
        downloadCompleted = false;
        downloadedImages = 0;
        imgDownloadList.clear();
    }

    public void addEffect(ImageView imageView) {
        imageView.setColorFilter(semiTransparentGrey, PorterDuff.Mode.SRC_ATOP);
        imageView.setBackgroundResource(R.drawable.bg_card_selected);
    }

    public void removeEffect(ImageView imageView) {
        imageView.clearColorFilter();
        imageView.setBackgroundResource(R.drawable.bg_card);
    }

    public void resetImages() {
        mGridView = findViewById(R.id.grid_view);
        mImageAdapter = (ImageAdapter) mGridView.getAdapter();
        mImageAdapter.notifyDataSetChanged();
        selectedImgList.clear();
    }

    public void startGame(List<Integer> selectedImgList) {

        Intent intent = new Intent(this, GameActivity.class);
        for (int i = 0; i < selectedImgList.size(); i++) {

            Bitmap bm = imgDownloadList.get(Integer.valueOf(selectedImgList.get(i)));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            intent.putExtra("img" + i, byteArray);
        }
        startActivity(intent);
    }


}