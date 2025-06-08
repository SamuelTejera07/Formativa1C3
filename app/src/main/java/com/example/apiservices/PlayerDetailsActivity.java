package com.example.apiservices;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerDetailsActivity extends AppCompatActivity {
    private TextView tvName, tvPosition, tvBirth, tvNationality, tvNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);


        tvName = findViewById(R.id.tvPlayerDetailName);
        tvPosition = findViewById(R.id.tvPlayerDetailPosition);
        tvBirth = findViewById(R.id.tvPlayerDetailBirth);
        tvNationality = findViewById(R.id.tvPlayerDetailNationality);
        tvNumber = findViewById(R.id.tvPlayerDetailNumber);


        String name = getIntent().getStringExtra("player_name");
        String position = getIntent().getStringExtra("player_position");
        String birth = getIntent().getStringExtra("player_birth");
        String nationality = getIntent().getStringExtra("player_nationality");
        String number = getIntent().getStringExtra("player_number");


        tvName.setText("Nombre: " + name);
        tvPosition.setText("Posición: " + position);
        tvBirth.setText("Fecha de nacimiento: " + birth);
        tvNationality.setText("Nacionalidad: " + nationality);
        tvNumber.setText("Número: " + number);
    }
}
