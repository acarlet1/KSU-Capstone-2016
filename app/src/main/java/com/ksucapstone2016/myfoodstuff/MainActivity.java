package com.ksucapstone2016.myfoodstuff;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
//import android.net.Uri;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnKeyListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.text.NumberFormat;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.text.DecimalFormat;

import java.util.HashSet;
import java.util.Set;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;

import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.TextView;
/*import android.view.View;
import android.view.Menu;
import android.view.MenuItem;*/
import android.view.MotionEvent;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import android.app.SearchManager;
import android.widget.SearchView.OnQueryTextListener;
/*import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.support.v4.view.GestureDetectorCompat;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
*/

public class MainActivity extends AppCompatActivity {
    //ArrayList<String> shoppingList = null; shopping list ---> masterItems
    ArrayAdapter<String> adapter = null;
    ArrayAdapter<Double> adapter2 = null;
    ArrayAdapter<String> adapter3 = null;

    ListView lv = null;
    ListView lv2 = null;
    ListView lv3 = null;

    ArrayList<String> masterItems = new ArrayList<String>();
    ArrayList<Double> w_prices = new ArrayList<Double>();
    ArrayList<String> w_priceString = new ArrayList<>();
    ArrayList<Double> a_prices = new ArrayList<Double>();
    ArrayList<Float> WalmartPrices = new ArrayList<Float>();
    ArrayList<Float> TargetPrices = new ArrayList<Float>();
    ArrayList<Float> bestPrices = new ArrayList<Float>();
    ArrayList<String> bestList = new ArrayList<String>();

    //String priceNum = null;
    Double PRICE = 0.00;
    Double TOTAL = 0.00;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private String TAG = MainActivity.class.getSimpleName();
    private EditText btnStringReq;
    private TextView msgResponse;
    private ProgressDialog pDialog;
    private String result;

    // This tag will be used to cancel the request
    private String tag_string_req = "string_req";

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        masterItems = getArrayVal(getApplicationContext());

        //btnStringReq = (EditText) findViewById(R.id.item_query);

        //msgResponse = (TextView) findViewById(R.id.msgResponse);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);


        //Collections.sort(masterItems);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, masterItems);
        adapter2 = new ArrayAdapter<Double>(this, android.R.layout.simple_list_item_1, w_prices);
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, w_priceString);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        //lv2 = (ListView) findViewById(R.id.wPrice);
        //lv2.setAdapter(adapter2);
        lv3 = (ListView) findViewById(R.id.wPrice);
        lv3.setAdapter(adapter3);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, final int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItem.trim().equals(masterItems.get(position).trim())) {
                    removeElement(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(), "Error Removing Element", Toast.LENGTH_LONG).show();
                }
            }
        });


        //End of addition//
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void makeStringReq(String ans) throws Exception{
        showProgressDialog();
        EditText mEdit;

        //System.out.println("searching for " + ans);
        StringRequest strReq = new StringRequest(Method.GET,
                "http://api.walmartlabs.com/v1/search?apiKey=52pcepcteuhhwx7gtg5z7dbe&query="
                        + ans, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //System.out.println("response = " + response);

                //Log.d(TAG, response.toString());
                result = response;
                int a = result.indexOf("salePrice");
                int b = result.indexOf("upc");
                String finalResult;
                finalResult = result.substring(a + 11, b - 2);

                //System.out.println("final result = " + finalResult);
                //msgResponse.setText(response);
                float finalResultFloat = Float.parseFloat(finalResult);
                PRICE = Double.parseDouble(finalResult);
                DecimalFormat f = new DecimalFormat("##.00");
                System.out.println("Walmart Price = " + f.format(finalResultFloat));
                WalmartPrices.add(finalResultFloat);

                lv3.setAdapter(adapter3);


                w_prices.add(PRICE);
                w_priceString.add(f.format(PRICE));
                System.out.println("Testing prices string: " + w_priceString);

                System.out.println(WalmartPrices);
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //^changing this to put the value into a variable

        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);

        //For the search function -Adam
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });


        return true;
        // end search view additions
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (id == R.id.action_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Item");

           final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // adds item user wishes to add to masterItems
                    //masterItems.add(preferredCase(input.getText().toString()) + " $" + priceNum);
                    masterItems.add(preferredCase(input.getText().toString()));
                    NumberFormat f = new DecimalFormat("#0.00");
                    System.out.println("User wants: " + input.getText().toString());
                    try {
                        makeStringReq(input.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //System.out.println("user adds " + input.getText().toString());


                    //Collections.sort(masterItems);
                    storeArrayVal(masterItems, getApplicationContext());

                    lv.setAdapter(adapter);
                    //lv2.setAdapter(adapter2);
                    lv3.setAdapter(adapter3);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;


        }

        if (id == R.id.action_clear) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Clear Entire List");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    masterItems.clear();
                    w_prices.clear();
                    w_priceString.clear();
                    storeArrayVal(masterItems, getApplicationContext());
                    lv.setAdapter(adapter);
                    //lv2.setAdapter(adapter2);
                    lv3.setAdapter(adapter3);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }

        if (id == R.id.total_price) {
            TOTAL = 0.00;
            getTotal();
            masterItems.add("Total: ");
            w_prices.add(TOTAL);
            NumberFormat f = new DecimalFormat("#0.00");
            w_priceString.add(f.format(TOTAL));
            lv.setAdapter(adapter);
            //lv2.setAdapter(adapter2);
            lv3.setAdapter(adapter3);
        }

        return super.onOptionsItemSelected(item);
    }

    public static String preferredCase(String original) {
        if (original.isEmpty())
            return original;

        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    public static void storeArrayVal(ArrayList<String> inArrayList, Context context) {
        Set<String> WhatToWrite = new HashSet<String>(inArrayList);
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet("myArray", WhatToWrite);
        prefEditor.apply();
    }

    public static ArrayList getArrayVal(Context dan) {
        SharedPreferences WordSearchGetPrefs = dan.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        Set<String> tempSet = new HashSet<String>();
        tempSet = WordSearchGetPrefs.getStringSet("myArray", tempSet);
        return new ArrayList<String>(tempSet);
    }

    //ToDO: swap no and yes,pleaes
    public void removeElement(String selectedItem, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                masterItems.remove(position);
                w_prices.remove(position);
                //Collections.sort(masterItems);
                storeArrayVal(masterItems, getApplicationContext());
                lv.setAdapter(adapter);
                //lv2.setAdapter(adapter2);
                lv3.setAdapter(adapter3);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    public void getTotal(){
        for(int i = 0; i < w_prices.size(); i++){
            TOTAL += w_prices.get(i);
        }
    }
//ToDO: Long hold for menu option

    //ToDo: Internet data interface
//ToDo: Side slides delete items
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):

                return true;
            case (MotionEvent.ACTION_MOVE):

                return true;
            case (MotionEvent.ACTION_UP):

                return true;
            case (MotionEvent.ACTION_CANCEL):

                return true;
            case (MotionEvent.ACTION_OUTSIDE):

                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
  /*  public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                //.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]")) //Commented out by Josh
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }*/
    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect(); //Commented out by Josh
        //AppIndex.AppIndexApi.start(client, getIndexApiAction()); //Commented out by Josh
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());  //Commented out by Josh
        //client.disconnect(); //Commented out by Josh
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.disconnect();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-HTTP-HOST-HERE]/main"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}


//-----------------------------------------------------------------------------------

     /*   btnStringReq.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER) {
                    makeStringReq();
                }
                return true;
            }
        });*/

// Adding request to request queue
// AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


// Added by Josh
        /*
        mEdit = (EditText) findViewById(R.id.item_query);
        String tempText = mEdit.getText().toString();
        StringBuilder finaltxtBuilder = new StringBuilder();
        finaltxtBuilder.append(tempText);
        char[] textChars = tempText.toCharArray();
        String spacetxt = " ";
        for (int i = 0; i < tempText.length(); i++)
            if (tempText.charAt(i) == spacetxt.charAt(0)) {
                textChars[i] = '+';
                finaltxtBuilder.setCharAt(i, textChars[i]);
            }

        String finalStr = finaltxtBuilder.toString();
*/