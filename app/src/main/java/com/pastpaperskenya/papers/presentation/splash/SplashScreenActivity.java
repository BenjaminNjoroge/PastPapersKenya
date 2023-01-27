package com.pastpaperskenya.papers.presentation.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pastpaperskenya.papers.presentation.auth.AuthActivity;
import com.pastpaperskenya.papers.presentation.main.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        if (user !=null){
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(SplashScreenActivity.this, AuthActivity.class));
            finish();
        }
    }
}
