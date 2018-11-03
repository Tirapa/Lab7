package com.example.admin.lab7;

import android.app.Activity;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnect {
    private final Activity main;
    private List<String> list;
    private String URL = "http://",GET_URL = "htdocs/get_post.php" , SENT_URL = "htdocs/sent_post.php";

    public MySQLConnect (Activity mainA){
        main = mainA;
        list = new ArrayList<String>();
    }

    public List<String> getData(){
        String url = URL + GET_URL;

        StringRequest stringRequest = StringRequest(url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                showJSON(response);
                Toast.makeText(main, list.get(0), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(main, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(main.getApplicationContext());
        requestQueue.add(stringRequest);

        return list;

    }

    public void showJSON(String response){
        String comment ="";

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);
                comment = collectData.getString("comment");
                list.add(comment);
            }
        }catch (JSONException ex) {ex.printStackTrace();}
    }
    public void sentData(String value){
        StrictMode.enableDefaults();
        if(Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            ArrayList<NameValuePair>nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("isAdd","true"));
            nameValuePairs.add(new BasicNameValuePair("comment",value));
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL + SENT_URL);
            httpPost.sentEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            httpClient.execute(httpPost);

            Toast.makeText(main, "Completed.", Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolExeception e){
            e.printStackTrace();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
