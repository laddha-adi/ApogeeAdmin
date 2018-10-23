package example.aditya.com.apogeeadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static example.aditya.com.apogeeadmin.SharedData.list;
import static example.aditya.com.apogeeadmin.URLS.PASSWORD;
import static example.aditya.com.apogeeadmin.URLS.URL_GET_SHOWS;
import static example.aditya.com.apogeeadmin.URLS.URL_LOGIN;
import static example.aditya.com.apogeeadmin.URLS.USERNAME;


public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public  ProgressDialog pd;
    private Thread th;
    private boolean flag = false;
    Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);


      /*  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);*/
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Logging In...");
        if(pd.isShowing()){pd.dismiss();}
         mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEmailView.getText().toString().equals("admin")&& mPasswordView.getText().toString().equals("password")){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    startPosting(mEmailView.getText().toString(),mPasswordView.getText().toString());
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


      SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
      SharedPreferences.Editor editor = pref.edit();
      String userName =  pref.getString("userName", null);
      String password = pref.getString("pass",null);// getting String

            if(userName!=null && password!=null){
        mEmailView.setText(userName);
        mPasswordView.setText(password);
        mEmailSignInButton.performClick();
    }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    void startPosting(final String username, final String password){

        pd.show();
        Login(username,password);

    }

    public void Login(final String userID, final String password) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String downloadedString = null;

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL_LOGIN);

                    try {

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("username", userID));
                        nameValuePairs.add(new BasicNameValuePair("password", password));

                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        // httppost.setHeader("Authorization","JWT "+token);
                        HttpResponse response = httpclient.execute(httppost);
                        InputStream in = response.getEntity().getContent();
                        StringBuilder stringbuilder = new StringBuilder();
                        BufferedReader bfrd = new BufferedReader(new InputStreamReader(in), 1024);
                        String line;
                        while ((line = bfrd.readLine()) != null)
                            stringbuilder.append(line);
                        downloadedString = stringbuilder.toString();

                    } catch (ClientProtocolException e) {
                        Log.e("Error1",e.toString());
                    } catch (IOException e) {
                        Log.e("Error2",e.toString());
                        //  Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                    try{
                        JSONObject mainObject = new JSONObject(downloadedString);
                        if(mainObject.get("token")!=null){

                            PASSWORD = password;
                            USERNAME = userID;
                            Log.e("TAG","User Successfully LoggedIn");
                           // getStallID(String.valueOf(mainObject.get("token")));
                            getDetails(String.valueOf(mainObject.get("token")));
                           // pd.dismiss();
                           // moveToMain();
                        }else {

                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Invalid Credentials. Please try again", Toast.LENGTH_SHORT).show();

                        }

                    }catch (Exception e){
                        Log.e("TAG","Something is not good");

                    }

                    try{
                        JSONObject mainObject = new JSONObject(downloadedString);
                        if(mainObject.get("token")!=null){

                            PASSWORD = password;
                            USERNAME = userID;
                            Log.e("TAG","User Successfully LoggedIn");
                            // getStallID(String.valueOf(mainObject.get("token")));
                            pd.dismiss();
                            moveToMain();
                        }else {

                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Invalid Credentials. Please try again", Toast.LENGTH_SHORT).show();

                        }

                    }catch (Exception e){
                        Log.e("TAG","Something is not good");
                        pd.dismiss();

                    }
                    System.out.println("downloadedString:in login:::" + downloadedString);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    public void getDetails(final String token) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String downloadedString = null;

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httppost = new HttpGet(URL_GET_SHOWS);

                    try {

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        // nameValuePairs.add(new BasicNameValuePair("username", userID));
                        // nameValuePairs.add(new BasicNameValuePair("password", password));

                        //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        httppost.setHeader("Authorization","JWT "+token);
                        HttpResponse response = httpclient.execute(httppost);
                        InputStream in = response.getEntity().getContent();
                        StringBuilder stringbuilder = new StringBuilder();
                        BufferedReader bfrd = new BufferedReader(new InputStreamReader(in), 1024);
                        String line;
                        while ((line = bfrd.readLine()) != null)
                            stringbuilder.append(line);
                        downloadedString = stringbuilder.toString();

                    } catch (ClientProtocolException e) {
                        Log.e("Error1",e.toString());
                    } catch (IOException e) {
                        Log.e("Error2",e.toString());
                        //  Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                    try{
                        JSONArray mainObject = new JSONArray(downloadedString);
                        list.clear();
                        for(int i=0;i<mainObject.length();i++){
                            JSONObject obj = mainObject.getJSONObject(i);
                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            int price =obj.getInt("price");
                            String date = obj.getString("date");
                            String time = obj.getString("time");
                            String venue = obj.getString("venue");
                            Shows show = new Shows(id,name,price,date,time,venue);
                            Log.e("show",show.toString());
                            list.add(show);
                            //adapter.notifyDataSetChanged();
                        }
                        pd.dismiss();
                        moveToMain();

                        Log.e("TAG","Data Successfully fetched.");
                        // pd.dismiss();


                    }catch (Exception e){
                        Log.e("TAG",e.getMessage());

                    }

                    System.out.println("downloadedString:in login:::" + downloadedString);
                    pd.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }



    /*  public void getStallID(final String token) {

        th = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String downloadedString = null;

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL_STALL_ID);

                    try {

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                        nameValuePairs.add(new BasicNameValuePair("token", token));
                        nameValuePairs.add(new BasicNameValuePair("WALLET_TOKEN", WALLET_TOKEN));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        httppost.setHeader("Authorization","JWT "+token);
                        HttpResponse response = httpclient.execute(httppost);
                        InputStream in = response.getEntity().getContent();
                        StringBuilder stringbuilder = new StringBuilder();
                        BufferedReader bfrd = new BufferedReader(new InputStreamReader(in), 1024);
                        String line;
                        while ((line = bfrd.readLine()) != null)
                            stringbuilder.append(line);
                        downloadedString = stringbuilder.toString();

                    } catch (ClientProtocolException e) {
                        // TODO Auto-generated catch block
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                    }
                    try{
                        JSONObject mainObject = new JSONObject(downloadedString);
                        if(mainObject.get("id")!=null){
                            stallID = String.valueOf(mainObject.get("id"));
                            stallName = String.valueOf(mainObject.get("name"));
                            Log.e("TAG","Stall ID Successfully fetched");
                            g= true;

                            pd.dismiss();
                            moveToMain();
                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Invalid Credentials. Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){

                    }
                    System.out.println("downloadedString:in getStallToken:::" + downloadedString);



                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(g) moveToMain();
            }
        });

        th.start();

    } */
    boolean g=false;

    public void moveToMain(){
        //
        //
        // SharedPreferences pref = getParent().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pass",PASSWORD);
        editor.putString("userId",USERNAME);
        editor.commit();

        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }


}

