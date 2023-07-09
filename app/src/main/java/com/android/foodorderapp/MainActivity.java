package com.android.foodorderapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.TaskInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;


import com.android.foodorderapp.adapters.RestaurantListAdapter;

import com.android.foodorderapp.model.RestaurantModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.api.ResourceDescriptor;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RestaurantListAdapter.RestaurantListClickListener {
    List<RestaurantModel> restList=null;
    RestaurantListAdapter adapter=null;
    FirebaseUser user;
    private boolean aBoolean=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aBoolean=false;
        Bundle data = getIntent().getExtras();
        user=data.getParcelable("account");
     //   Toast.makeText(MainActivity.this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        Log.d("USER",user.getPhoneNumber());

        ActionBar actionBar = getSupportActionBar();      //setting of a tittle
        actionBar.setTitle("Restaurant List");

        List<RestaurantModel> restaurantModelList =  getRestaurantData();

        initRecyclerView(restaurantModelList);
      //  Toast.makeText(this, String.valueOf(TaskInfo), Toast.LENGTH_SHORT).show();
    }

    private void initRecyclerView(List<RestaurantModel> restaurantModelList ) {
        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
     adapter = new RestaurantListAdapter(restaurantModelList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<RestaurantModel> getRestaurantData() {
        InputStream is = getResources().openRawResource(R.raw.restaurent);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try{
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while(( n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0,n);
            }
        }catch (Exception e) {

        }

        String jsonStr = writer.toString();
        Gson gson = new Gson();
        RestaurantModel[] restaurantModels =  gson.fromJson(jsonStr, RestaurantModel[].class);
         restList = Arrays.asList(restaurantModels);

        return  restList;

    }
    void fillter( String st){
        List<RestaurantModel> restList2=new ArrayList<>();
        for(RestaurantModel rm:restList){
            if(rm.getName().toLowerCase().contains(st.toLowerCase())){
             restList2.add(rm);
            }

        }
        adapter.updateData(restList2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


      getMenuInflater().inflate(R.menu.manu,menu);
//      MenuItem menuitem=menu.findItem(R.id.action_search);
//      androidx.appcompat.widget.SearchView searchView=(androidx.appcompat.widget.SearchView) menuitem.getActionView();
//        MenuItem menuitem2= menu.findItem(R.id.history);
//        ImageView ib= (ImageView) menuitem2.getActionView();
       // Toast.makeText(MainActivity.this, user.getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();
//        Glide.with(MainActivity.this)
//                .load(user.getPhoneNumber())
//                .into(ib);
//        ib.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, user.getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        });

     //  searchView.setQueryHint("Search Here");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//               fillter(newText);
//                return false;
//            }
//        });







        return super.onCreateOptionsMenu(menu);
    }
    void search123( SearchView searchView){
          searchView.setQueryHint("Search Here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               fillter(newText);
                return false;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        switch (id){
            case R.id.action_search:
                search123((SearchView) item.getActionView());
                break;
            case R.id.Profile:
                Intent i=new Intent(MainActivity.this,Profile.class);
                i.putExtra("account",user);
                startActivity(i);
                break;
            case R.id.History:
                Intent i2=new Intent(MainActivity.this,Order_history.class);
                i2.putExtra("userid",user.getUid());
                startActivity(i2);
              //  Intent i1=new Intent(MainActivity.this, History.class));
                break;
        }

        return false;
    }

    @Override
    public void onItemClick(RestaurantModel restaurantModel) {
        Intent intent = new Intent(MainActivity.this, RestaurantMenuActivity.class);
        intent.putExtra("RestaurantModel", restaurantModel);
        intent.putExtra("uid",user.getUid());
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {

        if(aBoolean){
            super.onBackPressed();
       setResult(Activity.RESULT_CANCELED);
            finish();}
        aBoolean=true;

    }
}