package com.example.schooldigital;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Récupérez les informations stockées dans les SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String matricule = sharedPreferences.getString("matricule", "");
        String prenom = sharedPreferences.getString("prenom", "");
        String nom = sharedPreferences.getString("nom", "");

        // Affichez les informations de l'utilisateur dans la vue appropriée (par exemple, des TextViews)
        TextView matriculeTextView = findViewById(R.id.matriculeTextView);
        TextView prenomTextView = findViewById(R.id.prenomTextView);
        TextView nomTextView = findViewById(R.id.nomTextView);

        matriculeTextView.setText("Matricule : " + matricule);
        prenomTextView.setText("Prénom : " + prenom);
        nomTextView.setText("Nom : " + nom);

        // Ajoutez le bouton de déconnexion et définissez son onClickListener
        Button deconnexionButton = findViewById(R.id.deconnexionButton);
        deconnexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deconnexion();
            }
        });
    }

    public void onScanQrButtonClicked(View view) {
        // Utilisez la classe IntentIntegrator pour démarrer le scan de code QR
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false); // Débloquer l'orientation de la caméra
        integrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String qrContent = result.getContents();
            // Supposons que le matricule est stocké dans qrContent

            // Connectez-vous à l'API en utilisant le matricule scanné
            connectWithQrCode(qrContent);
        }
    }

    private void connectWithQrCode(String qrContent) {
        // Afficher le ProgressBar avant de faire la requête
        // Créez l'URL complète avec le paramètre matricule
        String fullUrl = "https://authmobile.000webhostapp.com/showinfo.php" + "?matricule=" + qrContent;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Masquer le ProgressBar après avoir reçu la réponse
                        try {
                            // Analysez la réponse JSON pour obtenir les données pertinentes
                            boolean statut = response.getBoolean("statut");

                            if (statut) {
                                // Connexion réussie,
                                String matricule = response.getString("matricule");
                                String prenom = response.getString("prenom");
                                String nom = response.getString("nom");

                                // Lancer l'activité InformationActivity et passer les informations de l'étudiant via un Intent
                                Intent intent = new Intent(AccueilActivity.this, InformationActivity.class);
                                intent.putExtra("matricule", matricule);
                                intent.putExtra("prenom", prenom);
                                intent.putExtra("nom", nom);
                                startActivity(intent);
                            } else {
                                // Le matricule n'a pas été trouvé dans la base de données,
                                // vous pouvez gérer cela ici si nécessaire
                                Toast.makeText(AccueilActivity.this, "Étudiant non trouvé dans la base de données", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérez ici les erreurs Volley (par exemple, les problèmes de réseau)
                        error.printStackTrace();
                        // Afficher un message d'erreur pour les erreurs Volley
                        Toast.makeText(AccueilActivity.this, "Erreur Volley: Vérifiez votre connexion internet", Toast.LENGTH_SHORT).show();
                    }
                });

        // Ajoutez la requête à la RequestQueue.
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }


    private void deconnexion() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Éditez les SharedPreferences avec un éditeur
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Effacez les informations stockées (matricule et password)
        editor.remove("matricule");
        editor.remove("password");
        // Appliquez les modifications
        editor.apply();

        // Redirigez vers LoginActivity pour que l'utilisateur se connecte à nouveau
        Intent intent = new Intent(AccueilActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}