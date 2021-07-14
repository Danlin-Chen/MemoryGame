package iss.workshop.memorygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> imgInfoList;
    private int mMaxPosition;

    public ImageAdapter(Context context, List<Bitmap> imgInfoList){
        this.context=context;
        this.imgInfoList=imgInfoList;
    }

    @Override
    public int getCount(){
        mMaxPosition=imgInfoList.size();
        return mMaxPosition;
    }
    @Override
    public Object getItem(int position) {
        return imgInfoList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(3, 0, 3, 0);

        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(imgInfoList.get(position));
        return imageView;
    }



}
