package example.aditya.com.apogeeadmin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import static example.aditya.com.apogeeadmin.SharedData.list;

public class MainActivity extends AppCompatActivity {
ProgressDialog pd;
//public static ArrayList<Shows> list = new ArrayList<>();
RecyclerView recyclerView;
public static Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //list = new ArrayList<>();

        adapter= new Adapter(this,list);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
       // startPosting(USERNAME,PASSWORD);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
/*  void startPosting(final String username, final String password){

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
//
  //                          PASSWORD = password;
    //                        USERNAME = userID;
                            Log.e("TAG","User Successfully LoggedIn");
                            // getStallID(String.valueOf(mainObject.get("token")));
                          //  pd.dismiss();
                        }else {

                            pd.dismiss();
                            //Toast.makeText(LoginActivity.this, "Invalid Credentials. Please try again", Toast.LENGTH_SHORT).show();

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
                           getDetails(String.valueOf(mainObject.get("token")));
                            pd.dismiss();
                            //moveToMain();
                        }else {

                            pd.dismiss();
//                            Toast.makeText(LoginActivity.this, "Invalid Credentials. Please try again", Toast.LENGTH_SHORT).show();

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
                                adapter.notifyDataSetChanged();

                        }

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
*/
}
