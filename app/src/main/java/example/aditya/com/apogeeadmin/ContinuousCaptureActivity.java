package example.aditya.com.apogeeadmin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static example.aditya.com.apogeeadmin.SharedData.list;
import static example.aditya.com.apogeeadmin.URLS.PASSWORD;
import static example.aditya.com.apogeeadmin.URLS.URL_LOGIN;
import static example.aditya.com.apogeeadmin.URLS.URL_VALIDATE;
import static example.aditya.com.apogeeadmin.URLS.USERNAME;

public class ContinuousCaptureActivity extends Activity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    int pos;
    Shows show;
    ProgressDialog pd;
    TextView message_tv;
    ImageView img;
    EditText num;
    TextView tv;
    String num_tickets;

    private BarcodeCallback callback = new BarcodeCallback() {

        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.pause();

            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();
            String num_str = String.valueOf(num.getText());
            if(num_str.trim()!="") {
                num_tickets = num_str;
                startPosting(USERNAME, PASSWORD, lastText, String.valueOf(show.getShowId()), num_str);
                num.setText("1");
            }
            else{
                Toast.makeText(ContinuousCaptureActivity.this, "Enter valid number", Toast.LENGTH_SHORT).show();
            }
            //Added preview of scanned barcode

          //  ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
          //  imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
         //   onPause();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_continuous_capture);
        pd =new  ProgressDialog(this);
        pd.setMessage("Processing...");
        pos = getIntent().getIntExtra("pos",-1);
        show = list.get(pos);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);

        message_tv = (TextView) findViewById(R.id.message);
        img = (ImageView) findViewById(R.id.img_view);
        num = (EditText) findViewById(R.id.edit_text);
        num.setText("1");
        tv = (TextView) findViewById(R.id.txt_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.CAMERA);

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    1567);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant
        }
            return;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1567: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission Required.", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},1567);
                    }

                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        img.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
      //  num.setText("1");
        barcodeView.resume();

    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    void startPosting(final String username, final String password, final String bar_code,final String prof_id,final String num) {

        pd.show();
        Login(username,password,bar_code,prof_id,num);

    }

    public void Login(final String userID, final String password,final String bar_code,final String prof_id,final String num)  {

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
                            Log.e("TAG","User Successfully LoggedIn");
                        }else {
                            pd.dismiss();
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
                            getDetails(String.valueOf(mainObject.get("token")),bar_code,prof_id,num);
                            pd.dismiss();
                        }else {
                            pd.dismiss();
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

    public void getDetails(final String token, final String barcode,final String prof_id,final String num) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String downloadedString = null;

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL_VALIDATE);

                    try {

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("barcode", barcode));
                        nameValuePairs.add(new BasicNameValuePair("prof_show", prof_id));
                        nameValuePairs.add(new BasicNameValuePair("count", num));

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
                        Log.e("Error1",e.toString());
                    } catch (IOException e) {
                        Log.e("Error2",e.toString());
                        //  Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        //pd.dismiss();

                    }

                    try{
                        JSONObject mainObject = new JSONObject(downloadedString);
                         final String message = (String) mainObject.get("message");
                         final int status = (Integer) mainObject.get("status");
                       // barcodeView.setStatusText(message);

                        //  list.clear();
                        runOnUiThread(new Runnable() {
                            public void run() {
                               message_tv.setText(message);
                            if(status==1){
                                img.setImageResource(R.drawable.green_tick);
                                img.setVisibility(View.VISIBLE);

                                tv.setText(num_tickets);
                                tv.setVisibility(View.VISIBLE);
                            }
                            else{
                            try {
                                img.setImageResource(R.drawable.close_red);

                                img.setVisibility(View.VISIBLE);
                                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                // Vibrate for 500 milliseconds
                                v.vibrate(500);
                            }
                            catch (Exception e){

                            }
                               // animationView.setAnimation("checked_done_.json");
                               // animationView.playAnimation();
                               }
                            }
                        });

                        Log.e("TAG","Data Successfully fetched.");
                        // pd.dismiss();


                    }catch (Exception e){
                        Log.e("TAG",e.getMessage());

                    }

                    System.out.println("downloadedString:in login:::" + downloadedString);
                    //pd.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

}