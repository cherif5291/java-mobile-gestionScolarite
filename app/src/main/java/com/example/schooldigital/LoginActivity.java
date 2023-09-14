package com.example.schooldigital;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    TextView text;
    EditText matricule, password;
    Button retour;
    Button login;

    // Remplacez "VOTRE_URL_API" par l'URL réelle de votre API
    private String apiUrl = "https://authmobile.000webhostapp.com/index.php";
    private ProgressBar progressBar;
    private void saveUserCredentials(String matricule, String password) {
        // Obtenez les SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Éditez les SharedPreferences avec un éditeur
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Enregistrez les informations dans les SharedPreferences
        editor.putString("matricule", matricule);
        editor.putString("password", password);
        // Appliquez les modifications
        editor.apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progressBar);
        text = findViewById(R.id.txtLabel);
        retour = findViewById(R.id.retourButton);
        matricule = findViewById(R.id.edtMatricule);
        password = findViewById(R.id.edtPassword);
        login = findViewById(R.id.loginButton);

        text.setText("Espace "+getIntent().getExtras().getString("profilName"));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mat = matricule.getText().toString();
                String pwd = password.getText().toString();

                // Vérifier si les champs sont vides
                if (mat.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    connect(mat, pwd);
                }
            }
            private void showLoading(boolean show) {
                if (show) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            private void connect(String mat, String pwd) {
                // Afficher le ProgressBar avant de faire la requête
                showLoading(true);
                // Créez l'URL complète avec les paramètres matricule et password
                String fullUrl = apiUrl + "?matricule=" + mat + "&password=" + pwd;

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Masquer le ProgressBar après avoir reçu la réponse
                                showLoading(false);
                                try {
                                    // Analysez la réponse JSON pour obtenir les données pertinentes
                                    boolean statut = response.getBoolean("statut");
                                    String message = response.getString("message");

                                    if (statut) {


                                        // Connexion réussie,
                                        String matricule = response.getString("matricule");
                                        String prenom = response.getString("prenom");
                                        String nom = response.getString("nom");
                                        // Extraites d'autres informations sur l'utilisateur si nécessaire
// Authentification réussie, enregistrez les informations dans les SharedPreferences
                                        saveUserCredentials(mat, pwd);
                                        // Redirigez vers HomeActivity et transmettez les données pertinentes
                                        Intent intent = new Intent(LoginActivity.this, AccueilActivity.class);
                                        intent.putExtra("matricule", matricule);
                                        intent.putExtra("prenom", prenom);
                                        intent.putExtra("nom", nom);
                                        // Ajoutez d'autres données à l'intent si nécessaire
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Authentification échouée, affichez le message d'erreur du serveur dans une boîte de dialogue
                                        showErrorDialog(message);                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                showLoading(false);
                                // Gérez ici les erreurs Volley (par exemple, les problèmes de réseau)
                                error.printStackTrace();
                                // Afficher un message d'erreur pour les erreurs Volley
                                Toast.makeText(LoginActivity.this, "Erreur Volley: Vérifiez votre connexion internet", Toast.LENGTH_SHORT).show();
                            }
                        });

                // Ajoutez la requête à la RequestQueue.
                Volley.newRequestQueue(getApplicationContext()).add(request);
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void showErrorDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //setting alert dialog title
        alertDialogBuilder.setTitle("Erreur d'authentification");

        //setting alert dialog message
        alertDialogBuilder.setMessage(message);

        //permettre de ne pas enlever la boite de dialogue
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // You can perform any action here after the user clicks the "OK" button.
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
