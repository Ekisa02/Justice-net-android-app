package com.example.justicenetapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;

public class ImageListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> imagePaths;
    private final LayoutInflater inflater;

    public ImageListAdapter(Context context, ArrayList<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_image, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);

        // Get image path and set it to the ImageView
        String imagePath = imagePaths.get(position);
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            imageView.setImageURI(Uri.fromFile(imgFile));
        }

        return convertView;
    }
}
