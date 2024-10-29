package com.xncoder.nicd;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<Integer, Class<? extends Fragment>> fragmentMap;
    private final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        String temp = new CredentialDB(this).getAllUsers().get(0);
        String USERNAME = temp.substring(0, temp.indexOf('@'));
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.username);
        textView.setText(USERNAME);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.nav_home, HomeFragment.class);
        fragmentMap.put(R.id.nav_data, DataFragment.class);
        fragmentMap.put(R.id.nav_precautions, PrecautionFragment.class);
        navigationView.setCheckedItem(R.id.nav_home);
        displaySelectedFragment(R.id.nav_home);

        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_logout) {
                new CredentialDB(this).clearDatabase();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
            if(item.getItemId() == R.id.nav_data) {

            }
            displaySelectedFragment(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void displaySelectedFragment(int itemId) {
        Class<? extends Fragment> fragmentClass = fragmentMap.get(itemId);
        if (fragmentClass != null) {
            try {
                Fragment fragment = fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Location permission required for Wi-Fi connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}