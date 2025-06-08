package com.example.apiservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListadoTeamsCompetencia extends AppCompatActivity {
   ListView listado;
   String url;
    ArrayList<Teams> te = new ArrayList<Teams>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_teams_competencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent i = getIntent();
        String code = i.getStringExtra("code");
         url = "https://api.football-data.org/v4/competitions/"+code+"/teams";
        requestDatos();
     //   Toast.makeText(this,"url =" + url, Toast.LENGTH_LONG).show();
    }

    public void requestDatos(){
        RequestQueue cola = Volley.newRequestQueue(this);

        JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parserJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error en la conexion "+ error.getMessage(), Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-Auth-Token", "67c6af80aa6b4c218804d4f8ffa7ee2d");
                return headers;
            }
        };
        cola.add(peticion);
    }

    public void parserJson(JSONObject response){
        try {
            String cadena = "";
            JSONArray teams = response.getJSONArray("teams");
      //      Toast.makeText(this,"Cantidad de equipos " + teams.length(),Toast.LENGTH_LONG).show();
            for (int i = 0 ; i<teams.length(); i++) {
                JSONObject com = teams.getJSONObject(i);
                String id = com.getString("id");
                String nombre= com.getString("name");
                String web = com.getString("website");
                String fundado = com.getString("founded");
                cadena = cadena + id + "," + nombre+ "," + web + ","+ fundado + "\n";
                Teams t = new Teams(id,nombre,web,fundado);
               te.add(t);
            }
            //Toast.makeText(getApplicationContext(),"Id = "+ cs.get(1).getId(), Toast.LENGTH_LONG).show();
          //  json.setText(cadena);
          listado = findViewById(R.id.lstteams);
            if (te!=null && te.size()>0){
                TeamsAdapter adapter = new TeamsAdapter(this, te);
                listado.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                // Agregar click listener para los equipos
                listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Teams team = te.get(position);
                        Intent intent = new Intent(ListadoTeamsCompetencia.this, TeamDetailsActivity.class);
                        intent.putExtra("team_id", team.getId());
                        startActivity(intent);
                    }
                });
            }
            else{
                Toast.makeText(this,"No hay datos" , Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}