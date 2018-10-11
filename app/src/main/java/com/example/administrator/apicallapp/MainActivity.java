package com.example.administrator.apicallapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button clickBtn,postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickBtn = (Button)findViewById(R.id.clickBtn);
        postBtn = (Button)findViewById(R.id.postBtn);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ExecuteTask().execute();
            }
        });
    }

    public class ExecuteTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... urls) {
            try {

                HttpGet httpget = new HttpGet("http://192.168.71.109/VanHireQAAPI/get-vehiclesearchlist?VehicleTypeId=1");
                httpget.setHeader("X-Auth-Token", "v#1nH!%r18_8$ky399P1@3h-iR2vn");
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpget);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject Data = new JSONObject(data);
                    JSONObject vehiclesObj = Data.getJSONObject("responseData");

                    try {
                        JSONArray result = vehiclesObj.getJSONArray("vehicles");

                        for(int i=0;i<=result.length();i++)
                        {
                            JSONObject jobj = result.getJSONObject(i);
                            String VehicleName = jobj.getString("VehicleName");
                            String RegistrationNumber = jobj.getString("RegistrationNumber");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }
}



//https://stackoverflow.com/questions/24399294/android-asynctask-to-make-an-http-get-request