package com.example.apiservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class TeamDetailsActivity extends AppCompatActivity {
    private TextView tvCoachName, tvCoachNationality, tvCoachBirth;
    private ListView lvPlayers;
    private ArrayList<Player> playersList;
    private PlayerAdapter playerAdapter;
    private RequestQueue requestQueue;
    private static final String API_KEY = "67c6af80aa6b4c218804d4f8ffa7ee2d";
    private static final String BASE_URL = "https://api.football-data.org/v4/teams/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);


        tvCoachName = findViewById(R.id.tvCoachName);
        tvCoachNationality = findViewById(R.id.tvCoachNationality);
        tvCoachBirth = findViewById(R.id.tvCoachBirth);
        lvPlayers = findViewById(R.id.lvPlayers);


        playersList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(this, playersList);
        lvPlayers.setAdapter(playerAdapter);


        String teamId = getIntent().getStringExtra("team_id");


        requestQueue = Volley.newRequestQueue(this);


        loadTeamDetails(teamId);


        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = playersList.get(position);
                Intent intent = new Intent(TeamDetailsActivity.this, PlayerDetailsActivity.class);
                intent.putExtra("player_id", player.getId());
                intent.putExtra("player_name", player.getName());
                intent.putExtra("player_position", player.getPosition());
                intent.putExtra("player_birth", player.getDateOfBirth());
                intent.putExtra("player_nationality", player.getNationality());
                intent.putExtra("player_number", player.getShirtNumber());
                startActivity(intent);
            }
        });
    }

    private void showErrorDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
               .setMessage(message)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadTeamDetails(String teamId) {
        String url = BASE_URL + teamId;
        android.util.Log.d("TeamDetails", "Cargando equipo con URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            android.util.Log.d("TeamDetails", "Respuesta recibida: " + response.toString());


                            JSONObject coachObj = response.getJSONObject("coach");
                            if (coachObj != null) {
                                String name = coachObj.optString("name", "No disponible");
                                String nationality = coachObj.optString("nationality", "No disponible");
                                String birthDate = coachObj.optString("dateOfBirth", "No disponible");

                                tvCoachName.setText("Entrenador: " + name);
                                tvCoachNationality.setText("Nacionalidad: " + nationality);
                                tvCoachBirth.setText("Fecha de nacimiento: " + birthDate);
                            } else {
                                tvCoachName.setText("Entrenador: No disponible");
                                tvCoachNationality.setText("Nacionalidad: No disponible");
                                tvCoachBirth.setText("Fecha de nacimiento: No disponible");
                            }


                            JSONArray squadArray = response.getJSONArray("squad");
                            playersList.clear();

                            for (int i = 0; i < squadArray.length(); i++) {
                                JSONObject playerObj = squadArray.getJSONObject(i);
                                Player player = new Player(
                                        playerObj.optString("id", ""),
                                        playerObj.optString("name", "No disponible"),
                                        playerObj.optString("position", "No disponible"),
                                        playerObj.optString("dateOfBirth", "No disponible"),
                                        playerObj.optString("nationality", "No disponible"),
                                        playerObj.optString("shirtNumber", "N/A")
                                );
                                playersList.add(player);
                            }

                            playerAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            android.util.Log.e("TeamDetails", "Error al procesar JSON: " + e.getMessage());
                            e.printStackTrace();
                            showErrorDialog("Error de Datos",
                                "Error al procesar los datos: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error al cargar los datos del equipo:\n\n";
                        if (error.networkResponse != null) {
                            errorMessage += "Código de error: " + error.networkResponse.statusCode;
                            if (error.networkResponse.statusCode == 403) {
                                errorMessage += "\n\nPosible problema con la API key. Verifica que la key sea válida.";
                            } else if (error.networkResponse.statusCode == 404) {
                                errorMessage += "\n\nEquipo no encontrado. Verifica el ID del equipo.";
                            }


                            if (error.networkResponse.data != null) {
                                try {
                                    String responseBody = new String(error.networkResponse.data);
                                    errorMessage += "\n\nRespuesta del servidor: " + responseBody;
                                } catch (Exception e) {
                                    errorMessage += "\n\nNo se pudo leer la respuesta del servidor";
                                }
                            }
                        } else if (!isNetworkAvailable()) {
                            errorMessage += "Verifica tu conexión a internet";
                        } else {
                            errorMessage += "Error: " + error.toString();
                        }

                        android.util.Log.e("TeamDetails", "Error completo: " + errorMessage);
                        showErrorDialog("Error de Conexión", errorMessage);
                    }
                }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("X-Auth-Token", API_KEY);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private boolean isNetworkAvailable() {
        android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
