package com.xncoder.nicd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class LogoActivity extends AppCompatActivity {

    private boolean isHome, isWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView image = findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_anim);
        image.startAnimation(animation);

        isHome = false;
        isWelcome = true;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        CredentialDB credentials = new CredentialDB(this);
        ArrayList<String> getUser = credentials.getAllUsers();
        if(getUser.get(0) != null) {
            auth.signInWithEmailAndPassword(getUser.get(0), getUser.get(1))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if(user != null && user.isEmailVerified()) {
                                isHome = true;
                            } else {
                                isWelcome = false;
                            }

                        } else {
                            isWelcome = false;
                        }
                    });
        }

        new Handler().postDelayed(() -> {
            if(isHome) {
                Intent home = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(home);
            } else if(isWelcome) {
                Intent welcome = new Intent(LogoActivity.this, WelcomeActivity.class);
                startActivity(welcome);
            } else {
                Intent login = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(login);
            }
            finish();
        }, 4000);

    }
}