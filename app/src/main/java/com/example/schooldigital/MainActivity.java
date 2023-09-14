package com.example.schooldigital;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //appel de l'interface grafique dans l'activité
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        //appel de la methode de la verification de connexion
        checkInternet();
    }

    private void checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            // Connected to the internet, delay and open the new activity
            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Intent i = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        } else {
            showMessage();
        }
    }

    private void showMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //setting alert dialog title
        alertDialogBuilder.setTitle("Problème de connexion internet");

        //Icon of alert dialog
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_wifi_off_24);

        //setting alert dialog message
        alertDialogBuilder.setMessage("Vous n'êtes pas connecté à internet");

        //permettre de ne pas enlever la boite de dialogue
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("Réessayer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkInternet();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
