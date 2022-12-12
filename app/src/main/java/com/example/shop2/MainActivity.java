package com.example.shop2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firestore;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == btnAdd) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_toy, null, false);
            Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);
            EditText etPrice = dialogView.findViewById(R.id.etPrice);
            EditText etToyName = dialogView.findViewById(R.id.etToyName);


            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = etToyName.getText().toString();
                    Double price = Double.parseDouble(etPrice.getText().toString());

                    if(name.isEmpty() || price == null)
                    {
                        Toast.makeText(MainActivity.this, "Please type both Name and Price", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toy toy = new Toy(name, price);
                        firestore
                                .collection("toys")
                                .document(System.currentTimeMillis() + "")
                                .set(toy)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(MainActivity.this, "Toy added!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });

            builder.setView(dialogView);
            builder.create().show();
        }
    }
}