package com.example.admin.hamovil;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class Escan extends AppCompatActivity {

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

        //setContentView(R.layout.activity_escan);

        //getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.fondoVerde));

        // Se carga el usuario variable global del sistema

        Intent intent6 = getIntent();
        Bundle extras6 = intent6.getExtras();
        if (extras6 != null) {
            this.uid = (int) extras6.get("uid");
        }

        /*salir = (ImageButton)findViewById(R.id.btnSalir);

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        */

        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);

        }
    }

    /*@Override
    public void onBackPressed() {
        //Toast.makeText(Escan.this, "presiono back", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Escan.this, MenuPrincipal.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
        finish();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                final String contents = data.getStringExtra("SCAN_RESULT");
                //Toast.makeText(this, "Resultado del Scan: "+contents, Toast.LENGTH_SHORT).show();
                System.out.print(contents);
                final String DATOS_URL = global.direccion+"/servicioha/sesion_guardias.php?name_function=id_por_ha&name="+contents+"&jefe_id="+uid;
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, DATOS_URL, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                if (response.get("usuario_id").toString().equalsIgnoreCase("-1")) {
                                    Toast.makeText(Escan.this, "Usted no tiene permisos para evaluar esa hoja o la esta evaluando un dia incorrecto", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Escan.this, MenuPrincipal.class);
                                    intent.putExtra("uid",uid);
                                    startActivity(intent);
                                }
                                else {
                                    if (response.get("usuario_id").toString() != null && response.get("usuario_id").toString() != "null") {
                                        Intent intent = new Intent(Escan.this, Evaluacion.class);
                                        intent.putExtra("usuario_id",Integer.parseInt(response.get("usuario_id").toString()));
                                        intent.putExtra("scaned_ha",contents); // Este es el Id de la HA a evaluar
                                        intent.putExtra("uid",uid);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(Escan.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Escan.this, PersonalCargoList.class);
                                        intent.putExtra("uid",uid);
                                        startActivity(intent);
                                    }

                                }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                //Toast.makeText(Escan.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(Escan.this);
                requestQueue.add(jsObjRequest);
            }
            if(resultCode == RESULT_CANCELED){
                Intent intent = new Intent(Escan.this, PersonalCargoList.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(this, "Error al escanear la Hoja de Actividad", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Escan.this, PersonalCargoList.class);
            intent.putExtra("uid",uid);
            startActivity(intent);
        }
    }
}
