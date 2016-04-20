package com.example.fanchaozhou.project3;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fanchao Zhou on 4/8/2016.
 */
public class NameListAdapter extends ArrayAdapter<DBType> {
    Context mContext;
    private @LayoutRes
    int layoutID;
    ArrayList<DBType> typeList;

    public NameListAdapter(Context mContext, @LayoutRes int layoutID, ArrayList<DBType> typeList){
        super(mContext, layoutID, typeList);

        this.mContext = mContext;
        this.layoutID = layoutID;
        this.typeList = typeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DBType type = typeList.get(position);   //Get the current item in the source data set

        if(convertView == null){ //If this is a view with no layout before, then inflate the layout
            convertView = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        }

        TextView itemTextView = (TextView) convertView.findViewById(R.id.list_item_type_textview);
        String typeInfo = "Type Name: " + type.typeName + '\n' + "Type ID: " + type.typeID;
        itemTextView.setText(typeInfo);

        return convertView;
    }
}
