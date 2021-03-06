package com.anicket.yogai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    //Variable Declaration
    EditText mEmail,mPassword1,mPassword2;
    Button mRegisterBtn;
    TextInputLayout editTextEmail, editTextPassword, editTextConfirmPassword;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    //Google SignIn
    ImageButton googleSignInBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private  static final int RC_SIGN_IN=123;

    private boolean validateEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail=findViewById(R.id.email);
        mPassword1=findViewById(R.id.password1);
        mPassword2=findViewById(R.id.password2);
        mRegisterBtn=findViewById(R.id.buttonSignIn);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        progressBar = findViewById(R.id.progressBar);
        googleSignInBtn= findViewById(R.id.buttonGoogle);

        //Firebase Auth Instance
        fAuth=FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password1=mPassword1.getText().toString().trim();
                String password2=mPassword2.getText().toString().trim();

                //Validation Check
                if(!validateEmail(email)){
                    editTextEmail.setError("Valid Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    editTextPassword.setError("Password is required");
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    editTextConfirmPassword.setError("Password is required");
                    return;
                }
                if(password1.length()<5 ){
                    editTextPassword.setError("Password must not be less than 5 characters");
                    return;
                }
                if(!(password1.equals(password2))){
                    editTextConfirmPassword.setError("Password's do not match ");
                    return;
                }

                //Progress Bar
                progressBar.setVisibility(View.VISIBLE);

                //Register the user in Firebase
                fAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            //To Finish all the previous activities
                            finishAffinity();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        //Register through Google
        createRequest();
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

    }

    private void createRequest() {

        //Creating Request to Google for user google accounts signIn
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        //Transfering request to Google SignIn client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    //When User clicks on google signIn btn then intent is fired and all his google accounts are displayed
    private void signIn() {
        progressBar.setVisibility(View.INVISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //Result carried by intent when user choose google account to signIn for
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }


    public void backButton(View view) {
        finish();
    }
}