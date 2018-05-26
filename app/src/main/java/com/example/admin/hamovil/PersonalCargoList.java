package com.example.admin.hamovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class PersonalCargoList extends AppCompatActivity {


    ListView lv;
    EditText busqueda;
    ImageButton salir;
    ArrayList<String> empleados = new ArrayList<String>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ArrayList<String> HaIds = new ArrayList<String>();
    public int auxId = 0;

    // Global del sistema
    public int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent6 = getIntent();
        Bundle extras6 = intent6.getExtras();
        if (extras6 != null) {
            this.uid = (int) extras6.get("uid");
        }
        setContentView(R.layout.activity_personal_cargo_list);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.fondoVerde));
        lv = (ListView)findViewById(R.id.list_empleados);
        busqueda = (EditText) findViewById(R.id.busqueda);
        salir = (ImageButton)findViewById(R.id.btnSalir);
        // De inicio mostrar todos los empleado
        // Cuando se busque en el edit un empleado


        // Se carga el usuario variable global del sistema


        cargarLista();

        // Listeners
        busqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if(s.length() != 0) {
                    ArrayList<String> lempleados = new ArrayList<String>();
                    ArrayList<Integer> lids = new ArrayList<Integer>();
                    lv.setAdapter(null);
                    lempleados.clear();
                    lids.clear();
                    for (int k = 0 ; k < empleados.size() ; k++){
                        System.out.print(empleados.get(k).substring(0,s.length()));
                        System.out.print(s.toString());
                        //Toast.makeText(PersonalCargoList.this, "arreglo: "+empleados.get(k).substring(0,s.length())+"  -  "+s.toString(), Toast.LENGTH_SHORT).show();
                        if (empleados.get(k).substring(0,s.length()).equalsIgnoreCase(s.toString())){
                            lempleados.add(empleados.get(k));
                            lids.add(ids.get(k));
                        }
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            PersonalCargoList.this,
                            android.R.layout.simple_list_item_1,
                            lempleados );
                    lv.setAdapter(arrayAdapter);
                }
                else {
                    cargarLista();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (auxId == 0) {
                    if (!lv.getAdapter().getItem(i).toString().equalsIgnoreCase("Sin resultados...")) {
                        Intent intent = new Intent(PersonalCargoList.this, Evaluacion.class);
                        intent.putExtra("usuario_id", ids.get((int)lv.getAdapter().getItemId(i)));
                        intent.putExtra("scaned_ha", HaIds.get((int)lv.getAdapter().getItemId(i))); // Esto significa que no se escaneo por QR
                        intent.putExtra("uid",uid);
                        System.out.print("acaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+ids.get((int)lv.getAdapter().getItemId(i)));
                        startActivity(intent);
                        finish();
                    }
                }else {
                    if (!lv.getAdapter().getItem(i).toString().equalsIgnoreCase("Sin resultados...")) {
                        Intent intent = new Intent(PersonalCargoList.this, Evaluacion.class);
                        intent.putExtra("usuario_id", lv.getAdapter().getItemId(i));
                        intent.putExtra("scaned_ha", HaIds.get((int)lv.getAdapter().getItemId(i))); // No se escaneo por QR
                        intent.putExtra("uid",uid);
                        System.out.print("acaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+ids.get((int)lv.getAdapter().getItemId(i)));
                        startActivity(intent);
                        finish();
                    }
                }
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
        //loadDefaultValues();
    }

    public void cargarLista() {

        final String LISTA_URL = global.direccion+"/servicioha/sesion_guardias.php?name_function=listado_usuario&usuario_id="+String.valueOf(uid);

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, LISTA_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            empleados.clear();
                            ids.clear();
                            HaIds.clear();
                            if (response.length() > 0) {
                                for (int i = 0 ; i < response.length() ; i++) {
                                    JSONObject obj = (JSONObject) response.get(i);
                                    System.out.print(response.get(i));
                                    ids.add(Integer.parseInt(obj.getString("id")));
                                    System.out.print(obj.getString("id"));
                                    empleados.add(obj.getString("login"));
                                    System.out.print(obj.getString("login"));
                                    HaIds.add(obj.getString("id_ha"));
                                    System.out.print(obj.getString("id_ha"));
                                }
                                try {
                                    loadDefaultValues();
                                }
                                catch (Exception e){
                                    System.out.print(e);
                                }
                                loadDefaultValues();
                            } else {
                                empleados.add("Sin resultados...");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(PersonalCargoList.this, "No hay resultados", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(PersonalCargoList.this);
        requestQueue.add(jsObjRequest);
    }

    public void loadDefaultValues() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                PersonalCargoList.this,
                android.R.layout.simple_list_item_1,
                empleados );
        lv.setAdapter(arrayAdapter);
    }
}
