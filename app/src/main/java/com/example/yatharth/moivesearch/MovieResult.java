package com.example.yatharth.moivesearch;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MovieResult extends Activity {
    ProgressDialog dialog;
    TextView movieTitle,movieType,released,rating,lang,desc,actor,gen,award;
    String responseString;
    Bitmap bm;
    String apiKey = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_result);

        Intent in = getIntent();
        final String id = in.getStringExtra("id");

                Button open=(Button)findViewById(R.id.openlink);
                movieTitle = (TextView) findViewById(R.id.movietitle);
                movieType = (TextView) findViewById(R.id.movietype);
                released = (TextView) findViewById(R.id.released);
                released = (TextView) findViewById(R.id.released);
                rating = (TextView) findViewById(R.id.rating);
                lang = (TextView) findViewById(R.id.language);
                desc = (TextView) findViewById(R.id.description);
                actor = (TextView) findViewById(R.id.actor);
                gen = (TextView) findViewById(R.id.genre);
                award = (TextView) findViewById(R.id.awards);
                ImageView iv=(ImageView)findViewById(R.id.poster);

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i=new Intent(MovieResult.this,ImageDialog.class);
                        startActivity(i);

            }
        });


        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.imdb.com/title/"+id.trim()));
                startActivity(browserIntent);
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        dialog.setCancelable(false);

        new Thread() {
            public void run() {

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response;
                responseString = null;
                String finalString = null;
                try {
                    response = httpclient.execute(new HttpGet("http://www.omdbapi.com/?apikey="+apiKey+"&i=" + id.trim() + "&r=json"));
                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        responseString = out.toString();
                        out.close();

                    } else {
                        // Closes the connection.
                        response.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());
                    }
                } catch (ClientProtocolException e) {

                    // TODO Handle problems..
                } catch (IOException e) {

                    // TODO Handle problems..
                }/*catch(ParserConfigurationException e)
        {

		}
		catch(SAXException e){

		}*/ catch (NullPointerException e) {

                }

                MovieResult.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String s = JSONParse(MovieResult.this, responseString);
                        //movieTitle.setText(s);
                    }
                });


                if (dialog.isShowing()) {
                    dialog.dismiss();
                }


            }
        }.start();
    }

    public String JSONParse(Context context, String response) {
        StringBuilder sb = null;

        try {
            JSONObject jsonObj = new JSONObject(response.trim());

            String title = jsonObj.getString("Title");
            movieTitle.setText(title);

            String year = jsonObj.getString("Year");


            String releasedon = jsonObj.getString("Released");
            released.setText("Released on : "+releasedon);

            String runtime = jsonObj.getString("Runtime");
            String genre = jsonObj.getString("Genre");
            gen.setText("GENRE\n"+genre+"\n\nRUNTIME\n"+runtime);

            String director = jsonObj.getString("Director");
            String writer = jsonObj.getString("Writer");
            String actors = jsonObj.getString("Actors");
            actor.setText("DIRECTOR\n"+director+"\n\nWRITER\n"+writer+"\n\nACTORS\n"+actors);

            String plot = jsonObj.getString("Plot");
            desc.setText("DESCRIPTION\n"+plot);

            String langua = jsonObj.getString("Language");
            lang.setText("Language : "+langua);

            String awards = jsonObj.getString("Awards");
            award.setText("AWARDS\n"+awards);

            final String poster = jsonObj.getString("Poster");
            String imdbrating = jsonObj.getString("imdbRating");
            rating.setText("Rating : "+imdbrating);

            String imdbvotes = jsonObj.getString("imdbVotes");

            String type = jsonObj.getString("Type");
            movieType.setText("Type : "+type);

            sb = new StringBuilder(title);
            sb.append("\n" + year);
            sb.append("\n" + releasedon);
            sb.append("\n" + runtime);
            sb.append("\n" + genre);
            sb.append("\n" + director);
            sb.append("\n" + writer);
            sb.append("\n" + actors);
            sb.append("\n" + plot);
            sb.append("\n" + langua);
            sb.append("\n" + awards);
            sb.append("\n" + poster);
            sb.append("\n" + imdbrating);
            sb.append("\n" + imdbvotes);
            sb.append("\n" + type);

            new Thread() {
                Bitmap bm;
                public void run() {
                    try {
                        bm = BitmapFactory.decodeStream((InputStream) new URL(poster.trim()).getContent());

                    } catch (MalformedURLException e) {

                    }
                    catch(IOException e)
                    {

                    }
                    MovieResult.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ImageView iv=(ImageView)findViewById(R.id.poster);
                            iv.setImageBitmap(bm);
                            ImageDialog.setBitmap(bm);
                        }
                    });

                }
            }.start();


        } catch (JSONException e) {

        }


        return (sb.toString());
    }
}
