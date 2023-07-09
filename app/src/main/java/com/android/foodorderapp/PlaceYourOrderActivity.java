package com.android.foodorderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.foodorderapp.adapters.PlaceYourOrderAdapter;
import com.android.foodorderapp.model.Menu;
import com.android.foodorderapp.model.RestaurantModel;
import com.android.foodorderapp.model.Userinfo;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

public class PlaceYourOrderActivity extends AppCompatActivity {

    private EditText inputName, inputAddress, inputCity, inputState, inputZip,inputCardNumber, inputCardExpiry,inputCardExpiry2, inputCardPin,inputPhone ;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvSubtotalAmount, tvDeliveryChargeAmount, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private SwitchCompat switchDelivery;
    private boolean isDeliveryOn;
    private PlaceYourOrderAdapter placeYourOrderAdapter;
    Userinfo userinfo;
    ActionBar actionBar;
    String uid ;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_your_order);

        RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
        uid=getIntent().getStringExtra("uid");
        actionBar = getSupportActionBar();
        actionBar.setTitle(restaurantModel.getName());
        actionBar.setSubtitle(restaurantModel.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputName = findViewById(R.id.inputName);
        inputPhone=findViewById(R.id.inputPhone);
        inputAddress = findViewById(R.id.inputAddress);
        inputCity = findViewById(R.id.inputCity);
        inputState = findViewById(R.id.inputState);
        inputZip = findViewById(R.id.inputZip);
        inputCardNumber= findViewById(R.id.inputCardNumber);
        inputCardExpiry = findViewById(R.id.inputCardExpiry);
        inputCardExpiry2=findViewById(R.id.inputCardExpiry2);
        inputCardPin = findViewById(R.id.inputCardPin);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceYourOrder);
        switchDelivery = findViewById(R.id.switchDelivery);
        inputZip.setAutofillHints(View.AUTOFILL_HINT_POSTAL_CODE);
        inputAddress.setAutofillHints(View.AUTOFILL_HINT_POSTAL_ADDRESS);
        inputCardNumber.setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_NUMBER);
        inputCardExpiry.setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH);
        inputCardExpiry2.setAutofillHints(View.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR);

        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);

        buttonPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderButtonClick(restaurantModel);
            }
        });

        switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    inputAddress.setVisibility(View.VISIBLE);
                    inputCity.setVisibility(View.VISIBLE);
                    inputState.setVisibility(View.VISIBLE);
                    inputZip.setVisibility(View.VISIBLE);
                    tvDeliveryChargeAmount.setVisibility(View.VISIBLE);
                    tvDeliveryCharge.setVisibility(View.VISIBLE);
                    isDeliveryOn = true;
                    actionBar.hide();
                } else {
                    inputAddress.setVisibility(View.GONE);
                    inputCity.setVisibility(View.GONE);
                    inputState.setVisibility(View.GONE);
                    inputZip.setVisibility(View.GONE);
                    tvDeliveryChargeAmount.setVisibility(View.GONE);
                    tvDeliveryCharge.setVisibility(View.GONE);
                    isDeliveryOn = false;
                    actionBar.show();
                }
                calculateTotalAmount(restaurantModel);
            }
        });
        initRecyclerView(restaurantModel);
        calculateTotalAmount(restaurantModel);
       // Toast.makeText(this, year(), Toast.LENGTH_SHORT).show();

    }


    private int getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        String dateString = simpleDateFormat.format(System.currentTimeMillis());
        return  Integer.parseInt(dateString);
    }
    private int year() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMM");
        String dateString = simpleDateFormat.format(System.currentTimeMillis());

        return  Integer.parseInt(dateString);
    }


    private void calculateTotalAmount(RestaurantModel restaurantModel) {
        float subTotalAmount = 0f;
            if( restaurantModel.getMenus()!=null)
        for(Menu m : restaurantModel.getMenus()) {
            subTotalAmount += m.getPrice() * m.getTotalInCart();
        }

        tvSubtotalAmount.setText("₹"+String.format("%.2f", subTotalAmount));
        if(isDeliveryOn) {
            tvDeliveryChargeAmount.setText("₹"+String.format("%.2f", restaurantModel.getDelivery_charge()));
            subTotalAmount += restaurantModel.getDelivery_charge();
        }
        tvTotalAmount.setText("₹"+String.format("%.2f", subTotalAmount));
    }

    private boolean isValid(long number)
    {
        return (getSize(number) >= 13 &&
                getSize(number) <= 16) &&
                (prefixMatched(number, 4) ||
                        prefixMatched(number, 5) ||
                        prefixMatched(number, 37) ||
                        prefixMatched(number, 6)) &&
                ((sumOfDoubleEvenPlace(number) +
                        sumOfOddPlace(number)) % 10 == 0);
    }

    // Get the result from Step 2
    private int sumOfDoubleEvenPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);

        return sum;
    }

    // Return this number if it is a single digit, otherwise,
    // return the sum of the two digits
    private int getDigit(int number)
    {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    // Return sum of odd-place digits in number
    private int sumOfOddPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    // Return true if the digit d is a prefix for number
    private boolean prefixMatched(long number, int d)
    {
        return getPrefix(number, getSize(d)) == d;
    }

    // Return the number of digits in d
    private int getSize(long d)
    {
        String num = d + "";
        return num.length();
    }

    // Return the first k number of digits from
    // number. If the number of digits in number
    // is less than k, return number.
    private long getPrefix(long number, int k)
    {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }

    private void onPlaceOrderButtonClick(RestaurantModel restaurantModel) {
        if(Integer.parseInt(inputCardExpiry.getText().toString())!=0 &&inputCardExpiry.getText().toString().length()==1)
            inputCardExpiry.setText("0"+inputCardExpiry.getText());


        if(TextUtils.isEmpty(inputName.getText().toString().trim())) {
            inputName.setError("Please enter name ");
            return;
        }
        else if(!Pattern.matches("(0|91)?[5-9][0-9]{9}",inputPhone.getText().toString())) {
            inputPhone.setError("Please enter valid Phone no. ");
            return;
        }
        else if(isDeliveryOn && TextUtils.isEmpty(inputAddress.getText().toString().trim())) {
            inputAddress.setError("Please enter valid address ");
            return;
        }else if(isDeliveryOn && TextUtils.isEmpty(inputCity.getText().toString().trim())) {
            inputCity.setError("Please enter valid city ");
            return;
        }else if(isDeliveryOn && inputState.getText().toString().length()<6) {
            inputState.setError("Please enter valid Pin ");
            return;
        }else if( TextUtils.isEmpty(inputCardNumber.getText().toString().trim())||!isValid(Long.parseLong(inputCardNumber.getText().toString()))) {
            inputCardNumber.setError("Please enter correct card number ");
            return;
        }else if( Integer.parseInt(inputCardExpiry.getText().toString())<1||Integer.parseInt(inputCardExpiry.getText().toString())>12 ) {
            inputCardExpiry.setError("MM");
            return;}

        else if( !Pattern.matches("20[2-5][0-9]",inputCardExpiry2.getText().toString())) {
                inputCardExpiry2.setError("YYYY");
                return;}
        else if( inputCardPin.getText().toString().length()<3) {
            inputCardPin.setError("Please enter card pin/cvv ");
            return;
        }
        else if(Integer.parseInt(inputCardExpiry2.getText().toString()+inputCardExpiry.getText().toString())<year()){
         //   Toast.makeText(this,String.valueOf(year()), Toast.LENGTH_SHORT).show();

//            Snackbar snackbar = Snackbar
//                    .make(inputCardExpiry, "www.journaldev.com", Snackbar.LENGTH_LONG).setAction("100", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
                           Toast.makeText(PlaceYourOrderActivity.this, "YOUR CARD HAS EXPIRED THIS YEAR", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//            snackbar.show();

       //     Toast.makeText(PlaceYourOrderActivity.this, "YOUR CARD HAS EXPIRED THIS YEAR", Toast.LENGTH_SHORT).show();
            return;
        }


         userinfo=new Userinfo(inputName.getText().toString().trim(),inputPhone.getText().toString(),inputCardNumber.getText().toString(),inputCardExpiry.getText().toString(),inputCardPin.getText().toString(), inputAddress.getText().toString(),inputZip.getText().toString(),inputCity.getText().toString(),inputState.getText().toString());
      //  Toast.makeText(this, inputCardNumber.getText().toString(), Toast.LENGTH_SHORT).show();
        //start success activity..


        Intent i = new Intent(PlaceYourOrderActivity.this, OrderSucceessActivity.class);
  //      Log.d("MAIN",userinfo.Address);
    //    i.putExtra("Name",inputName.getText().toString());
//        i.putExtra("Phone",inputPhone.getText().toString());
      //  Bundle b=new Bundle();
       // b.putParcelable("v1",restaurantModel);
       // b.putParcelable("User1",userinfo);
       // System.out.println(userinfo);
        i.putExtra("User1",userinfo);
        i.putExtra("v1", restaurantModel);
        i.putExtra("uid",uid);
       // Toast.makeText(this,"B"+String.valueOf(restaurantModel),Toast.LENGTH_LONG).show();
        startActivityForResult(i, 1000);
    }

    private void initRecyclerView(RestaurantModel restaurantModel) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceYourOrderAdapter(restaurantModel.getMenus());
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     //   Toast.makeText(this, "Place your order", Toast.LENGTH_SHORT).show();
        if(requestCode == 1000) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}