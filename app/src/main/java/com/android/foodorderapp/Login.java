package com.android.foodorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements Logint {
    int RC_SIGN_IN=110;
    ImageView ProfileImage;
    TextView Name,Gmail;
    Button logout;
    com.google.android.gms.common.SignInButton gsignin;
    private  String TAG="Login";
    private FirebaseAuth mAuth;
    ActionBar actionBar;
   static GoogleSignInClient mGoogleSignInClient;
   ScriptGroup.Binding mBinding;
// ...
// Initialize Firebase Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ProfileImage=findViewById(R.id.userImage);
      //  logout=findViewById(R.id.Logout);
        gsignin=findViewById(R.id.GoogleSignIn);
         actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("650910004442-8dp83foub5t80iko0vet9vgscbcquhgs.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//          Toast.makeText(this,"create",Toast.LENGTH_LONG).show();
//        if(currentUser!=null)
//            updateUI(currentUser);


    gsignin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    });
    }
  public void onStart() {
       super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
      //  Toast.makeText(this,currentUser.getPhoneNumber().toString(),Toast.LENGTH_LONG).show();
       if(currentUser!=null)
        updateUI(currentUser);
    //  Toast.makeText(this,"Loginst",Toast.LENGTH_LONG).show();
   }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       // Toast.makeText(this, String.valueOf(resultCode), Toast.LENGTH_SHORT).show();

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && resultCode== AppCompatActivity.RESULT_OK) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
          //  Toast.makeText(this, account.getIdToken(), Toast.LENGTH_SHORT).show();

            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "check", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            Toast.makeText(Login.this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this,"Sign In Failed", Toast.LENGTH_SHORT).show();

                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if(user!=null){
        Intent intent=new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("account",user);
        startActivity(intent);
        finish();
          }
    }


//    public void signIn(View view) {
////        FirebaseUser currentUser = mAuth.getCurrentUser();
////      //  Toast.makeText(this,currentUser.getPhoneNumber().toString(),Toast.LENGTH_LONG).show();
////       if(currentUser!=null)
////        updateUI(currentUser);
////       else{
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

//    public void Logout(View view) {
//
//      // Toast.makeText(this,currentUser.getPhoneNumber().toString(),Toast.LENGTH_LONG).show();
//        switch (view.getId()) {
//            case R.id.Logout:
//                signOut();
//                break;
//        }
//        }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Login.this, "SignOut", Toast.LENGTH_SHORT).show();
                      //  Log.d(TAG,"LOGOUT");
                    }
                });
    }

}