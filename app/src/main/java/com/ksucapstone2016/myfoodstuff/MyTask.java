package com.ksucapstone2016.myfoodstuff;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyTask extends AsyncTask <Void, Integer, String> {
    String server_response;
    Context context;
    ProgressDialog progressDialog;
    TextView textView;
    Adapter adapter;
    String ans;

    MyTask(Context context, Adapter adapter, String ans) throws Exception {
        this.context = context;
        this.adapter = adapter;
        this.ans = ans;
    }


    @Override
    protected String doInBackground(Void... params) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("https://www.amazon.com/s/ref=nb_sb_noss_1?url=search-alias%3Daps&field-keywords="
                          + ans); // ans being what is to be queried
            urlConnection = (HttpURLConnection) url.openConnection(); // open connection to url
            int responseCode = urlConnection.getResponseCode();  // response code for testing connection
            if(responseCode == HttpURLConnection.HTTP_OK){  // if successful connection
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

        // substring to find dollar amount
        int dollarEndIndex = toReturn.indexOf("class=\"sx-price-fractional") - 32;
        int dollarStartIndex = dollarEndIndex - 5;
        toReturn = toReturn.substring(dollarStartIndex, dollarEndIndex);
        int dollar = Integer.parseInt(toReturn.replaceAll("[\\D]", ""));

        // substring to find cents amount
        toReturn = response.toString();
        int centsStartIndex = dollarEndIndex + 59;
        int centsEndIndex = centsStartIndex + 4;
        toReturn = toReturn.substring(centsStartIndex, centsEndIndex);
        int cents = Integer.parseInt(toReturn.replaceAll("[\\D]", ""));

        // combine into one string
        if (cents == 0){
            toReturn = dollar + "." + cents + "0";
        }
        else {
            toReturn = dollar + "." + cents;
        }
        return toReturn;
    }
}