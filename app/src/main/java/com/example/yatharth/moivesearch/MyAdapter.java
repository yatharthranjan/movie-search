package com.example.yatharth.moivesearch;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yatharth on 6/6/2015.
 */
public class MyAdapter extends ArrayAdapter<ClipData.Item> implements View.OnClickListener{

private final Context context;
private final ArrayList<ClipData.Item> itemsArrayList;

public MyAdapter(Context context, ArrayList<ClipData.Item> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        }

@TargetApi(16)@Override
public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.value);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getText());
        valueView.setText(itemsArrayList.get(position).getHtmlText());

    rowView.setOnClickListener(new OnItemClickListener( position ));

        // 5. retrn rowView
        return rowView;
        }

    @Override
    public void onClick(View v) {
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            MainActivity.openDetails(context,mPosition);


        }
    }
}

