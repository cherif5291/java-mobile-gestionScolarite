package com.example.schooldigital;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        // Récupérer les données de l'intent
        Intent intent = getIntent();
        if (intent != null) {
            String matricule = intent.getStringExtra("matricule");
            String prenom = intent.getStringExtra("prenom");
            String nom = intent.getStringExtra("nom");

            // Mettre à jour les TextView pour afficher les informations de l'étudiant
            TextView matriculeTextView = findViewById(R.id.matriculeTextView);
            TextView prenomTextView = findViewById(R.id.prenomTextView);
            TextView nomTextView = findViewById(R.id.nomTextView);

            matriculeTextView.setText("Matricule : " + matricule);
            prenomTextView.setText("Prénom : " + prenom);
            nomTextView.setText("Nom : " + nom);
        }
    }
}
