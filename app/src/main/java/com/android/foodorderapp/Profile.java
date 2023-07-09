package com.android.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    ImageView ProfileImage;
    TextView Name,Gmail;
    FirebaseUser user;
    Logint li;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle("Profile");


         //   actionBar.setDisplayHomeAsUpEnabled(true);
            Bundle data = getIntent().getExtras();
            user=data.getParcelable("account");



          //  actionBar.setHomeButtonEnabled(true);




//            Toast.makeText(this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
//            Log.d("USERid",user.getUid());
//            Log.d("USERno", Objects.requireNonNull(user.getPhoneNumber()));
//            Log.d("USERPid",user.getProviderId());
//            Log.d("USER",user.getMetadata().toString());
//            Log.d("USER",user.getUid());
//            Log.d("USER",user.getUid());
            ProfileImage=findViewById(R.id.userImage);
            Name=findViewById(R.id.Name);
            Gmail=findViewById(R.id.gmail);
            Glide.with(Profile.this)
                    .load(user.getPhotoUrl())
                    .circleCrop()
                    .into(ProfileImage);
            Name.setText(user.getDisplayName());
            Gmail.setText(user.getEmail());

        }

    public void signInPage(View view) {

        Login.mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       // Toast.makeText(Login.this, "SignOut", Toast.LENGTH_SHORT).show();
                      //  Log.d(TAG,"LOGOUT");
                    }
                });
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Profile.this,Login.class));
        finish();
    }
}
