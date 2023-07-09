package com.android.foodorderapp;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.foodorderapp.model.RestaurantModel;
import com.android.foodorderapp.model.Userinfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class OrderSucceessActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView orderNo,status;
    TextView buttonDone;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_succeess);
        orderNo = findViewById(R.id.orderNo);
        buttonDone = findViewById(R.id.buttonDone);
        status = findViewById(R.id.textView2);
        // RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
        Bundle data = getIntent().getExtras();
        RestaurantModel restaurantModel = (RestaurantModel) data.getParcelable("v1");
        Userinfo userinfo = (Userinfo) data.getParcelable("User1");
        uid=(String) data.getString("uid");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurantModel.getName());
        actionBar.setSubtitle(restaurantModel.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(false);

        Map<String, Object> Orders = new HashMap<>();
        Orders.put("Time",System.currentTimeMillis());

        Orders.put("Items", restaurantModel.getMenus());
        Orders.put("Restorent_Name", restaurantModel.getName());
        Orders.put("User", userinfo);


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == conMgr.getActiveNetworkInfo()

                || !conMgr.getActiveNetworkInfo().isAvailable()
                || !conMgr.getActiveNetworkInfo().isConnected()) {
                    status.setText("No Data Connection");
                    buttonDone.setText("Back");
                    buttonDone.setVisibility(View.VISIBLE);
                } else {
// Add a new document with a generated ID
        db.collection(uid)
                .add(Orders)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(OrderSucceessActivity.this, " " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                        orderNo.setText(documentReference.getId());
                        status.setText("Order Places successfully");
                        buttonDone.setVisibility(View.VISIBLE);
                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.setText("Sorry,Please Try Again");
                        //   Log.w(TAG, "Error adding document", e);
                    }
                });

    }

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderSucceessActivity.this, "Done Before", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        }
    }

