package com.ksucapstone2016.myfoodstuff;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
//import android.net.Uri;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;

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
    ListView lv = null;
    ArrayList<String> masterItems = new ArrayList<String>();
    ArrayList<Float> WalmartPrices = new ArrayList<Float>();
    ArrayList<Float> TargetPrices = new ArrayList<Float>();
    ArrayList<Float> bestPrices = new ArrayList<Float>();
    ArrayList<String> bestList = new ArrayList<String>();


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        masterItems = getArrayVal(getApplicationContext());


        // Added by Aaron

        // example prices (currently hard-coded):
        // Walmart:
            // eggs 2.50
            // milk 4.00
            // cheese 2.25
            // bread 3.00
        // Target:
            // eggs 2.75
            // milk 4.00
            // cheese 2.50
            // bread 2.75

        // can comment out all the adds
        // but DO NOT REMOVE THEM ~~~Aaron
        masterItems.add("Eggs");
        // get price for eggs from Walmart API
        WalmartPrices.add((float) 2.50);
        // get price for eggs from Target API
        TargetPrices.add((float) 2.75);

        masterItems.add("Milk");
        // get price for milk from Walmart API
        WalmartPrices.add((float) 4.00);
        // get price for milk from Target API
        TargetPrices.add((float) 4.00);

        masterItems.add("Cheese");
        // get price for cheese from Walmart API
        WalmartPrices.add((float) 2.25);
        // get price for cheese from Target API
        TargetPrices.add((float) 2.50);

        masterItems.add("Bread");
        // get price for bread from Walmart API
        WalmartPrices.add((float) 3.00);
        // get price for bread from Target API
        TargetPrices.add((float) 2.75);

        Double WalmartTotal = 0.00;
        Double TargetTotal = 0.00;
        Double bestTotal = 0.00;
        Integer largestList = null;

        if(WalmartPrices.size() > TargetPrices.size())
            largestList = WalmartPrices.size();
        else
            largestList = TargetPrices.size();

        // Walmart stuff
        for (Integer i = 0; i < WalmartPrices.size(); ++i) {
            WalmartTotal += WalmartPrices.get(i);
            System.out.printf("Walmart " + masterItems.get(i) + " = $" + "%1.2f", WalmartPrices.get(i));
            System.out.println();
        }

        System.out.printf("One trip to Walmart Total: $" + "%1.2f", WalmartTotal);
        System.out.println();
        System.out.println();

        // Target stuff
        for (Integer i = 0; i < TargetPrices.size(); ++i){
            TargetTotal += TargetPrices.get(i);
            System.out.printf("Target " + masterItems.get(i) + " = $" + "%1.2f", TargetPrices.get(i));
            System.out.println();
        }

        System.out.printf("One trip to Target Total: $" + "%1.2f", TargetTotal);
        System.out.println();
        System.out.println();

        for (Integer i = 0; i < largestList; ++i){
            if (WalmartPrices.get(i) <= TargetPrices.get(i)) {
                bestPrices.add((float) WalmartPrices.get(i));
                bestTotal += WalmartPrices.get(i);
                bestList.add("Walmart");
            } else {
                bestPrices.add((float) TargetPrices.get(i));
                bestTotal += TargetPrices.get(i);
                bestList.add("Target");
            }
        }
        System.out.println();
        System.out.println("Best prices for a multiple store trip:");

        for(Integer i = 0; i < bestPrices.size(); ++i)
        {
            System.out.printf(bestList.get(i) + " " + masterItems.get(i) + ": $" + "%1.2f", bestPrices.get(i));
            System.out.println();
        }

        System.out.printf("Multiple store total: $" + "%1.2f", bestTotal);
        // End Add by Aaron

        Collections.sort(masterItems);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, masterItems);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

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
                    masterItems.add(preferredCase(input.getText().toString()));
                    Collections.sort(masterItems);
                    storeArrayVal(masterItems, getApplicationContext());
                    lv.setAdapter(adapter);
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
                    lv.setAdapter(adapter);
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
        prefEditor.commit();
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
                Collections.sort(masterItems);
                storeArrayVal(masterItems, getApplicationContext());
                lv.setAdapter(adapter);
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
