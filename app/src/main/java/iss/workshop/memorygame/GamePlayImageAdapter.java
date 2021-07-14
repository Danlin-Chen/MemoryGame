package iss.workshop.memorygame;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Arrays;

public class GamePlayImageAdapter extends BaseAdapter {

    private Context mContext;

    private Integer[] mImageIds;

    public GamePlayImageAdapter(Context context, Integer[] ids){
        mContext = context;
        mImageIds = ids;
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(310, 380));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(15, 15, 15, 15);
            imageView.setCropToPadding(true);
            imageView.setBackgroundResource(R.drawable.bg_card);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mImageIds[position]);
        return imageView;
    }

}
