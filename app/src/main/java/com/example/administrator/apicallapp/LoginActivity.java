package com.example.administrator.apicallapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText usernameED, passwordED;
    Button loginBtn;

    private JSONObject jsonObject;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);

        usernameED = (EditText)findViewById(R.id.usernameED);
        passwordED = (EditText)findViewById(R.id.passwordED);
        loginBtn = (Button)findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String unm = usernameED.getText().toString();
                String pwd = passwordED.getText().toString();

                jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserName", usernameED.getText().toString().trim());
                    jsonObject.put("Password", passwordED.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new AsyncTaskJson().execute(unm,pwd);
            }
        });
    }

    public class AsyncTaskJson extends AsyncTask<String,String,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {

                HttpPost httppost = new HttpPost("http://192.168.71.109/VanHireQAAPI/validate-app-login");
                httppost.setHeader("Content-type","application/json");
                httppost.setHeader("X-Auth-Token","v#1nH!%r18_8$ky399P1@3h-iR2vn");

                httppost.setEntity(new StringEntity(jsonObject.toString()));
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject dataObj = jsonObject.getJSONObject("responseData");


                    String namedisplay = dataObj.getJSONObject("data").getString("displayName");
                    message = dataObj.getJSONObject("data").getString("message");


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        }
                    });



                    return true;
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return false;
        }
    }
}
