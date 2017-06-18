package com.example.yatharth.moivesearch;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

class RequestTask extends AsyncTask<String, String, String> {
    ProgressDialog dialog;
    Context context;


    public RequestTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading Please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(true);
        super.onPreExecute();
    }

    @Override
	protected String doInBackground(String... uri) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		String finalString=null;
		try {
			response = httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				responseString = out.toString();
				/*DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db=documentBuilderFactory.newDocumentBuilder();
				Document doc=db.parse(uri[0]);
				NodeList nl= doc.getElementsByTagName("BookData");
				responseString="ISBN="+((Element) nl.item(0)).getAttribute("isbn").toString();
				finalString=responseString;
				responseString="\nISBN13="+((Element) nl.item(0)).getAttribute("isbn13").toString();
				finalString=finalString+responseString;
				nl=doc.getElementsByTagName("Title");
				responseString="\nTitle="+((Element)nl.item(0)).getTextContent().toString();
				finalString=finalString+responseString;
				nl=doc.getElementsByTagName("AuthorsText");
				responseString="\nAuthors="+((Element)nl.item(0)).getTextContent().toString();
				finalString=finalString+responseString;*/
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
			
		}*/catch(NullPointerException e)
		{

		}


		return responseString;

	}

	@Override
	protected void onPostExecute(final String result) {
		super.onPostExecute(result);
		// Do anything with response..
		//BarcodeScanner.bookdetail.setTextSize(24);
		//BarcodeScanner.bookdetail.setText(result);
        //MainActivity.result.setText(result);
        //MainActivity.js=result;
        MainActivity.parseJson(context,result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        //MainActivity.check.setText("result set");
	}

}