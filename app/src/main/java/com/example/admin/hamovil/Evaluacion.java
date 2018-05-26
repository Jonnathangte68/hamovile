package com.example.admin.hamovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class Evaluacion extends AppCompatActivity {

    private ListView lv;
    Button btnEvaluar;
    ImageButton salir;
    TextView txtNombre;
    TextView txtSemana;
    String ha_id = new String();
    public int usuario_id = -1;
    public String scaned_ha = new String();

    // Global del sistema
    public int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_evaluacion);

        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.fondoVerde));

        // Se carga el usuario variable global del sistema

        Intent intent6 = getIntent();
        Bundle extras6 = intent6.getExtras();
        if (extras6 != null) {
            this.uid = (int) extras6.get("uid");
            this.usuario_id = (int) extras6.get("usuario_id");
            this.scaned_ha = extras6.get("scaned_ha").toString();

        }
        // Si tiene HA de esta semana se ejecuta todo lo que este debajo sino mostrar mensaje no ha generado la HA
        /*
        * if (HA == null) {
        *   Toast.makeText(Evaluacion.this, "No se ha generado la HA de esta semana", Toast.LENGTH_SHORT).show();
        * }
        *
        * */

        lv = (ListView)findViewById(R.id.listViewOpciones);
        btnEvaluar = (Button)findViewById(R.id.btnEvaluate);
        txtNombre = (TextView)findViewById(R.id.txtNombre);
        txtSemana = (TextView)findViewById(R.id.txtSemana);
        salir = (ImageButton)findViewById(R.id.btnSalir);

        List<String> listaNombres = new ArrayList<String>();
        listaNombres.add("¿La HA se encuentra limpia y ordenada?");
        listaNombres.add("¿La Ha se encuentra llena de acuerdo al método");
        listaNombres.add("¿Los tiempos dedicados concuerdan con los resultados presentados?");
        listaNombres.add("¿Validar si existen actividades claves sin revisar y cuestionar el porque?");
        listaNombres.add("¿Validar si existen compromisos para las actividades no realizadas? ");

        /*Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.usuario_id = (int) extras.get("usuario_id");
            this.scaned_ha = extras.get("scaned_ha").toString();
        }*/

        //Toast.makeText(this, "EL id del usuario es"+this.usuario_id, Toast.LENGTH_SHORT).show();

        final CustomAdapter customAdapter = new CustomAdapter(Evaluacion.this, listaNombres);
        if (lv.getAdapter() == null) { //Adapter not set yet.
            lv.setAdapter(customAdapter);
        } else { //Already has an adapter
            lv.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
            lv.invalidateViews();
            lv.refreshDrawableState();
        }

        btnEvaluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Evaluacion.this, "Button pressed", Toast.LENGTH_SHORT).show();
                int ponderacion1 = 4;
                int ponderacion2 = 6;
                int ponderacion3 = 1;
                int ponderacion4 = 9;
                int ponderacion5 = 3;
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean check4 = false;
                boolean check5 = false;
                float sumaPonderada = 0;
                int seleccionados = 0;
                for ( int i = 0 ; i < lv.getCount() ; i++){
                    View v = getViewByPosition(i,lv);
                    CheckBox cb = (CheckBox) v.findViewById(R.id.checq);
                    if (cb.isChecked()) {
                        if (i == 0){
                            sumaPonderada += ponderacion1;
                            check1 = true;
                        }else if(i == 1) {
                            sumaPonderada += ponderacion2;
                            check2 = true;
                        }else if(i == 2) {
                            sumaPonderada += ponderacion3;
                            check3 = true;
                        }else if(i == 3) {
                            sumaPonderada += ponderacion4;
                            check4 = true;
                        }else {
                            sumaPonderada += ponderacion5;
                            check5 = true;
                        }
                    }
                }

                /* Se almacena la calificacion de la HA */

                final String EVALUAR_URL = global.direccion+"/servicioha/sesion_guardias.php?name_function=guardar_evaluacion&ha_id="+scaned_ha+"&calificacion="+dC(sumaPonderada)+"&criterio1="+check1+"&criterio2="+check2+"&criterio3="+check3+"&criterio4="+check4+"&criterio5="+check5;

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, EVALUAR_URL, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.get("resultado").toString().equalsIgnoreCase("1")){}else {
                                        Toast.makeText(Evaluacion.this, "No se ha podido guardar ya ha sido evaluada", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Ya se calculo el Resultado
                                Toast.makeText(Evaluacion.this, "Evaluacion asignada", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(Evaluacion.this, MenuPrincipal.class);
                                intent.putExtra("uid",uid);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Toast.makeText(Evaluacion.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(Evaluacion.this);
                requestQueue.add(jsObjRequest);

                // Envio al menu principal - Solo envio ningun extra
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        cargarInformacionDeUsuario();
    }

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public String dC (float c) {
        String calif = "";
        if(c > 22) {
            calif = "A";
        }else if (c > 10 && c <= 22) {
            calif = "B";
        }
        else {
            calif = "C";
        }
        return calif;
    }

    private void cargarInformacionDeUsuario() {
        String DATOS_URL = global.direccion+"/servicioha/sesion_guardias.php?name_function=get_datos_ha&usuario_id="+this.usuario_id + "&ha_id=" + scaned_ha;
        System.out.print(DATOS_URL);
        //final String DATOS_URL = global.direccion+"/servicioha/sesion_guardias.php?name_function=get_datos_ha&usuario_id="+this.usuario_id + "&ha_id=" + scaned_ha;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, DATOS_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (!response.equals("false")) {
                            try {
                                txtNombre.setText(response.get("login").toString());
                                if (response.get("semana").equals(null)){
                                    txtSemana.setText("N/A");
                                }
                                else {
                                    txtSemana.setText(response.get("semana").toString());
                                }
                                if (scaned_ha.equalsIgnoreCase("NaN")) {
                                    ha_id = response.get("name").toString();
                                }
                                else {
                                    ha_id = scaned_ha;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Intent intent = new Intent(Evaluacion.this, MenuPrincipal.class);
                            intent.putExtra("uid",uid);
                            Toast.makeText(Evaluacion.this, "El usuario no tiene ninguna HA generada por el sistema", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(Evaluacion.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Evaluacion.this, MenuPrincipal.class);
                        intent.putExtra("uid",uid);
                        Toast.makeText(Evaluacion.this, "El usuario no tiene ninguna HA generada por el sistema", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(Evaluacion.this);
        requestQueue.add(jsObjRequest);
    }

}
