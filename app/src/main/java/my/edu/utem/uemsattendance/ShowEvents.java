package my.edu.utem.uemsattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowEvents extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Event> events;
    private static String JSON_URL = "http://192.168.68.110:80/api/events";
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        recyclerView = findViewById(R.id.eventsList);
        events = new ArrayList<>();

        extractEvents();
    }

    private void extractEvents() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject eventObject = response.getJSONObject(i);

                        Event event = new Event();
                        event.setName(eventObject.getString("name").toString());
                        event.setDescription(eventObject.getString("description").toString());
                        event.setVenue(eventObject.getString("venue").toString());
                        event.setCapacity(eventObject.getInt("capacity"));
                        event.setStart_event(eventObject.getString("start").toString());
                        event.setEnd_event(eventObject.getString("end").toString());

                        events.add(event);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new Adapter(getApplicationContext(), events);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void returnBack(View view) {
        Intent intent = new Intent(ShowEvents.this, UserProfile.class);
        startActivity(intent);
        finish();
    }
}