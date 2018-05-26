package com.example.admin.hamovil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class MenuPrincipal extends AppCompatActivity {

    LinearLayout optionOne;
    LinearLayout optionTwo;
    ImageButton salir;

    // Global del sistema
    public int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_principal);

        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.fondoVerde));
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        // Se carga el usuario variable global del sistema

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.uid = Integer.valueOf(extras.getString("uid"));
        }

        optionOne = (LinearLayout)findViewById(R.id.opcionuno);
        optionTwo = (LinearLayout)findViewById(R.id.opciondos);
        salir = (ImageButton)findViewById(R.id.btnSalir);

        optionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Opcion 1", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuPrincipal.this, Escan.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        optionTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Opcion 2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuPrincipal.this, PersonalCargoList.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_SINGLE_TOP);
                //intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

    }

}
