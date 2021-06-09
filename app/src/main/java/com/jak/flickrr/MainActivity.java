package com.jak.flickrr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private Button exploreButton;
    private LinearLayout linearLayout;
    FloatingActionButton fab;

    private List<Item> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.linearLayout);
        exploreButton = findViewById(R.id.button);
        fab = findViewById(R.id.fab);

        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });


        mDrawerLayout =  findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // working gallery recyclerView

        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // using volley library to get requestQueue
        fetchDataFromAPI();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Refreshing. . .", Toast.LENGTH_SHORT).show();
                fetchDataFromAPI();
            }
        });

    }

    private void fetchData() {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&per_page=20&page=1&api_key=6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("photos");
                    Log.d("Photos", jsonObject.toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("photo");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String imageUrl = jsonObject1.getString("url_s");
                        String title = jsonObject1.getString("id"); // fetching id rather than title as id is common to all

                        Item postItem = new Item(imageUrl, title);
                        mList.add(postItem);
                    }

                    PostAdapter postAdapter = new PostAdapter(MainActivity.this, mList);
                    recyclerView.setAdapter(postAdapter);
                    postAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.d("Error: ", "Loading failed");
                     e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

        //        int id = item.getItemId();
        //
        //        if (id == R.id.home) {
        //            fetchDataFromAPI();
        //        }
        //
        //        if (id == R.id.faq) {
        //            Toast.makeText(this, "FAQ clicked", Toast.LENGTH_LONG).show();
        //            return true;
        //        }
        //        if (id == R.id.about) {
        //            Toast.makeText(this, "Showing About Us", Toast.LENGTH_LONG).show();
        //            return true;
        //        }
        //
        //        return super.onOptionsItemSelected(item);
    }

    private void fetchDataFromAPI(){
        requestQueue = VolleySingleton.getmInstance(MainActivity.this).getRequestQueue();

        fetchData();

        mList = new ArrayList<>();
    }

}
