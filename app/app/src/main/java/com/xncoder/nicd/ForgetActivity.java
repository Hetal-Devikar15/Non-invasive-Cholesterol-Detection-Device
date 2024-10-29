package com.xncoder.nicd;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetActivity extends AppCompatActivity {

    private Popup popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText emailText = findViewById(R.id.fg_email);
        Button fgSend = findViewById(R.id.fgButton);
        fgSend.setOnClickListener(view -> {
            String email = String.valueOf(emailText.getText());
            if(TextUtils.isEmpty(email)) {
                popupWindow = new Popup(getApplicationContext(), false, "Please enter your email address");
                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
            } else {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                popupWindow = new Popup(getApplicationContext(), true, "Password reset email sent successfully");
                                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                                popupWindow.onBtn.setOnClickListener(view1 -> {
                                    finish();
                                });
                            } else {
                                popupWindow = new Popup(getApplicationContext(), false, "Error: " + Objects.requireNonNull(task.getException()).getMessage());
                                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                            }
                        });
            }
        });
    }
}