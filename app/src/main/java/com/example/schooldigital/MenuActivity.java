package com.example.schooldigital;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

    Button etudiant, administrateur, comptable, vigile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //liaison des variables aux vues par les id
        initUI();

        etudiant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permet d'appeller une nouvelle activité
                Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                i.putExtra("profilName","Etudiant");
                startActivity(i);
                finish();
            }
        });

        vigile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                i.putExtra("profilName","Vigile");
                startActivity(i);
                finish();
            }
        });

        comptable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                i.putExtra("profilName","Comptable");
                startActivity(i);
                finish();
            }
        });

        administrateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                i.putExtra("profilName","Administrateur");
                startActivity(i);
                finish();
            }
        });

    }

    private void initUI() {
        etudiant = findViewById(R.id.btnEdutiant);
        vigile = findViewById(R.id.btnVigile);
        comptable = findViewById(R.id.btnComptable);
        administrateur = findViewById(R.id.btnAdministrateur);

    }
}