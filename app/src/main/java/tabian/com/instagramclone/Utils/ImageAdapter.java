package tabian.com.instagramclone.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import tabian.com.instagramclone.R;

public class ImageAdapter extends ArrayAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private String[] imgURLs;

    public ImageAdapter(Context context, String[] imgURLs) {
        super(context , R.layout.gridview_item_image , imgURLs);

        this.mContext = context;
        this.imgURLs = imgURLs;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView ==null)
        {
            convertView = layoutInflater.inflate(R.layout.gridview_item_image,parent,false);
        }
        Glide.with(mContext).load(imgURLs[position]).into((ImageView) convertView);
        return convertView;
    }
}
