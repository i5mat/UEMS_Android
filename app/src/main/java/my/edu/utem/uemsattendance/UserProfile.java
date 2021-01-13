package my.edu.utem.uemsattendance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    Button btnScan, btnLogout;
    TextView txt4, txtMatric, txtEmail, txtPoint, txtID;
    String getEventID, getUsrID;

    private static String JSON_URL_ATTENDANCE = "http://192.168.68.110:80/api/attendance/create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        btnScan = findViewById(R.id.btnScan);
        btnLogout = findViewById(R.id.btnLogout);
        txt4 = findViewById(R.id.studName);
        txtMatric = findViewById(R.id.studMatric);
        txtEmail = findViewById(R.id.studEmail);
        txtPoint = findViewById(R.id.studPoint);
        txtID = findViewById(R.id.studID);

        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        String name = sharedpreferences.getString("name", "NO NAME");
        String matric = sharedpreferences.getString("matric_no", "NO MATRIC NO.");
        int point = sharedpreferences.getInt("point", 0);
        String email = sharedpreferences.getString("email", "NO EMAIL");
        int id = sharedpreferences.getInt("user_id", 0);

        txt4.setText(name);
        txtMatric.setText(matric);
        txtPoint.setText(Integer.toString(point));
        txtEmail.setText(email);
        txtID.setText(Integer.toString(id));

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    IntentIntegrator intentIntegrator = new IntentIntegrator(UserProfile.this);
                    intentIntegrator.setPrompt("For flash use volume up key");
                    intentIntegrator.setBeepEnabled(true);
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.setCaptureActivity(Capture.class);
                    intentIntegrator.initiateScan();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this, "You have been logged out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
//                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity., Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.clear();
//                editor.commit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
//            builder.setTitle("Attendance recorded!");
//
//            //Calendar calendar = Calendar.getInstance();
//            //String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//            Date currentTime = Calendar.getInstance().getTime();

            //data for QR Code
            getEventID = intentResult.getContents();
            //builder.setMessage(getEventID+" at "+currentTime);
            register();
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.show();
        }else {
            Toast.makeText(getApplicationContext(), "Opps..you did not scan anything", Toast.LENGTH_SHORT).show();
        }
    }

    public void goEventList(View view) {
        Intent intent = new Intent(UserProfile.this, ShowEvents.class);
        startActivity(intent);
        finish();
    }

    private void register() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);

        StringRequest request = new StringRequest(Request.Method.POST, JSON_URL_ATTENDANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replaceAll("^\"|\"$", "");
                if (response.equals("Attendance recorded, thank you and have a nice day.")) {
                    builder.setTitle("SUCCESS");
                    builder.setMessage(response);
                } else if (response.equals("Attendance not recorded. Cannot record twice, only 1 attendance.")) {
                    builder.setTitle("FAILED");
                    builder.setMessage(response);
                }

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", String.valueOf(txtID.getText()));
                param.put("event_id", getEventID);
                return param;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}