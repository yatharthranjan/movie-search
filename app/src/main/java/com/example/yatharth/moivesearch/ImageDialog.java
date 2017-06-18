package com.example.yatharth.moivesearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Yatharth on 6/10/2015.
 */
public class ImageDialog extends Activity {

    private ImageView mDialog;
    static Bitmap bm;


public static void setBitmap(Bitmap bm1)
{
    bm=bm1;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagedialog);



        mDialog = (ImageView)findViewById(R.id.my_image);
        mDialog.setImageBitmap(bm);
        mDialog.setClickable(true);


        //finish the activity (dismiss the image dialog) if the user clicks
        //anywhere on the image
        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}