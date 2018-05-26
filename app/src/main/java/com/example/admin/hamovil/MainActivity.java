package com.example.admin.hamovil;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnIngresar;
    EditText txtUsername;
    EditText txtPassword;
    ProgressBar esperar;
    //public EditText entradaUsuario;
    //public EditText entradaContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        //this.setContentView(R.layout.your_layout_name_here);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.fondoVerde));

        txtUsername = (EditText)findViewById(R.id.editTextUsername);
        txtPassword = (EditText)findViewById(R.id.editTextPassword);
        btnIngresar = (Button)findViewById(R.id.buttonLogin);
        esperar = (ProgressBar) findViewById(R.id.progressBar1);
        EditText entradaUsuario = (EditText)findViewById(R.id.editTextUsername);
        EditText entradaContrasenia = (EditText)findViewById(R.id.editTextPassword);

        esperar.setVisibility(View.GONE);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtUsername.getText().toString().equals(""))
                    iniciarSesion(txtUsername.getText().toString(),txtPassword.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() { } // -*- No hago nada

    public void iniciarSesion(String pusername, String ppass) {

        esperar.setVisibility(View.VISIBLE);

        final String LOGIN_URL = global.direccion+"/servicioha/sesion_guardias.php?name_function=logear_usuario&login="+pusername+"&password="+ppass;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, LOGIN_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("resultado").equalsIgnoreCase("0")){
                                esperar.setVisibility(View.GONE);
                                txtUsername.setText("");
                                txtPassword.setText("");
                                Intent i =new Intent (MainActivity.this, MenuPrincipal.class);
                                i.putExtra("uid",response.getString("uid").toString());
                                startActivity(i);
                            }
                            else {
                                esperar.setVisibility(View.GONE);
                                txtPassword.setText("");
                                Toast.makeText(MainActivity.this, "Error usuario y/o contrasena incorrecta", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.toString());
                        //iniciarSesion(txtUsername.getText().toString(),txtPassword.getText().toString());
                    }
                });


        jsObjRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 100000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 100000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                iniciarSesion(txtUsername.getText().toString(),txtPassword.getText().toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }
}
