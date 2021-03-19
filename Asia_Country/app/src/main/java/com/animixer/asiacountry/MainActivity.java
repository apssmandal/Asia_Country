package com.animixer.asiacountry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Delete;
import androidx.room.Query;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mCountryList;
    private ProgressDialog mProgress;
    private ConstraintLayout mDelete;
    private CountryAdapter countryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCountryList = findViewById(R.id.main_recycler_view);
        mDelete = findViewById(R.id.main_delete);
        mProgress = new ProgressDialog(MainActivity.this);
        mProgress.setMessage("Please wait");
        mProgress.setCanceledOnTouchOutside(false);
        mCountryList.setLayoutManager(new LinearLayoutManager(this));



        //************** Checking data already exists or not **************

        AppDatabase db  = AppDatabase.getDbInstance(MainActivity.this);

        int countryNumber = db.countryDao().getCount();
        if (countryNumber==0){

            //************** IF length zero calling api to get list **************
            GetCountries();
        }
        else {

            //************** IF length not zero loading list **************
            LoadCountryList();
        }


        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //************** Making sure if really want to delete **************
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Delete all data from database ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // On click yes, deleting database

                                db.countryDao().deleteAll();

                                Toast.makeText(MainActivity.this,"Data deleted",Toast.LENGTH_SHORT).show();
                                LoadCountryList();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button

                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });





    }


    private void LoadCountryList(){
        countryAdapter = new CountryAdapter(this);

        mCountryList.setAdapter(countryAdapter);

        AppDatabase database = AppDatabase.getDbInstance(this.getApplicationContext());
        List<Country> countries = database.countryDao().getAllCountry();
        countryAdapter.setCountryList(countries);

    }


    private void GetCountries() {

        String url = "https://restcountries.eu/rest/v2/region/asia";

        // Getting country by volley library

        mProgress.show();

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonArrayRequest jar = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    int size = response.length();


                    for (int i = 0; i<size; i++){
                    JSONObject all_details = response.getJSONObject(i);

                    // Getting all the data from jsonArray

                        String Name = all_details.getString("name");
                        String Capital = all_details.getString("capital");
                        String Region = all_details.getString("region");
                        String SubRegion = all_details.getString("subregion");
                        String Population = all_details.getString("population");
                        String Flag = all_details.getString("flag");
                        JSONArray AllBorders = all_details.getJSONArray("borders");

                        String Borders = "";
                        String Languages = "";

                        for (int j = 0;j<AllBorders.length();j++){



                            if (Borders.equals("")){
                                Borders = AllBorders.getString(j);
                            }
                            else {
                                Borders = Borders+", "+AllBorders.getString(j);
                            }


                        }

                        JSONArray AllLanguages = all_details.getJSONArray("languages");

                        for (int k = 0;k<AllLanguages.length(); k++){


                            JSONObject language_details = AllLanguages.getJSONObject(k);

                            if (Languages.equals("")){
                                Languages = language_details.getString("name");
                            }
                            else {
                                Languages = Languages+", "+language_details.getString("name");
                            }


                        }

                        // Saving all data to database

                        AppDatabase db  = AppDatabase.getDbInstance(MainActivity.this);

                        Country country = new Country();
                        country.Name = Name;
                        country.Capital = Capital;
                        country.Region = Region;
                        country.SubRegion = SubRegion;
                        country.Image = Flag;
                        country.Population = Population;
                        country.Borders = Borders;
                        country.Language = Languages;
                        db.countryDao().insertCountry(country);


                        // If it is last array, Creating country list

                        if (i==size-1){
                            mProgress.dismiss();
                            LoadCountryList();

                            queue.getCache().clear();
                        }

                    }



                } catch (JSONException e) {

                    mProgress.dismiss();

                    Toast.makeText(MainActivity.this,"JSON : "+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mProgress.dismiss();

                Toast.makeText(MainActivity.this,"VOLLEY : "+error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jar);


    }

}