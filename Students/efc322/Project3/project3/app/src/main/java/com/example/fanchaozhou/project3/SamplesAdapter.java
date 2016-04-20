package com.example.fanchaozhou.project3;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fanchao Zhou on 4/8/2016.
 */
public class SamplesAdapter extends ArrayAdapter<DBRecord> {

    Context mContext;
    private @LayoutRes
    int layoutID;
    ArrayList<DBRecord> recList;

    public SamplesAdapter(Context mContext, @LayoutRes int layoutID, ArrayList<DBRecord> recList){
        super(mContext, layoutID, recList);

        this.mContext = mContext;
        this.layoutID = layoutID;
        this.recList = recList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DBRecord rec = recList.get(position);   //Get the current item in the source data set

        if(convertView == null){ //If this is a view with no layout before, then inflate the layout
            convertView = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        }

        final ImageView itemImageView = (ImageView) convertView.findViewById(R.id.list_item_sample_imageView);
        itemImageView.setImageBitmap(rec.thumbnailPhoto);

        TextView itemTextView = (TextView) convertView.findViewById(R.id.list_item_sample_textView);
        String typeInfo = "Name: " + rec.typeName + '\n' + "Name ID: " + rec.typeID + '\n' + "Record ID: " + rec.recordID;
        itemTextView.setText(typeInfo);

        return convertView;
    }
}
