package my.edu.utem.uemsattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemption;
    private ProgressDialog dialog;
    public static final String MyPREFERENCES = "MyPrefs" ;

    Credentials credentials = new Credentials("B031920042","12345678");

    boolean isValid = false;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.txtName);
        ePassword = findViewById(R.id.txtPassword);
        eLogin = findViewById(R.id.btnLogin);
        eAttemption = findViewById(R.id.attemption);

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = eName.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if(inputName.isEmpty() || inputPassword.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter all the details correctly!", Toast.LENGTH_SHORT).show();

                }else{
                    login();
                }
            }
        });

    }

    private boolean validate(String name, String password){
        if(name.equals(credentials.getUsername()) && password.equals(credentials.getPassword())){
            return true;
        }
        return false;
    }

    private void login() {
//        dialog.setMessage("Logging In");
//        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    JSONObject user = object.getJSONObject("user");
                    SharedPreferences userPref = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token", object.getString("token"));
                    editor.putString("name", user.getString("name"));
                    editor.putString("matric_no", user.getString("matric_no"));
                    editor.putString("email", user.getString("email"));
                    editor.putInt("point", user.getInt("point"));
                    editor.putInt("user_id", user.getInt("id"));
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    startActivity(new Intent((MainActivity.this), UserProfile.class));
                    finish();
                    Toast.makeText(MainActivity.this, "Login Success " + user.getString("name"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.this, "Login Failed...", Toast.LENGTH_SHORT).show();
            }
            //dialog.dismiss();

        }, error -> {
            error.printStackTrace();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("email", eName.getText().toString().trim());
                map.put("password", ePassword.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }
}