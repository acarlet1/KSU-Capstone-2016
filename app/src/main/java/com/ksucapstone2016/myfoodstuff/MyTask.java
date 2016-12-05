package com.ksucapstone2016.myfoodstuff;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.android.volley.Response;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

//import info.androidhive.volleyexamples.utils.AsyncResponse;


import static java.lang.System.out;

/**
 * Created by Aaron on 12/3/2016.
 */

public class MyTask extends AsyncTask <Void, Integer, String> {
    String server_response;
    Context context;
    ListView listView;
    ProgressDialog progressDialog;
    TextView textView;
    Adapter adapter;
    String ans;

    MyTask(Context context, ListView listview, Adapter adapter, String ans) throws Exception {
        this.context = context;
        this.listView = listView;
        this.adapter = adapter;
        //this.button = button;
        this.ans = ans;
    }


    @Override
    protected String doInBackground(Void... params) {
        // connecting to url
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("https://www.amazon.com/s/ref=nb_sb_noss_1?url=search-alias%3Daps&field-keywords=" + ans);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream()); // store html into server_response
                Log.v("CatalogClient", server_response);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void  onPreExecute(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Fetching Amazon Price...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String result){
        progressDialog.hide();
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        int progress = values[0];
        progressDialog.setProgress(progress);
        textView.setText("Task in progress...");
    }

    // Converting InputStream to String
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String toReturn = response.toString();

        // a and b for substring of html (dollar)
        int b = toReturn.indexOf("class=\"sx-price-fractional") - 32;
        int a = b-5;
        toReturn = toReturn.substring(a, b);
        int dollar = Integer.parseInt(toReturn.replaceAll("[\\D]", ""));

        // c and d for substring of html )cents)
        toReturn = response.toString();
        int c = b + 59;
        int d = c + 4;
        toReturn = toReturn.substring(c, d);
        int cents = Integer.parseInt(toReturn.replaceAll("[\\D]", ""));

        // combine into one string
        toReturn = dollar + "." + cents;
        return toReturn;
    }
}