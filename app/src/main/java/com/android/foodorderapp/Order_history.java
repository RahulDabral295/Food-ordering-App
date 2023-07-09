package com.android.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.foodorderapp.adapters.Orderhistoryadepter;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.model.RestaurantModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Order_history extends AppCompatActivity {
    FirebaseFirestore db ;
    RecyclerView rv;
    List<Map<String,Object>> rma ;
    Orderhistoryadepter adapter;
    androidx.swiperefreshlayout.widget.SwipeRefreshLayout sv;
    String TAG="ORD";
   private String UID;
    List<Object> mn;
    Map<String,Object> mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        UID=getIntent().getStringExtra("userid");
        rv=findViewById(R.id.recyclerviewHistory);
        sv=findViewById(R.id.swip);
        db = FirebaseFirestore.getInstance();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Orders History");
        actionBar.setDisplayHomeAsUpEnabled(false);
       // Toast.makeText(this, "String.valueOf(rma.size())", Toast.LENGTH_SHORT).show();
        rma=new ArrayList<>();
//        sv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {

//        RestaurantModel rr;
//        rr=new RestaurantModel();
//        rr.setName("Risab");
//        rma.add(0,rr);
//        rr=new RestaurantModel();
//        rr.setName("Risab2");
//        rma.add(0,rr);
//        rr=new RestaurantModel();
//        rr.setName("Risab4");
//        rma.add(0,rr);
      //  Toast.makeText(this, String.valueOf(rma.size()), Toast.LENGTH_SHORT).show();

        rv.setLayoutManager(new LinearLayoutManager(Order_history.this));
        adapter = new Orderhistoryadepter(this);
        rv.setAdapter(adapter);
        getdata();
        sv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();

            }

        });

//            }
//        });

    }


//    private String getDate(Object dat) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        String dateString = simpleDateFormat.format();
//        return String.format("Date: %s", dateString);
//    }
    void getdata() {
        db.collection(UID)
              //  .document(UID)
                .orderBy("Time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       // RestaurantModel[] r=new RestaurantModel[100];

                        if (task.isSuccessful() && task.getResult().size()>rma.size()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                mp=document.getData();
                                rma.add(mp);

//                                List<Map<String,Object>> mp2=(List<Map<String,Object>>)mp.get("Items");
//                                mp2.get(0).get("name");
//                                mp = document.getData();
//                                mn=new ArrayList<Object>();
//                                RestaurantModel rm = new RestaurantModel();
//                                rm.setName(String.valueOf(mp.get("Restorent_Name")));
//                               // mn.addAll(List<Menu> mp.get("Items"));
//                                rm.setAddress(""+mp.get("Time"));
//                               // mn=mp.get("Items").;
//
//                               mn.add(mp.get("Items"));
//
                             // Toast.makeText(Order_history.this,mp2.get(0).get("name").toString(),Toast.LENGTH_LONG).show();
//                               // rma= Arrays.asList(r);
//                                Log.d(TAG, document.getId() + " => " +mp.get("Restorent_Name"));
//                                rma.add(rm);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        adapter.updateNews(rma);

                    }
                });

sv.setRefreshing(false);
    }





    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }


}