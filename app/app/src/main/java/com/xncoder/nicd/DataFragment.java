package com.xncoder.nicd;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DataFragment extends Fragment {

    private ActivityResultLauncher<Intent> qrScanLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        Spinner gen = view.findViewById(R.id.genderSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"Select gender", "Male", "Female", "Others"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gen.setAdapter(adapter);
        Button btn = view.findViewById(R.id.shareBtn);

        EditText nameEd = view.findViewById(R.id.name);
        EditText ageEd = view.findViewById(R.id.age);
        EditText weightEd = view.findViewById(R.id.weight);
        EditText drEd = view.findViewById(R.id.drinker);
        EditText smkEd = view.findViewById(R.id.smoker);
        EditText dtEd = view.findViewById(R.id.diabetes);
        EditText hptEd = view.findViewById(R.id.hypertension);
        HealthDB db = new HealthDB(getContext());

        ArrayList<String> data = db.getData();
        if(!data.isEmpty()) {
            nameEd.setText(data.get(0));
            nameEd.setFocusable(false);
            nameEd.setFocusableInTouchMode(false);
            nameEd.setClickable(false);

            ageEd.setText(data.get(1));
            ageEd.setFocusable(false);
            ageEd.setFocusableInTouchMode(false);
            ageEd.setClickable(false);

            gen.setSelection(adapter.getPosition(data.get(2)));
            gen.setEnabled(false);

            weightEd.setText(data.get(3));
            weightEd.setFocusable(false);
            weightEd.setFocusableInTouchMode(false);
            weightEd.setClickable(false);

            drEd.setText(data.get(4));
            drEd.setFocusable(false);
            drEd.setFocusableInTouchMode(false);
            drEd.setClickable(false);

            smkEd.setText(data.get(5));
            smkEd.setFocusable(false);
            smkEd.setFocusableInTouchMode(false);
            smkEd.setClickable(false);

            dtEd.setText(data.get(6));
            dtEd.setFocusable(false);
            dtEd.setFocusableInTouchMode(false);
            dtEd.setClickable(false);

            hptEd.setText(data.get(7));
            hptEd.setFocusable(false);
            hptEd.setFocusableInTouchMode(false);
            hptEd.setClickable(false);

            btn.setText("Edit");
        } else {
            nameEd.setFocusable(true);
            nameEd.setFocusableInTouchMode(true);
            nameEd.setClickable(true);

            ageEd.setFocusable(true);
            ageEd.setFocusableInTouchMode(true);
            ageEd.setClickable(true);

            gen.setEnabled(true);

            weightEd.setFocusable(true);
            weightEd.setFocusableInTouchMode(true);
            weightEd.setClickable(true);

            drEd.setFocusable(true);
            drEd.setFocusableInTouchMode(true);
            drEd.setClickable(true);

            smkEd.setFocusable(true);
            smkEd.setFocusableInTouchMode(true);
            smkEd.setClickable(true);

            dtEd.setFocusable(true);
            dtEd.setFocusableInTouchMode(true);
            dtEd.setClickable(true);

            hptEd.setFocusable(true);
            hptEd.setFocusableInTouchMode(true);
            hptEd.setClickable(true);

            btn.setText("Share Now");
        }


        qrScanLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    IntentResult intentResult = IntentIntegrator.parseActivityResult(
                            result.getResultCode(),
                            result.getData()
                    );

                    if (intentResult.getContents() != null) {
                        String scannedContent = intentResult.getContents();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        Map<String, Object> userData = new HashMap<>();

                        userData.put("Name", nameEd.getText().toString().trim());
                        userData.put("Age", ageEd.getText().toString().trim());
                        userData.put("Gender", gen.getSelectedItem().toString());
                        userData.put("Weight", weightEd.getText().toString().trim());
                        userData.put("Drinker", smkEd.getText().toString().trim());
                        userData.put("Smoker", dtEd.getText().toString().trim());
                        userData.put("Diabetes", hptEd.getText().toString().trim());
                        userData.put("Hypertension", hptEd.getText().toString().trim());
                        userData.put("DateTime", new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()));

                        database.getReference("Devices/" + scannedContent).updateChildren(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "User data send successfully", Toast.LENGTH_LONG).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                        Toast.makeText(getContext(), "Scanned: " + scannedContent, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Scan Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        btn.setOnClickListener(view1 -> {

            if(btn.getText().equals("Edit")) {
                nameEd.setFocusable(true);
                nameEd.setFocusableInTouchMode(true);
                nameEd.setClickable(true);

                ageEd.setFocusable(true);
                ageEd.setFocusableInTouchMode(true);
                ageEd.setClickable(true);

                gen.setEnabled(true);

                weightEd.setFocusable(true);
                weightEd.setFocusableInTouchMode(true);
                weightEd.setClickable(true);

                drEd.setFocusable(true);
                drEd.setFocusableInTouchMode(true);
                drEd.setClickable(true);

                smkEd.setFocusable(true);
                smkEd.setFocusableInTouchMode(true);
                smkEd.setClickable(true);

                dtEd.setFocusable(true);
                dtEd.setFocusableInTouchMode(true);
                dtEd.setClickable(true);

                hptEd.setFocusable(true);
                hptEd.setFocusableInTouchMode(true);
                hptEd.setClickable(true);

                btn.setText("Share Now");

                return;
            }

            String name = nameEd.getText().toString().trim();
            String a = ageEd.getText().toString().trim();
            String gender = gen.getSelectedItem().toString();
            String w = weightEd.getText().toString();
            String dr = drEd.getText().toString();
            String sm = smkEd.getText().toString();
            String di = dtEd.getText().toString();
            String hy = hptEd.getText().toString();

            if(name.isEmpty()) {
                Toast.makeText(getContext(), "Name is empty", Toast.LENGTH_SHORT).show();
            } else if(a.isEmpty()) {
                Toast.makeText(getContext(), "Age is empty", Toast.LENGTH_SHORT).show();
            } else if(gender.equals("Select gender")) {
                Toast.makeText(getContext(), "Gender is not selected", Toast.LENGTH_SHORT).show();
            } else if(w.isEmpty()) {
                Toast.makeText(getContext(), "Weight is empty", Toast.LENGTH_SHORT).show();
            } else if(dr.isEmpty()) {
                Toast.makeText(getContext(), "Drinker is empty", Toast.LENGTH_SHORT).show();
            } else if(sm.isEmpty()) {
                Toast.makeText(getContext(), "Smoker is empty", Toast.LENGTH_SHORT).show();
            } else if(di.isEmpty()) {
                Toast.makeText(getContext(), "Diabetes is empty", Toast.LENGTH_SHORT).show();
            } else if(hy.isEmpty()) {
                Toast.makeText(getContext(), "Hypertension is empty", Toast.LENGTH_SHORT).show();
            } else {
                int age = Integer.parseInt(a);
                double weight = Double.parseDouble(w);

                db.clearData();
                db.insertData(name, age, gender, weight, dr, sm, di, hy);

                Toast.makeText(getContext(), "Started", Toast.LENGTH_SHORT).show();

                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan a QR code");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);

                qrScanLauncher.launch(integrator.createScanIntent());

            }
        });
        return view;
    }


}