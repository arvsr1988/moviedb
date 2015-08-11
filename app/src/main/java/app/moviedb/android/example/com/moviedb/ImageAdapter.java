package app.moviedb.android.example.com.moviedb;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private List<MovieData> movieDataList;
    private Context mContext;


    public ImageAdapter(Context c, List<MovieData> movieDataList) {
        mContext = c;
        this.movieDataList = movieDataList;
    }

    public int getCount() {
        return movieDataList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }


        Picasso.with(mContext) //
                .load(movieDataList.get(position).getImageUrl())
                .fit()
                .into(imageView);
        return imageView;
    }
}