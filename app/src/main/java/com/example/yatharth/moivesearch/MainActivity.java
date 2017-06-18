package com.example.yatharth.moivesearch;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    EditText et;
    Button search;
    //static TextView result;
    //static TextView result2;
    static ListView movielist;
    static String js=null;
    static String id[];
    String apiKey = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=(EditText)findViewById(R.id.enter);
        search=(Button)findViewById(R.id.search);
        //result=(TextView)findViewById(R.id.result);
        //result2=(TextView)findViewById(R.id.result2);

        movielist=(ListView) findViewById(R.id.movielist);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        movielist.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        movielist.setDividerHeight(3);
        //result.setVisibility(View.INVISIBLE);
        //result2.setVisibility(View.INVISIBLE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResult(et.getText().toString());



            }
        });
    }


    public void getResult(String s)
    {
        s=s.trim();
        String s1="";
        int p1=0,p2=0;
        if(s.contains(" "))
        {
            for(int i=0;i<s.length();i++) {
                if (s.charAt(i) == ' ' || i == s.length() - 1) {

                    if (i == s.length() - 1) {
                        p2 = i + 1;
                        s1 = s1 + s.substring(p1, p2);
                        p1 = p2;
                    } else {
                        p2 = i;
                        s1 = s1 + s.substring(p1, p2) + "+";
                        p1 = p2 + 1;
                    }
                }

            }
        }
        else
        {
            s1=s;
        }
        //http://www.imdb.com/xml/find?json=1&nr=1&tt=on&q=
        //result.setText(s1);
        String url = "http://www.omdbapi.com/?apikey="+apiKey+"&s="+s1;
        RequestTask rt=new RequestTask(this);
        rt.execute(url);
        //MainActivity.check.setText("main");








    }

    @TargetApi(16)
    public static void parseJson(final Context context, String resultfinal)
    {
        try {
            /*JSONParser parser = new JSONParser();
            Object obj = parser.parse(result.getText().toString().trim());
            org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) obj;


*/


            JSONObject jsonObj = new JSONObject(resultfinal.trim());

            JSONArray ja = (JSONArray) jsonObj.getJSONArray("Search");

            if (ja.length() != 0) {
                String title[] = new String[ja.length()];
                String year[]=new String[ja.length()];
                String type[]=new String[ja.length()];
                id=new String[ja.length()];

                ArrayList<ClipData.Item> planetList = new ArrayList<ClipData.Item>();


                for (int i = 0; i < ja.length(); i++) {
                    JSONObject inner = ja.getJSONObject(i);
                    title[i] = inner.getString("Title");
                    year[i] = inner.getString("Year");
                    type[i] = inner.getString("Type");
                    id[i]=inner.getString("imdbID");
                    planetList.add(new ClipData.Item(title[i],"Year = "+year[i]+ "   Type = "+type[i]));
                    //result2.setText(result2.getText() + "\n" + title[i]);
                }



                MyAdapter adapter = new MyAdapter(context, planetList);

                /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, planetList);*/

                movielist.setAdapter(adapter);
                //movielist.setDivider(context.getResources().getDrawable(R.drawable.divider));
                //movielist.setDividerHeight(2);

                setListViewHeightBasedOnChildren(movielist);
                movielist.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });


                movielist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        // ListView Clicked item index
                        int itemPosition = position;

                        // ListView Clicked item value
                        String itemValue = (String) movielist.getItemAtPosition(position);

                        // Show Alert
                        Toast.makeText(context,
                                "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_SHORT)
                                .show();

                    }

                });

            }

        } /*catch (org.json.simple.parser.ParseException e) {
            System.out.println(e);
            result2.setText(e + " at " + e.getPosition() + " of type " + e.getErrorType());
        }*/ catch (JSONException e) {
            System.out.println(e);
        }
    }
    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void openDetails(Context context,int pos)
    {
        Intent i=new Intent(context,MovieResult.class);
        i.putExtra("id",id[pos]);
        context.startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.about_us) {

            Intent in=new Intent(this,AboutUs.class);
            startActivity(in);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
